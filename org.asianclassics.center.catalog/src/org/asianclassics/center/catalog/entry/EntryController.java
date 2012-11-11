package org.asianclassics.center.catalog.entry;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.asianclassics.center.catalog.CatalogApp;
import org.asianclassics.center.catalog.entry.model.EntryModel;
import org.asianclassics.center.catalog.entry.model.EntryRepo;
import org.asianclassics.center.catalog.event.CatalogTaskMakeTopEvent;
import org.asianclassics.center.catalog.event.CatalogTaskMakeTopEvent.CatalogTaskViewType;
import org.asianclassics.center.catalog.event.EntryDeleteAllowedEvent;
import org.asianclassics.center.catalog.event.EntryEditEvent;
import org.asianclassics.center.catalog.event.EntryModelPostReadEvent;
import org.asianclassics.center.catalog.event.EntryModelPreWriteEvent;
import org.asianclassics.center.catalog.event.EntryUserMessageEvent;
import org.asianclassics.center.catalog.event.EntryValidateEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.MessageBox;
import org.ektorp.ViewResult.Row;
import org.joda.time.DateTime;

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
	private Logger log;
	
	@Inject
	public EntryController(Logger log, EventBus eb, EntryRepo repo) {
		this.log = log;
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
		if (CatalogApp.debugMode) {
			if (model==null) model = repo.get("13ae733504c00035c89");					////////////////////   TEST 
		}
		checkIfDeleteAllowed();
		eb.post(new EntryModelPostReadEvent());
		isModified = false;		
	}
	
	public void submit() {
		if (model!=null) {
			validate();
			model.isValid = isValid;
			if (isValid) {
				if (model.dateTimeFirstSubmitted==null) {
					model.dateTimeFirstSubmitted = new DateTime();
				}
				write();
	//			eb.post(new CatalogTaskMakeTopEvent(CatalogTaskViewType.SELECTION));    ////////////////////   TEST  
				eb.post(new EntryUserMessageEvent("All entries are valid."));        ////////////////////   TEST  
			} else {
				write();
				eb.post(new EntryUserMessageEvent("Please check your input and re-submit."));
			}
		}
	}
	

	public void saveAsDraft() {
		write();
		eb.post(new CatalogTaskMakeTopEvent(CatalogTaskViewType.SELECTION));
	}
	
	
	public void viewRawData() {
		if (model!=null) {
			StringBuilder url = new StringBuilder("http://127.0.0.1:5984/");
			url.append("_utils/document.html?");  // for viewing in Futon  (comment out this line for pretty-print-JSON)
			url.append(repo.getDbName());
			url.append("/");
			url.append(model.getId());
			log.info(url.toString());
			try {
				java.awt.Desktop.getDesktop().browse(URI.create(url.toString()));
			} catch (IOException e) {
				log.log(Level.INFO, "", e);
			}
		}
	}
	
	public void delete() {
		if (model!=null) {
			model._deleted=true;
			repo.update(model);
			eb.post(new CatalogTaskMakeTopEvent(CatalogTaskViewType.SELECTION));
		}
	}

	private void checkIfDeleteAllowed() {
		if (model!=null) {
			boolean deleteAllowed = true;
			if (model.sutraIndex==1) {  //  deleting sutra #1 would make the entire poti invisible in the selection interface
				List<Row> sutraList = repo.getSutras(model.potiIndex, 2);
				System.out.println("size="+sutraList.size());
				if (sutraList.size()>1) deleteAllowed = false;  // so we prevent that from happening unless #1 is the only one
			}
			if (deleteAllowed) eb.post(new EntryDeleteAllowedEvent());
		}
	}
	
	private void write() {
		if (model!=null) {
			System.out.println("EntryController#write() - isModified="+isModified);
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
		System.out.println("TEST   isModified="+isModified);
	}

	public void requestDelete_OLD() {
		if (model!=null) {
			boolean okToDelete = true;
			if (model.sutraIndex==1) {  //  deleting sutra #1 would make the entire poti invisible in the selection interface
				List<Row> sutraList = repo.getSutras(model.potiIndex, 2);
				System.out.println("size="+sutraList.size());
				if (sutraList.size()>1) okToDelete = false;  // so we prevent that from happening unless #1 is the only one
			}
			// TODO:  get verification from user
			if (okToDelete) {
				delete();
			} else {
				System.out.println("NOT OK to delete!");
				// TODO:  show msg to user
			}
		}
	}

}
