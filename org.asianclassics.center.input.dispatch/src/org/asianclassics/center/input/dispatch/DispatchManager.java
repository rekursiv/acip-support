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
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbInstance;

import util.ektorp.IdCouchDbConnector;


public class DispatchManager {
	
	private static final String hqDbName = "acip-hq-input";
	private static final String centerDbName = "acip-center-test-tasks";
	private static Logger log = Logger.getLogger(DispatchManager.class.getName());

	private CouchDbConnector hqDb;
	private CouchDbConnector centerDb;
	private CouchDbInstance couch;
//	private CollectionRepo colRepo;
	private PageRepo centerSrcRepo;
	private PageRepo hqSrcRepo;
	private InputTaskRepo centerTaskRepo;
	
	public void test() {

		initDbs();
		resetCenterDb();
		
		List<Page> srcList = hqSrcRepo.getAllNeedingDispatch(3);
		
		ArrayList<String> srcIds = new ArrayList<String>();
		for (Page src : srcList) {
			System.out.println(src.getId());
			srcIds.add(src.getId());
			InputTask it = new InputTask();
			it.linkWithSource(src);
			it.isActive=true;
			centerTaskRepo.add(it);
		}
		
		centerDb.replicateFrom(hqDbName, srcIds);

	}
	
	private void initDbs() {
		HttpClient httpClient = new StdHttpClient.Builder().build();
		couch = new StdCouchDbInstance(httpClient);
		
		hqDb = new IdCouchDbConnector(hqDbName, couch);
		hqSrcRepo = new PageRepo(hqDb);
		hqSrcRepo.initStandardDesignDocument();

		centerDb = new IdCouchDbConnector(centerDbName, couch);
		centerSrcRepo = new PageRepo(centerDb);
		centerTaskRepo = new InputTaskRepo(centerDb);
	}
	
	private void resetCenterDb() {
		couch.deleteDatabase(centerDbName);
		couch.createDatabase(centerDbName);

		centerSrcRepo.initStandardDesignDocument();
		centerTaskRepo.initStandardDesignDocument();
		new DebugRepo(centerDb).initStandardDesignDocument();
	}

}
