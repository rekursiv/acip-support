package org.asianclassics.center.catalog.entry;

import java.io.IOException;
import java.net.URI;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.asianclassics.center.catalog.CatalogApp;
import org.asianclassics.center.catalog.CatalogConfig;
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
import org.asianclassics.center.event.StatusPanelUpdateEvent;
import org.eclipse.swt.widgets.Display;
import org.ektorp.ViewResult.Row;


import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class EntryController implements Runnable {
	
	private EntryModel model;
	private EventBus eb;
	
	private volatile int autoSaveDelayInMs;
	private volatile boolean autoSaveTimerRunning = false;
	private volatile boolean autoSaveArmed = false;  
	private volatile boolean isModified = false;
	private boolean isValid;
	private boolean checkWorkMsgDisplayed = false;
	private EntryRepo repo;
	private Logger log;

	private CatalogConfig cfg;
	
	@Inject
	public EntryController(Logger log, EventBus eb, EntryRepo repo, CatalogConfig cfg) {
		this.log = log;
		this.eb = eb;
		this.repo = repo;
		this.cfg = cfg;
		autoSaveDelayInMs = cfg.catalogAutoSaveDelay*500;  // convert to ms, divide by 2  (timer is fired twice before save)
	}
	
	@Subscribe
	public void onEdit(EntryEditEvent evt) {
		autoSaveArmed = false;         // start out disarmed to prevent redundant save after load
		checkWorkMsgDisplayed = false;
		model = evt.getEntry();
		if (model==null) {
			if (cfg.catalogInitModelId!=null) {
				model = repo.get(cfg.catalogInitModelId);
			} else {
				model = new EntryModel();
				log.severe("ERROR:  EntryEditEvent was posted with NULL model");
			}
		}
		log.info("Editing "+model.getId()+"   "+model.getSerial());
		checkIfDeleteAllowed();
		eb.post(new EntryModelPostReadEvent());
	}
	
	@Override
	public void run() {
//		log.info("run TOP");
		autoSaveTimerRunning = true;
		try {
			Thread.sleep(autoSaveDelayInMs);
		} catch (InterruptedException e) {
			autoSaveTimerRunning = false;
			return;
		}
		
		if (isModified) {
			isModified = false;
			new Thread(this).start();
		} else if (autoSaveArmed) {
			log.info("DO AUTO SAVE");
			writeAsync();
			isModified = false;
		} else {
			log.info("ARM AUTO SAVE");
			isModified = false;
			autoSaveArmed = true;
		}
		autoSaveTimerRunning = false;
//		log.info("run BOTTOM");
	}


	public void onModify() {
//		log.info("");
		if (!autoSaveTimerRunning&&autoSaveDelayInMs>100) {
			autoSaveTimerRunning = true;
			new Thread(this).start();
		}
		if (checkWorkMsgDisplayed) {
			eb.post(new EntryUserMessageEvent(""));   // clear message
			checkWorkMsgDisplayed = false;
		}
		eb.post(new StatusPanelUpdateEvent(""));   // clear status
		isModified = true;
	}

	public void invalidate() {
		isValid = false;
	}

	public EntryModel getModel() {
		return model;
	}
	
	public void submit() {
		validate();
		model.isValid = isValid;
		if (isValid) {
			if (model.dateTimeFirstSubmitted==null) {
				model.dateTimeFirstSubmitted = new Date();
			}
			write();
			endEditSession();
//			eb.post(new EntryUserMessageEvent("All entries are valid."));        ////////////////////   TEST  
		} else {
			write();
			eb.post(new EntryUserMessageEvent("Please check your input and re-submit."));
			checkWorkMsgDisplayed = true;
		}
	}
	
	public void saveAsDraft() {
		write();
		endEditSession();
	}
	
	
	public void viewRawData() {
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
	
	public void delete() {
		model._deleted=true;
		repo.update(model);
		endEditSession();
	}

	
	
	private void checkIfDeleteAllowed() {
		boolean deleteAllowed = true;
		if (model.sutraIndex==1) {  //  deleting sutra #1 would make the entire poti invisible in the selection interface
			List<Row> sutraList = repo.getSutras(model.potiIndex, 2);
			if (sutraList.size()>1) deleteAllowed = false;  // so we prevent that from happening unless #1 is the only one
		}
		if (deleteAllowed) eb.post(new EntryDeleteAllowedEvent());
	}
	
	private void writeAsync() {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				write();
			}
		});
	}
	
	private void write() {
		eb.post(new EntryModelPreWriteEvent());
		Date now = new Date();
		model.dateTimeLastEdited = now;
		try {
			repo.update(model);
			// TODO:  remove "go back" btn
		} catch (Exception e) {
			log.log(Level.SEVERE, "ERROR", e);
			return;
		}
//		eb.post(new StatusPanelUpdateEvent("Saved "+model.getSerial()+" to database at "+now.toString("h:mm a")));   //  FIXME
	}

	private void validate() {
		isValid = true;
		eb.post(new EntryValidateEvent());
	}
	
	private void endEditSession() {
		eb.post(new CatalogTaskMakeTopEvent(CatalogTaskViewType.SELECTION));
		autoSaveArmed = false;
	}

}
