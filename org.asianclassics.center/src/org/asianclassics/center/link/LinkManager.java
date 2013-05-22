package org.asianclassics.center.link;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.asianclassics.center.CenterConfig;
import org.asianclassics.center.event.LinkReadyEvent;
import org.asianclassics.center.event.LoginMessageEvent;
import org.asianclassics.center.event.StatusPanelUpdateEvent;
import org.asianclassics.database.CustomCouchDbConnector;
import org.eclipse.swt.widgets.Display;
import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.DbPath;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbConnector;
import org.ektorp.impl.StdCouchDbInstance;
import org.jgroups.Address;
import org.jgroups.JChannel;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;
import org.jgroups.blocks.locking.LockService;
import org.jgroups.util.PayloadUUID;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;


@Singleton
public class LinkManager extends ReceiverAdapter implements Runnable {
	
	private String dbCenterPrefix;
	
	private volatile boolean initInProgress = false;
	private volatile boolean initInterrupt = false;
	private volatile boolean isServer = false;
	private volatile String serverIp = null;
	
	private JChannel channel = null;
	private String myIp = null;
	private CouchDbInstance localLink;
	private CouchDbInstance remoteLink;
	private final Logger log;
	private EventBus eb;
	private LockService lockService;
	private Lock lock;
	private CenterConfig cfg;


	@Inject
	public LinkManager(Logger log, EventBus eb, CenterConfig cfg) {
		this.log = log;
		this.eb = eb;
		this.cfg = cfg;
		
		if (cfg.autoUpdateCouchViews) {
			System.getProperties().setProperty("org.ektorp.support.AutoUpdateViewOnChange", "true");
		}
	}
	
	@Override
	public void run() {
		updateStatus("Connecting to database...");
		
		if (cfg.directLinkServerIp!=null) setupStaticLink();
		else setupBootstrapLink();

		if (initInterrupt) shutdown();
		initInProgress = false;
	}
	
	
	private void setupStaticLink() {
		serverIp = cfg.directLinkServerIp;
		dbCenterPrefix = cfg.dbOrgPrefix+cfg.dbCenterCode+"-";

		try {			
			remoteLink = new StdCouchDbInstance(new StdHttpClient.Builder().host(serverIp).build());
		} catch (Exception e) {
			updateStatus("ERROR:  Cannot connect to database.");
			log.log(Level.SEVERE, "exception", e);
			shutdown();
			initInProgress = false;
			return;
		}	
		
		try {			
			channel = new JChannel("jgroups-stack.xml");
			myIp = channel.getProtocolStack().getTransport().getBindAddress().getHostAddress();
		} catch (Exception e) {
			updateStatus("ERROR:  Cannot create JChannel.");
			log.log(Level.SEVERE, "exception", e);
			shutdown();
			initInProgress = false;
			return;
		}	

		updateStatusFull("Linked with remote database at "+serverIp);
		linkReady();		
	}
	
	private void setupBootstrapLink() {
		try {
			localLink = new StdCouchDbInstance(new StdHttpClient.Builder().build());
		} catch (Exception e) {
			updateStatus("ERROR:  Cannot connect to local database.");
			log.log(Level.SEVERE, "exception", e);
			shutdown();
			initInProgress = false;
			return;
		}

		if (cfg.testDirectLink) {
			isServer = true;
			try {
				setupServer();
			} catch (Exception e) {
				updateStatus("Error setting up server. (No bootstrap??)");
				log.log(Level.SEVERE, "exception", e);
				shutdown();
				initInProgress = false;
				return;
			}
		} else {
			try {
				checkLocalForServer();
			} catch (Exception e) {
				updateStatus("ERROR:  Cannot query local database.");
				log.log(Level.SEVERE, "exception", e);
				shutdown();
				initInProgress = false;
				return;
			}
			try {
				connectGroup();
			} catch (Exception e) {
				updateStatus("ERROR:  Cannot connect to peers.");
				log.log(Level.SEVERE, "exception", e);
				shutdown();
				initInProgress = false;
				return;
			}
			initLock();
		}

	}
	
	
	
	public void init() {
		initInProgress = true;
		new Thread(this).start();
	}
	
	public void destroy() {
		if (initInProgress) initInterrupt = true;
		else shutdown();
	}
	
	public void lock() {
		if (lock==null) return;
//		log.info("Attempting lock...   ");
		boolean gotLock = false;
		try {
			gotLock = lock.tryLock(3L, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
		}
		if (!gotLock) log.severe("lock timeout");
	}
	
	public void unlock() {
		if (lock==null) return;
		lock.unlock();
	}
	
	private synchronized void shutdown() {
		log.info("Shutting down");
		if (channel!=null) channel.close();
		if (localLink!=null) {
			if (localLink.getConnection()!=null) localLink.getConnection().shutdown();
		}
		if (remoteLink!=null) {
			if (remoteLink.getConnection()!=null) remoteLink.getConnection().shutdown();
		}
	}
	
	private void checkLocalForServer() throws Exception {
		if (localLink.checkIfDbExists(new DbPath(cfg.dbOrgPrefix+"bootstrap"))) {
			log.info("DB server is local");
			isServer = true;
		} else {
			log.info("DB server is remote");
			isServer = false;
		}
	}
	
	private void connectGroup() throws Exception {
		channel = new JChannel("jgroups-stack.xml");
		myIp = channel.getProtocolStack().getTransport().getBindAddress().getHostAddress();
		log.info(myIp);

		if (isServer) {
			serverIp = myIp;
		} else {
			serverIp = null;
		}

		updateStatusFull("Waiting for server...");
		
		channel.setAddressGenerator(new PayloadAddressGenerator(serverIp));
		channel.setReceiver(this);
		channel.connect(cfg.dbOrgPrefix+"link");

	}
	
	private void initLock() {
		lockService = new LockService(channel);
		lock = lockService.getLock("acip");
	}
	
	private synchronized void updateStatusFull(final String message) {
		final String nodeType;
		if (isServer) {
			nodeType = "Server";
		}
		else {
			nodeType = "Client";
		}
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				eb.post(new StatusPanelUpdateEvent(message, myIp, nodeType));
				if (!isServer) {
					eb.post(new LoginMessageEvent("Could not connect to database.\n(Server machine needs to be powered on with ACIP app running.)"));
				}
			}
		});
	}
	
	private synchronized void updateStatus(final String message) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				eb.post(new StatusPanelUpdateEvent(message));
			}
		});
	}
	
	private synchronized void linkReady() {
		log.info("Link is ready.");
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				eb.post(new LinkReadyEvent());
			}
		});
	}
	
	private synchronized void setupServer() throws Exception {
		log.info("Setting up server...");
		if (isServer) {
			// ???
		} else {
			remoteLink = new StdCouchDbInstance(new StdHttpClient.Builder().host(serverIp).build());
		} 
		CouchDbConnector bootstrap = new StdCouchDbConnector("acip-center-bootstrap", getServerLink());
		@SuppressWarnings("unchecked")
		Map<String, Object> settings = bootstrap.get(Map.class, "settings");
		String centerCode = (String) settings.get("center-code");
		if (centerCode==null) {
			throw new Exception("Error reading center code from settings in bootstrap");
		} else {
			dbCenterPrefix = cfg.dbOrgPrefix+centerCode+"-";
			if (isServer) {
				updateStatus("Linked with local database");
			} else {
				updateStatus("Linked with remote database at "+serverIp);
			} 
			linkReady();
		}
	}
	
	public String getDbPrefix() {
		return dbCenterPrefix;
	}
	
	public CouchDbConnector getDb(String suffix) {
		return new CustomCouchDbConnector(dbCenterPrefix+suffix, getServerLink());   ///  FIXME:  handle re-assignment of DB
	}
	
	public CouchDbInstance getServerLink() {
		if (isServer) return localLink;
		else return remoteLink;
	}

	
	@Override
	public void viewAccepted(View view) {
		log.info(view.toString());
		if (isServer) {
			try {
				setupServer();
			} catch (Exception e) {
				log.log(Level.SEVERE, "exception", e);
			}
		} else {
			log.info("Finding server...");
			boolean foundServer = false;
			for (Address adr : view) {
				if (adr instanceof PayloadUUID) {
					String ip = ((PayloadUUID)adr).getPayload();
					if (ip!=null) {
						foundServer = true;
						log.info("Found server at: "+ip);
						if (serverIp==null) {
							serverIp = ip;
							try {
								setupServer();
								break;
							}  catch (Exception e) {
								log.log(Level.SEVERE, "exception", e);
								serverIp = null;
								// don't break out of loop here, maybe we'll find another server that will work...
							}
						}
					}
				}
			}
			if (foundServer==false&&serverIp!=null) {
				log.warning("detected server app leaving the group");
				// the app on the server can crash or be shut down while the database remains up,
				// so for now we just log the event and hope for the best
				// (in the future we may want to do something more sophisticated here, like test the DB connection)
			}
		}
	}
	

}
