package org.asianclassics.center.catalog.entry;

import org.asianclassics.center.catalog.entry.model.EntryModel;
import org.asianclassics.center.catalog.event.EntryModelPostReadEvent;
import org.asianclassics.center.catalog.event.EntryModelPreWriteEvent;
import org.asianclassics.center.catalog.event.EntryValidateEvent;
import org.asianclassics.center.catalog.event.TestEvent;
import org.asianclassics.center.link.LinkManager;
import org.asianclassics.database.CustomCouchDbConnector;
import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class EntryController {
	
	private EntryModel model;
	private LinkManager lm;
	private EventBus eb;
	
	private boolean isModified;
	private boolean isValid;
	
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
	
	
	public void read() {
		CouchDbInstance couch = lm.getServerLink();
		CouchDbConnector db = new CustomCouchDbConnector("acip-nlm-catalog", couch); 
		model = db.get(EntryModel.class, "M0057421-001");    //      M0057413-001, M0057415-001, TEST=M0057421-001

		eb.post(new EntryModelPostReadEvent());
		isModified = false;
	}

	public void write() {
		if (model!=null) {
			System.out.println("isModified="+isModified);
			eb.post(new EntryModelPreWriteEvent());
			
		}
	}

	public void validate() {
		isValid = true;
		eb.post(new EntryValidateEvent());
		System.out.println("isValid="+isValid);
	}

}
