package org.asianclassics.center;

import java.util.logging.Logger;

import org.asianclassics.center.event.LinkReadyEvent;
import org.asianclassics.center.event.LoginFailureEvent;
import org.asianclassics.center.event.LoginRequestEvent;
import org.asianclassics.center.event.LoginSuccessEvent;
import org.asianclassics.center.event.LogoutEvent;
import org.asianclassics.center.event.MainMakeTopEvent;
import org.asianclassics.center.event.MainMakeTopEvent.MainViewType;
import org.asianclassics.center.link.LinkManager;
import org.ektorp.CouchDbConnector;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class LoginController {
	
	private final Logger log;
	private CouchDbConnector userDb;
	private EventBus eb;
	private LinkManager lm;
	
	@Inject
	public LoginController(Logger log, EventBus eb, LinkManager lm) {
		this.log=log;
		this.eb = eb;
		this.lm = lm;
	}

	@Subscribe
	public void onLinkReady(LinkReadyEvent evt) {
		this.userDb = lm.getDb("users");
		eb.post(new LoginRequestEvent("mdm"));						/////////////////    DEBUG:  Auto Login    /////////////////
	}
	
	@Subscribe
	public void onLoginRequest(LoginRequestEvent evt) {
		String id = evt.getWorkerId().toLowerCase();
		log.info("login: "+id);
		if (id==null||id.isEmpty()) {
			eb.post(new LoginFailureEvent(id, "Enter your worker ID to log in."));
		} else if (userDb==null) {
			eb.post(new LoginFailureEvent(id, "ERROR:  No connection to user database."));
		} else {
			Object user = userDb.find(Object.class, id);
			if (user==null) {
				eb.post(new LoginFailureEvent(id, "User '"+id+"' not found.  Please try again."));
			} else {
				eb.post(new LoginSuccessEvent(id));
				eb.post(new MainMakeTopEvent(MainViewType.TASK));
			}
		}
	}
	
	@Subscribe
	public void onLogout(LogoutEvent evt) {
		eb.post(new MainMakeTopEvent(MainViewType.LOGIN));
	}
	

}
