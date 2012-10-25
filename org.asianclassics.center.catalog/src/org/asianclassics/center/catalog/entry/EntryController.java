package org.asianclassics.center.catalog.entry;

import org.asianclassics.center.catalog.entry.model.EntryModel;
import org.asianclassics.center.catalog.event.EntryModelPostReadEvent;
import org.asianclassics.center.catalog.event.EntryModelPreWriteEvent;
import org.asianclassics.center.catalog.event.EntryValidateEvent;
import org.asianclassics.center.catalog.event.TestEvent;
import org.asianclassics.center.event.LoginSuccessEvent;
import org.asianclassics.center.link.LinkManager;
import org.asianclassics.database.CustomCouchDbConnector;
import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class EntryController {
	
	private EntryModel model;
	private LinkManager lm;
	private EventBus eb;
	
	private boolean isModified;
	private boolean isValid;
	private EntryRepo entryRepo;
	
	@Inject
	public EntryController(EventBus eb, LinkManager lm) {
		this.eb = eb;
		this.lm = lm;
	}

	public EntryModel getModel() {
		return model;
	}

	public void onModify() {
		isModified = true;
	}

	public void invalidate() {
		isValid = false;
	}
	
	
	///////////////////////
	

	@Subscribe
	public void onLogin(LoginSuccessEvent evt) {
		entryRepo = new EntryRepo(lm.getDb("catalog"));
	}
	
	
	public void read() {
//		model = entryRepo.get("M0057421-001");    //      M0057413-001, M0057415-001, TEST=M0057421-001
		model = entryRepo.get("13a97262be40000");
		eb.post(new EntryModelPostReadEvent());
		isModified = false;
	}

	public void write() {
		if (model!=null) {
			System.out.println("isModified="+isModified);
			eb.post(new EntryModelPreWriteEvent());
			entryRepo.update(model);                       //   TODO:  how to create new??
		}
	}

	public void validate() {
		isValid = true;
		eb.post(new EntryValidateEvent());
		System.out.println("isValid="+isValid);
	}
	
	
	
	public void test() {                       ////////////////////   TEST
		EntryModel e = new EntryModel();
		e.setAuthor("me");
		e.setColophon("it's a long story");
		entryRepo.add(e);
	}

}
