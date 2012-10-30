package org.asianclassics.center.catalog.entry;

import java.util.List;

import org.asianclassics.center.catalog.entry.model.EntryModel;
import org.asianclassics.center.catalog.entry.model.EntryRepo;
import org.asianclassics.center.catalog.event.CatalogTaskMakeTopEvent;
import org.asianclassics.center.catalog.event.EntryEditEvent;
import org.asianclassics.center.catalog.event.EntryModelPostReadEvent;
import org.asianclassics.center.catalog.event.EntryModelPreWriteEvent;
import org.asianclassics.center.catalog.event.EntryValidateEvent;
import org.asianclassics.center.catalog.event.CatalogTaskMakeTopEvent.CatalogTaskViewType;
import org.asianclassics.database.DateTimeStamp;
import org.ektorp.ViewResult.Row;
import org.joda.time.DateTime;
import org.joda.time.chrono.ISOChronology;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class EntryController {
	
	private EntryModel model;
	private EventBus eb;
	
	private boolean isModified;
	private boolean isValid;
	private EntryRepo repo;
	
	@Inject
	public EntryController(EventBus eb, EntryRepo repo) {
		this.eb = eb;
		this.repo = repo;
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
	public void onEdit(EntryEditEvent evt) {
		model = evt.getEntry();
		if (model==null) model = repo.get("13aac4d64950076f5a9");					////////////////////   TEST    //  13aac4d64950076f5a9=test,  13aac4d646d007481b0
		eb.post(new EntryModelPostReadEvent());
		isModified = false;
	}
	
	public void submit() {
		validate();
		model.isValid = isValid;
		if (isValid) {
			if (model.dateTimeFirstSubmitted==null) {
				model.dateTimeFirstSubmitted = new DateTime();
			}
			write();
			eb.post(new CatalogTaskMakeTopEvent(CatalogTaskViewType.SELECTION));
		} else {
			write();
			// TODO:  show msg to user
		}
		
	}
	
	public void saveAsDraft() {
		write();
		eb.post(new CatalogTaskMakeTopEvent(CatalogTaskViewType.SELECTION));
	}
	
	public void delete() {
		if (model!=null) {
			boolean okToDelete = true;
			if (model.sutraIndex==1) {  //  deleting sutra #1 would make the entire poti invisible in the selection interface
				List<Row> sutraList = repo.getSutras(model.potiIndex, 2);
				System.out.println("size="+sutraList.size());
				if (sutraList.size()>1) okToDelete = false;  // so we prevent that from happening unless #1 is the only one
			}
			// TODO:  get verification from user
			if (okToDelete) {
				model._deleted=true;
				repo.update(model);
				eb.post(new CatalogTaskMakeTopEvent(CatalogTaskViewType.SELECTION));
			} else {
				System.out.println("NOT OK to delete!");
				// TODO:  show msg to user
			}
		}
	}
	
	
	
	private void write() {
		if (model!=null) {
			System.out.println("isModified="+isModified);
			eb.post(new EntryModelPreWriteEvent());
			model.dateTimeLastEdited = new DateTime();
			repo.update(model);
		}
	}

	private void validate() {
		isValid = true;
		eb.post(new EntryValidateEvent());
		System.out.println("isValid="+isValid);
	}
	
	
	
	public void test() {                       ////////////////////   TEST

	}

}
