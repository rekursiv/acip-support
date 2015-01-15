package org.asianclassics.center.input.dispatch;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.asianclassics.center.input.db.InputTask;
import org.asianclassics.center.input.db.InputTaskRepo;
import org.asianclassics.center.input.db.Source;
import org.asianclassics.center.input.db.SourceRepo;
import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbConnector;
import org.ektorp.impl.StdCouchDbInstance;


public class DispatchManager {
	
	private static final String hqDbName = "acip-hq-input";
	private static final String centerDbName = "test";
	private static Logger log = Logger.getLogger(DispatchManager.class.getName());

	private CouchDbConnector hqDb;
	private CouchDbConnector centerDb;
	private CouchDbInstance couch;
//	private CollectionRepo colRepo;
	private SourceRepo centerSrcRepo;
	private SourceRepo hqSrcRepo;
	private InputTaskRepo centerTaskRepo;
	
	public void test() {

		initDbs();
//		resetCenterDb();
		
		List<Source> srcList = hqSrcRepo.getAllNeedingDispatch(3);
		
		ArrayList<String> srcIds = new ArrayList<String>();
		for (Source src : srcList) {
			System.out.println(src.getId());
			srcIds.add(src.getId());
			InputTask it = new InputTask();
			it.linkWithSource(src);
			it.setActive(true);
			it.setDebugId();
			centerTaskRepo.add(it);
		}
		
		centerDb.replicateFrom(hqDbName, srcIds);

	}
	
	private void initDbs() {
		HttpClient httpClient = new StdHttpClient.Builder().build();
		couch = new StdCouchDbInstance(httpClient);
		centerDb = new StdCouchDbConnector(centerDbName, couch);
		centerSrcRepo = new SourceRepo(centerDb);
		centerTaskRepo = new InputTaskRepo(centerDb);
		hqDb = new StdCouchDbConnector(hqDbName, couch);
		hqSrcRepo = new SourceRepo(hqDb);
		hqSrcRepo.initStandardDesignDocument();
	}
	
	private void resetCenterDb() {
		couch.deleteDatabase(centerDbName);
		couch.createDatabase(centerDbName);
		centerSrcRepo.initStandardDesignDocument();
	}

}
