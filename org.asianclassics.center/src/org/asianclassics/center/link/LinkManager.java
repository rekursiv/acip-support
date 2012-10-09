package org.asianclassics.center.link;

import java.net.InetAddress;
import java.net.InterfaceAddress;
import java.net.NetworkInterface;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.asianclassics.center.LoginController;
import org.asianclassics.center.event.LinkReadyEvent;
import org.asianclassics.center.event.ServerHelpMessageEvent;
import org.asianclassics.center.event.StatusPanelUpdateEvent;
import org.asianclassics.database.CustomCouchDbConnector;
import org.eclipse.swt.widgets.Display;
import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.DbPath;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbConnector;
import org.ektorp.impl.StdCouchDbInstance;
import org.jgroups.Address;
import org.jgroups.JChannel;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;
import org.jgroups.util.PayloadUUID;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;


@Singleton
public class LinkManager extends ReceiverAdapter implements Runnable {
	
	private boolean testDirectLink = true;   // // // //
	
	protected JChannel channel = null;
	private boolean isServer = false;
	private String serverIp = null;
	private String myIp = null;
	private CouchDbInstance localLink;
	private CouchDbInstance remoteLink;
	private final Logger log;
	private boolean initInProgress = false;
	private boolean initInterrupt = false;
	private String dbPrefix;
	private EventBus eb;
	
	
	@Inject
	public LinkManager(Logger log, EventBus eb) {
		this.log = log;
		this.eb = eb;
	}
	
	@Override
	public void run() {
		updateStatus("Connecting to local database...");
		try {
			localLink = new StdCouchDbInstance(new StdHttpClient.Builder().build());
		} catch (Exception e) {
			updateStatus("ERROR:  Cannot connect to local database.");
			log.log(Level.SEVERE, "exception", e);
			shutdown();
			initInProgress = false;
			return;
		}

		if (testDirectLink) {
			isServer = true;
			try {
				setupServer();
			} catch (Exception e) {
				log.log(Level.SEVERE, "exception", e);
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
		}

		if (initInterrupt) shutdown();
		initInProgress = false;
	}
	
	public void init() {
		initInProgress = true;
		new Thread(this).start();
	}
	
	public void shutdown() {
		log.info("Shutting down");
		if (channel!=null) channel.close();
		if (localLink!=null) {
			if (localLink.getConnection()!=null) localLink.getConnection().shutdown();
		}
		if (remoteLink!=null) {
			if (remoteLink.getConnection()!=null) remoteLink.getConnection().shutdown();
		}
	}
	
	public void destroy() {
		if (initInProgress) initInterrupt = true;
		else shutdown();
	}
	
	public void checkLocalForServer() throws Exception {
		if (localLink.checkIfDbExists(new DbPath("acip-center-bootstrap"))) {
			log.info("DB server is local");
			isServer = true;
		} else {
			log.info("DB server is remote");
			isServer = false;
		}
	}
	
	public void connectGroup() throws Exception {
		channel = new JChannel("jgroups-stack.xml");
		myIp = channel.getProtocolStack().getTransport().getBindAddress().getHostAddress();
		log.info(myIp);

		if (isServer) {
			serverIp = myIp;
		} else {
			serverIp = null;
		}

		updateStatusFull("Waiting for server...");
		
		channel.setAddressGenerator(new GroupAddressGenerator(serverIp));
		channel.setReceiver(this);
		channel.connect("acip");

	}
	
	public void updateStatusFull(final String message) {
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
					eb.post(new ServerHelpMessageEvent());
				}
			}
		});
	}
	
	public void updateStatus(final String message) {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				eb.post(new StatusPanelUpdateEvent(message));
			}
		});
	}
	
	public void initLogin() {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				eb.post(new LinkReadyEvent());
			}
		});
	}
	
	public void setupServer() throws Exception {
		log.info("Seting up server...");
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
			dbPrefix = "acip-center-"+centerCode+"-";
			if (isServer) {
				updateStatus("Linked with local database");
			} else {
				updateStatus("Linked with remote database at "+serverIp);
			} 
			initLogin();
		}
	}
	
	public CouchDbConnector getDb(String suffix) {
		return new CustomCouchDbConnector(dbPrefix+suffix, getServerLink());
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
				// (in the future we may want to do something more sophisticated here...)
			}
		}
	}
	
	
	
	
	
	
	public void test() {
		String ip = "";
		try {
			ip = InetAddress.getLocalHost().getHostAddress();
			log.info(ip);
			ifconfig();
/*			
			InetAddress ia = InetAddress.getByName(dbIp);
			if (ia.isSiteLocalAddress()) log.info("isSiteLocalAddress");
			if (ia.isLinkLocalAddress()) log.info("isLinkLocalAddress");
			if (ia.isLoopbackAddress()) log.info("isLoopbackAddress");
*/
		} catch (Exception e) {
			log.log(Level.SEVERE, "exception", e);
		}
	}

	public void ifconfig() throws Exception {
		Enumeration<NetworkInterface> ifList = NetworkInterface.getNetworkInterfaces();
		NetworkInterface iface;
		while (ifList.hasMoreElements()) {
			iface = ifList.nextElement();
			log.info(iface.getDisplayName());
			log.info(iface.getName());
			log.info(iface.getInetAddresses().toString());
		}
	}
	
	public void ifconfig2() throws Exception {
	     Enumeration<NetworkInterface> theIntfList = NetworkInterface.getNetworkInterfaces();
	     List<InterfaceAddress> theAddrList = null;
	     NetworkInterface theIntf = null;
	     InetAddress theAddr = null;
	       theAddrList = theIntf.getInterfaceAddresses();
	       log.info("     int addrs: " + theAddrList.size() + " total.");
	        int addrindex = 0;
	        for(InterfaceAddress intAddr : theAddrList)
	        {
	           addrindex++;
	           theAddr = intAddr.getAddress();
	           log.info("            " + addrindex + ").");
	           log.info("            host: " + theAddr.getHostName());
	           log.info("           class: " + theAddr.getClass().getSimpleName());
	           log.info("              ip: " + theAddr.getHostAddress() + "/" + intAddr.getNetworkPrefixLength());
	           log.info("           bcast: " + intAddr.getBroadcast().getHostAddress());
//	           int maskInt = Integer.MIN_VALUE >> (intAddr.getNetworkPrefixLength()-1);
//	           System.out.println("            mask: " + toIPAddrString(maskInt));
	           log.info("           chost: " + theAddr.getCanonicalHostName());
//	           System.out.println("        byteaddr: " + toMACAddrString(theAddr.getAddress()));
	           log.info("      sitelocal?: " + theAddr.isSiteLocalAddress());
	           log.info("");
	        }
	}

}
