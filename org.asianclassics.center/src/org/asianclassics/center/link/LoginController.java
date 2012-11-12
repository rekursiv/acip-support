package org.asianclassics.center.link;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.asianclassics.center.event.LinkReadyEvent;
import org.asianclassics.center.event.LoginMessageEvent;
import org.asianclassics.center.event.LoginRequestEvent;
import org.asianclassics.center.event.LoginSuccessEvent;
import org.asianclassics.center.event.LogoutEvent;
import org.asianclassics.center.event.MainMakeTopEvent;
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
public class LoginController extends ReceiverAdapter {
	
	private final Logger log;
	private CouchDbConnector userDb;
	private EventBus eb;
	private LinkManager lm;
	private boolean isUserLoggedIn = false;
	private JChannel channel;
	private String workerId;
	
	@Inject
	public LoginController(Logger log, EventBus eb, LinkManager lm) {
		this.log=log;
		this.eb = eb;
		this.lm = lm;
	}

	@Subscribe
	public void onLinkReady(LinkReadyEvent evt) {
		this.userDb = lm.getDb("users");
//		eb.post(new LoginRequestEvent("tng"));						/////////////////    DEBUG:  Auto Login    /////////////////
	}
	
	@Subscribe
	public void onLoginRequest(LoginRequestEvent evt) {
		workerId = evt.getWorkerId().toLowerCase();
		log.info("login: "+workerId);
		if (workerId==null||workerId.isEmpty()) {
			eb.post(new LoginMessageEvent("Enter your worker ID to log in."));
		} else if (userDb==null) {
			eb.post(new LoginMessageEvent("ERROR:  No connection to user database."));
		} else {
			Object user = userDb.find(Object.class, workerId);
			if (user==null) {
				eb.post(new LoginMessageEvent("User '"+workerId+"' not found.  Please try again."));
			} else {
				if (LinkManager.testDirectLink) {
					loginIfExclusive(true);
				} else {
//					eb.post(new LoginFailureEvent(workerId, "Logging in..."));  // FIXME:  needs async to work
					try {
						connectGroup();
					} catch (Exception e) {
						log.log(Level.SEVERE, "exception", e);
					}
				}
			}
		}
	}
	
	@Subscribe
	public void onLogout(LogoutEvent evt) {
		if (channel!=null) channel.disconnect();
		isUserLoggedIn = false;
		eb.post(new MainMakeTopEvent(MainViewType.LOGIN));
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
	
	public void loginIfExclusive(boolean isExclusive) {
		if (isExclusive) {
			Display.getDefault().asyncExec(new Runnable() {
				@Override
				public void run() {
					isUserLoggedIn = true;
					eb.post(new LoginSuccessEvent(workerId));
					eb.post(new MainMakeTopEvent(MainViewType.TASK));
				}
			});
		} else {
			Display.getDefault().asyncExec(new Runnable() {
				@Override
				public void run() {
					eb.post(new LoginMessageEvent("User '"+workerId+"' already logged into another machine in this room.\nPlease logout from that machine before logging into this one."));
					if (channel!=null) channel.disconnect();
				}
			});
		}

	}

	public void destroy() {
		log.info("bye bye");
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
		channel.connect(LinkManager.dbOrgPrefix+"login");
	}


}
