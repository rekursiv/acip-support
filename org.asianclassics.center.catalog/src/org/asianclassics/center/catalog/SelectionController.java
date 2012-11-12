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
		System.out.println("worker="+workerId);
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
		System.out.println("beginPoti:  lgp="+latestGlobalPoti);
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
		System.out.println("SelectionController#addSutra id="+idOfEntryToCopy);
		EntryModel entry = new EntryModel();

		entry.potiIndex = potiIndex;
		entry.sutraIndex = sutraIndex;

		//  copy
		if (idOfEntryToCopy!=null) {
			System.out.println("   fetch model to copy");
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
		
		/*
function(doc) {
  emit(doc._id, {format:doc.format, author:doc.author, inkColor:doc.inkColor, paperSource:doc.paperSource, paperColor:doc.paperColor, 
  paperGrade:doc.paperGrade, readability:doc.readability, volume:doc.volume, linesPerPage:doc.linesPerPage, 
  pageSize:doc.pageSize, printedAreaSize:doc.printedAreaSize});
}
		 */
		
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
	

	
	
	
	
	
	public void test() {
		List<Row> sutraList = repo.getSutras(1, 100);
		System.out.println(sutraList.get(0).getValueAsNode().toString());
	}
	
}
