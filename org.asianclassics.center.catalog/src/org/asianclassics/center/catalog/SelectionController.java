package org.asianclassics.center.catalog;

import java.util.List;

import org.asianclassics.center.catalog.entry.EntryRepo;
import org.asianclassics.center.catalog.entry.model.EntryModel;
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

	private EventBus eb;
	private EntryRepo repo;
	private String workerId;

	@Inject
	public SelectionController(EventBus eb, EntryRepo repo) {
		this.eb = eb;
		this.repo = repo;
		repo.initStandardDesignDocument();    //  DEBUG
	}
	
	@Subscribe
	public void onLoginSuccess(LoginSuccessEvent evt) {
		workerId = evt.getWorkerId();
		System.out.println("worker="+workerId);
	}
	
	public void doAction(int potiIndex, int sutraIndex, String id) {
		if (potiIndex==-1) {
			beginPoti();
		}
		else if (id==null) {
			addSutra(potiIndex, sutraIndex);
		}
		else {
			editSutra(id);
		}
	}
	
	private void beginPoti() {
		int latestGlobalPoti = repo.getLatestPotiIndex();
		System.out.println("beginPoti:  lgp="+latestGlobalPoti);
		EntryModel entry = new EntryModel();
		entry.inputBy = workerId;
		entry.potiIndex = latestGlobalPoti+1;
		entry.sutraIndex = 1;
		entry.titleTibetan = "This sutra begins poti # "+entry.potiIndex;
		repo.add(entry);
		eb.post(new CatalogTaskMakeTopEvent(CatalogTaskViewType.ENTRY));
		eb.post(new EntryEditEvent(entry));
	}
	
	private void addSutra(int potiIndex, int sutraIndex) {
		EntryModel entry = new EntryModel();
		entry.submitDate = 2012;                       ///    TEST
		entry.potiIndex = potiIndex;
		entry.sutraIndex = sutraIndex;
		entry.titleTibetan = "This is sutra # "+entry.sutraIndex+" in poti # "+entry.potiIndex;
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
		return repo.getPotis(workerId, 100);
	}
	
	public List<Row> getSutraList(int potiIndex) {
		return repo.getSutras(potiIndex, 100);
	}
	

	
	
	
	
	
	public void test() {
		List<Row> sutraList = repo.getSutras(1, 100);
		System.out.println(sutraList.get(0).getValueAsNode().toString());
	}
	
}
