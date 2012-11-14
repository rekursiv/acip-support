package org.asianclassics.center.catalog;

import java.util.List;

import org.asianclassics.center.catalog.entry.model.EntryModel;
import org.asianclassics.center.catalog.entry.model.EntryRepo;
import org.asianclassics.center.catalog.event.CatalogTaskMakeTopEvent;
import org.asianclassics.center.catalog.event.EntryEditEvent;
import org.asianclassics.center.catalog.event.CatalogTaskMakeTopEvent.CatalogTaskViewType;
import org.asianclassics.center.event.LoginSuccessEvent;
import org.ektorp.ViewResult.Row;


import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class SelectionController {

	public static final int maxListLength = 300;
	
	private EventBus eb;
	private EntryRepo repo;
	private String workerId;

	@Inject
	public SelectionController(EventBus eb, EntryRepo repo) {
		this.eb = eb;
		this.repo = repo;
	}
	
	@Subscribe
	public void onLoginSuccess(LoginSuccessEvent evt) {
		workerId = evt.getWorkerId();
	}
	
	public void doAction(int potiIndex, int sutraIndex, String idOfEntryToEdit, String idOfEntryToCopy) {
		if (potiIndex==-1) {
			beginPoti();
		}
		else if (idOfEntryToEdit==null) {
			addSutra(potiIndex, sutraIndex, idOfEntryToCopy);
		}
		else {
			editSutra(idOfEntryToEdit);
		}
	}
	
	private void beginPoti() {
		repo.lock();
		int latestGlobalPoti = repo.getLatestPotiIndex();
		EntryModel entry = new EntryModel();
		entry.inputBy = workerId;
		entry.potiIndex = latestGlobalPoti+1;
		entry.sutraIndex = 1;
		repo.add(entry);
		repo.unlock();
		eb.post(new CatalogTaskMakeTopEvent(CatalogTaskViewType.ENTRY));
		eb.post(new EntryEditEvent(entry));
	}
	
	private void addSutra(int potiIndex, int sutraIndex, String idOfEntryToCopy) {
		EntryModel entry = new EntryModel();
		entry.potiIndex = potiIndex;
		entry.sutraIndex = sutraIndex;
		if (idOfEntryToCopy!=null) {
			EntryModel toCopy = repo.get(idOfEntryToCopy);  //  TODO: catch exception, log error
			entry.format = toCopy.format;
			entry.author = toCopy.author;
			entry.inkColor = toCopy.inkColor;
			entry.paperSource = toCopy.paperSource;
			entry.paperColor = toCopy.paperColor;
			entry.paperGrade = toCopy.paperGrade;
			entry.readability = toCopy.readability;
			entry.volume = toCopy.volume;
			entry.linesPerPage = toCopy.linesPerPage;
			entry.copySizes(toCopy);
			entry.isCopy = true;
		}
		repo.add(entry);
		eb.post(new CatalogTaskMakeTopEvent(CatalogTaskViewType.ENTRY));
		eb.post(new EntryEditEvent(entry));
	}

	private void editSutra(String id) {
		EntryModel entry = repo.get(id);
		eb.post(new CatalogTaskMakeTopEvent(CatalogTaskViewType.ENTRY));
		eb.post(new EntryEditEvent(entry));
	}
	
	public List<Row> getPotiList() {
		return repo.getPotis(workerId, maxListLength);
	}
	
	public List<Row> getSutraList(int potiIndex) {
		return repo.getSutras(potiIndex, maxListLength);
	}
	
}
