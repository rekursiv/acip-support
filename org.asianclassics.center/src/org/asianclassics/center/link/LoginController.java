package org.asianclassics.center.link;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.asianclassics.center.config.AppConfig;
import org.asianclassics.center.event.LinkReadyEvent;
import org.asianclassics.center.event.LoginMessageEvent;
import org.asianclassics.center.event.LoginRequestEvent;
import org.asianclassics.center.event.LoginSuccessEvent;
import org.asianclassics.center.event.LogoutEvent;
import org.asianclassics.center.event.MainMakeTopEvent;
import org.asianclassics.center.event.StatusPanelUpdateEvent;
import org.asianclassics.center.event.MainMakeTopEvent.MainViewType;
import org.eclipse.swt.widgets.Display;
import org.ektorp.CouchDbConnector;
import org.jgroups.Address;
import org.jgroups.JChannel;
import org.jgroups.ReceiverAdapter;
import org.jgroups.View;
import org.jgroups.util.PayloadUUID;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class LoginController extends ReceiverAdapter implements Runnable {
	
	private volatile String workerId;
	private volatile boolean isUserLoggedIn = false;
	private volatile boolean loginInProgress = false;
	private volatile boolean loginInterrupt = false;
	private final Logger log;
	private CouchDbConnector userDb;
	private EventBus eb;
	private LinkManager lm;
	private JChannel channel;
	private AppConfig cfg;
	
	@Inject
	public LoginController(Logger log, EventBus eb, LinkManager lm, AppConfig cfg) {
		this.log=log;
		this.eb = eb;
		this.lm = lm;
		this.cfg = cfg;
	}

	@Subscribe
	public void onLinkReady(LinkReadyEvent evt) {
		this.userDb = lm.getDb("users");
		if (cfg.get().autoLoginId!=null) {
			eb.post(new LoginRequestEvent(cfg.get().autoLoginId));
		}
	}
	
	@Subscribe
	public void onLoginRequest(LoginRequestEvent evt) {
		if (loginInProgress) return;
		workerId = evt.getWorkerId().toUpperCase();
		log.info("login: "+workerId);
		if (workerId==null||workerId.isEmpty()) {
			eb.post(new LoginMessageEvent("Enter your worker ID to log in."));
		} else if (userDb==null) {
			eb.post(new LoginMessageEvent("ERROR:  No connection to user database."));
		} else {
			eb.post(new LoginMessageEvent("Logging in, please wait..."));
			loginInProgress = true;
			Object user = userDb.find(Object.class, workerId);
			if (user==null) {
				eb.post(new LoginMessageEvent("User '"+workerId+"' not found.  Please try again."));
				loginInProgress = false;
			} else {
				if (cfg.get().testDirectLink) {
					loginIfExclusive(true);
					loginInProgress = false;
				} else {
					new Thread(this).start();
				}
			}
		}
	}
	
	@Subscribe
	public void onLogout(LogoutEvent evt) {
		loginInProgress = false;
		isUserLoggedIn = false;
		if (channel!=null) channel.disconnect();
		eb.post(new MainMakeTopEvent(MainViewType.LOGIN));
		eb.post(new StatusPanelUpdateEvent(""));
	}

	@Override
	public void run() {
		try {
			connectGroup();
		} catch (Exception e) {
			eb.post(new LoginMessageEvent("ERROR:  Cannot join login group."));
			log.log(Level.SEVERE, "exception", e);
			shutdown();
			loginInProgress = false;
		}
		if (loginInterrupt) shutdown();
		loginInProgress = false;
	}
	
	@Override
	public void viewAccepted(View view) {
		if (isUserLoggedIn) return;
		boolean isExclusive = true;
		for (Address adr : view) {
			if (!adr.equals(channel.getAddress()) && adr instanceof PayloadUUID) {
				String id = ((PayloadUUID)adr).getPayload();
				if (id!=null) {
					log.info("***"+workerId+":"+id);
					if (id.equals(workerId)) {
						isExclusive = false;
						break;
					}
				}
			}
		}
		loginIfExclusive(isExclusive);
	}
	
	public synchronized void loginIfExclusive(boolean isExclusive) {
		if (isExclusive) {
			Display.getDefault().asyncExec(new Runnable() {
				@Override
				public void run() {
					isUserLoggedIn = true;
					eb.post(new LoginSuccessEvent(workerId));
					eb.post(new MainMakeTopEvent(MainViewType.TASK));
					eb.post(new StatusPanelUpdateEvent(""));
				}
			});
		} else {
			Display.getDefault().asyncExec(new Runnable() {
				@Override
				public void run() {
					eb.post(new LoginMessageEvent("User '"+workerId+"' already logged into another machine in this room.\n" +
							"Please logout from the other machine before logging into this one."));
					if (channel!=null) channel.disconnect();
				}
			});
		}
	}

	public void destroy() {
		log.info("bye bye");
		if (loginInProgress) loginInterrupt = true;
		else shutdown();
	}
	
	public synchronized void shutdown() {
		log.info("shutdown");
		if (channel!=null) channel.close();
	}
	
	public boolean isUserLoggedIn() {
		return isUserLoggedIn;
	}
	
	private void connectGroup() throws Exception {
		if (channel==null) {
			channel = new JChannel("jgroups-stack.xml");
			channel.setReceiver(this);
		}
		channel.setAddressGenerator(new PayloadAddressGenerator(workerId));
		channel.connect(cfg.get().dbOrgPrefix+"login");
	}

}
