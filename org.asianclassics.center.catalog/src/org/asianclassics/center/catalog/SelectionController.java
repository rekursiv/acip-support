package org.asianclassics.center.catalog;

import java.util.List;

import org.asianclassics.center.catalog.entry.EntryRepo;
import org.asianclassics.center.catalog.entry.model.EntryModel;
import org.asianclassics.center.catalog.event.CatalogTaskMakeTopEvent;
import org.asianclassics.center.catalog.event.EntryEditEvent;
import org.asianclassics.center.catalog.event.CatalogTaskMakeTopEvent.CatalogTaskViewType;
import org.asianclassics.center.event.LoginSuccessEvent;


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
	
	
	public void addSutra() {
		repo.add(new EntryModel());
/*		
		int latestPoti = repo.getLatestPotiIndex();
		System.out.println("addSutra:  lp="+latestPoti);
		EntryModel entry = new EntryModel();
		entry.potiIndex = 2;
		entry.sutraIndex = 3;
		entry.titleTibetan = "Im new round here";
		repo.add(entry);
		eb.post(new CatalogTaskMakeTopEvent(CatalogTaskViewType.ENTRY));
		eb.post(new EntryEditEvent(entry));
*/		
	}

	public void beginPoti() {
		int latestPoti = repo.getLatestPotiIndex();
		System.out.println("beginPoti:  lp="+latestPoti);
		EntryModel entry = new EntryModel();
		entry.inputBy = workerId;
		entry.potiIndex = latestPoti+1;
		entry.sutraIndex = 1;
		entry.titleTibetan = "This sutra begins poti # "+entry.potiIndex;
		repo.add(entry);
		eb.post(new CatalogTaskMakeTopEvent(CatalogTaskViewType.ENTRY));
		eb.post(new EntryEditEvent(entry));
	}
	
	
	public List<EntryModel> getPotiList() {
		System.out.println("getPotiList");
		return repo.getPotis(workerId, 100);
	}
	
	public List<EntryModel> getSutraList(int potiIndex) {
		System.out.println("getSutraList from poti # "+potiIndex);
		return null;
	}
	
	
	public void test() {
		
		
		
		List<EntryModel> el = repo.getPotis("test", 100);
		
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
	}
	
	
	
	
}
