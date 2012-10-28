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
	private int latestPoti;
	private int latestSutra;

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
	
	
	public void addSutra() {
		System.out.println("addSutra:  lp="+latestPoti);
		EntryModel entry = new EntryModel();
		entry.potiIndex = latestPoti;
		entry.sutraIndex = latestSutra+1;
		entry.titleTibetan = "This is sutra # "+entry.sutraIndex+" in poti # "+entry.potiIndex;
		repo.add(entry);
		eb.post(new CatalogTaskMakeTopEvent(CatalogTaskViewType.ENTRY));
		eb.post(new EntryEditEvent(entry));
		
	}

	public void beginPoti() {
		int latestGlobalPoti = repo.getLatestPotiIndex();
		System.out.println("beginPoti:  lp="+latestGlobalPoti);
		EntryModel entry = new EntryModel();
		entry.inputBy = workerId;
		entry.potiIndex = latestGlobalPoti+1;
		entry.sutraIndex = 1;
		entry.titleTibetan = "This sutra begins poti # "+entry.potiIndex;
		repo.add(entry);
		eb.post(new CatalogTaskMakeTopEvent(CatalogTaskViewType.ENTRY));
		eb.post(new EntryEditEvent(entry));
	}
	
	
	public List<Row> getPotiList() {
		List<Row> potiList = repo.getPotis(workerId, 100);
//		potiList.add(index, element)
		return potiList;
	}
	
	public List<Row> getSutraList(int potiIndex) {
		return repo.getSutras(potiIndex, 100);
	}
	
/*	
	public List<EntryModel> _getPotiList() {
		System.out.println("getPotiList");
		List<EntryModel> potiList = repo.getPotis(workerId, 100);
		if (!potiList.isEmpty()) latestPoti = potiList.get(0).potiIndex;
		return potiList;
	}
	*/
	
/*	
	public List<EntryModel> getSutraList(int potiIndex) {
		System.out.println("getSutraList from poti # "+potiIndex);
		List<EntryModel> sutraList = repo.getSutras(potiIndex, 100);
		if (!sutraList.isEmpty()) latestSutra = sutraList.get(0).sutraIndex;
		return sutraList;
	}
	*/
	
	
	
	public void test() {
		
		List<Row> sl = getSutraList(1);
		
		System.out.println(sl.get(0).getValueAsNode().get("titleTibetan"));
		
		/*
		
//		List<EntryModel> el = repo.getPotis("test", 100);
		
//		WritableList test = new WritableList(el, EntryModel.class);
		
//		tableViewer.setContentProvider();
		
		for (EntryModel e : el) {
			System.out.println("inputby: "+e.inputBy);
			System.out.println("potiIndex: "+e.potiIndex);
			System.out.println("sutraIndex: "+e.sutraIndex);
			System.out.println("titleTibetan: "+e.titleTibetan);
		}
		
//		repo.test();
//		int latestPoti = repo.getLatestPotiIndex();
//		System.out.println("test:  lp="+latestPoti);
 * 
 * *
 */
	}
	
	
	
	
}
