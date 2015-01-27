package org.asianclassics.center.input.dispatch;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.asianclassics.center.input.db.DebugRepo;
import org.asianclassics.center.input.db.InputTask;
import org.asianclassics.center.input.db.InputTaskRepo;
import org.asianclassics.center.input.db.Page;
import org.asianclassics.center.input.db.PageRepo;
import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.ReplicationStatus;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbInstance;

import util.ektorp.DateTimeStamp;
import util.ektorp.IdCouchDbConnector;


public class DispatchManager {

	private static final int centerPageCount = 4;
	
	private static final int copyDataFromCenterBlockSize = 30;
	
	private static final String centerDbIp = "127.0.0.1";
//	private static final String centerDbIp = "192.168.0.16";
	private static final String hqDbName = "acip-hq-input";
	private static final String centerName = "test";
	
	
	private static Logger log = Logger.getLogger(DispatchManager.class.getName());

	private CouchDbConnector hqDb;
	private CouchDbConnector centerDb;
	private CouchDbInstance hqCouch;
	private CouchDbInstance centerCouch;
	private PageRepo centerPageRepo;
	private PageRepo hqPageRepo;
	private InputTaskRepo centerTaskRepo;
	
	
	public DispatchManager() {
		System.getProperties().setProperty("org.ektorp.support.AutoUpdateViewOnChange", "true");
	}
	
	public void copyDataFromCenter() {
		List<InputTask> nonActiveTasks = centerTaskRepo.getNonActive(copyDataFromCenterBlockSize);
		
		// pass one:  replicate into HQ database
		ArrayList<String> itIds = new ArrayList<String>();
		for (InputTask it : nonActiveTasks) {
			itIds.add(it.getId());
			//  TODO:  any reason to copy IT with worker=_init  ????
		}
		ReplicationStatus status = hqDb.replicateFrom("http://"+centerDbIp+":5984/"+getCenterDbName(), itIds);
		if (status.isOk()) log.info("replication OK");
		else log.warning("replication FAIL");
		
		// pass two:  for all isFinal, copy text into HQ:Page and delete Center:Page
//		itIds.clear();
		for (InputTask it : nonActiveTasks) {
			if (it.isFinal==true && it.pageId!=null) {
				System.out.println("Final: "+it.pageIndex);
				Page p = hqPageRepo.get(it.pageId);
				if (p==null) {
					log.warning("cannot find page id in HQ database: "+it.pageId);
				}
				else {
					// copy text into HQ page
					p.text = it.product;
					hqPageRepo.update(p);

					// delete Center page
					p = centerPageRepo.get(it.pageId);
					if (p!=null) centerDb.delete(p);
				}
			}
			else System.out.println("---");
		}
		
		// pass three: delete from Center db
		for (InputTask it : nonActiveTasks) {
			centerDb.delete(it);
		}
		
	}
	
	public void copyDataToCenter(int blockSize) {
		
		List<Page> srcList = hqPageRepo.getAllNeedingDispatch(blockSize);
		
		ArrayList<String> pageIds = new ArrayList<String>();
		for (Page page : srcList) {
			log.info("Preparing page "+page.pageIndex+" in book "+page.bookIndex);
			pageIds.add(page.getId());
			page.dispatchedTo=centerName;
			hqPageRepo.update(page);
			
			InputTask it = new InputTask();
			it.linkWithSource(page);
			it.isActive=true;
			it.center=centerName;
			it.dateTimeDispatched=DateTimeStamp.gen();
			centerTaskRepo.add(it);
		}
		
		ReplicationStatus status = hqDb.replicateTo("http://"+centerDbIp+":5984/"+getCenterDbName(), pageIds);
		if (status.isOk()) log.info("replication OK");
		else log.warning("replication FAIL");

	}
	
	public void reassignIfNeeded() {
		//  TODO:   for all active ITs with dateTimeAssigned older than X days, reassign to "_any"
	}

	public void reset() {
		initDbs();
		log.info("Deleting and re-creating Center DB....");		
		resetCenterDb();
		initDesignDocs();
		log.info("Done.");
	}
	
	public void dispatch() {
		initDbs();
		initDesignDocs();

		copyDataFromCenter();
		
		int blockSize = centerPageCount - centerPageRepo.getNumPages();
		if (blockSize>0) copyDataToCenter(blockSize);
		
		reassignIfNeeded();

		log.info("Done.");
	}

	public void test() {
		initDbs();
		initDesignDocs();
		
		System.out.println(centerPageRepo.getNumPages());
		
	}



	private void initDbs() {
		HttpClient hqHttpClient = new StdHttpClient.Builder().build();
		hqCouch = new StdCouchDbInstance(hqHttpClient);
		
		HttpClient centerHttpClient = new StdHttpClient.Builder().host(centerDbIp).build();
		centerCouch = new StdCouchDbInstance(centerHttpClient);
		
		hqDb = new IdCouchDbConnector(hqDbName, hqCouch);
		hqPageRepo = new PageRepo(hqDb);
		hqPageRepo.initStandardDesignDocument();

		centerDb = new IdCouchDbConnector(getCenterDbName(), centerCouch);
		centerPageRepo = new PageRepo(centerDb);
		centerTaskRepo = new InputTaskRepo(centerDb);
	}
	
	private void resetCenterDb() {
		centerCouch.deleteDatabase(getCenterDbName());
		centerCouch.createDatabase(getCenterDbName());
	}
	
	private void initDesignDocs() {
		centerPageRepo.initStandardDesignDocument();
		centerTaskRepo.initStandardDesignDocument();
		new DebugRepo(centerDb).initStandardDesignDocument();
	}
	
	private String getCenterDbName() {
		return "acip-center-"+centerName+"-tasks";
	}

}
