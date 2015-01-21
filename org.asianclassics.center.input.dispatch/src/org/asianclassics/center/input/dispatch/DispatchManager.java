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

	private static final String centerDbIp = "127.0.0.1";
//	private static final String centerDbIp = "192.168.0.16";
	private static final String hqDbName = "acip-hq-input";
	private static final String centerDbName = "acip-center-test-tasks";
	
	
	private static Logger log = Logger.getLogger(DispatchManager.class.getName());

	private CouchDbConnector hqDb;
	private CouchDbConnector centerDb;
	private CouchDbInstance hqCouch;
	private CouchDbInstance centerCouch;
	private PageRepo centerSrcRepo;
	private PageRepo hqPageRepo;
	private InputTaskRepo centerTaskRepo;
	
	
	
	public void dispatch(int blockSize) {
		
		initDbs();
		resetCenterDb();
		
		List<Page> srcList = hqPageRepo.getAllNeedingDispatch(blockSize);
		
		ArrayList<String> srcIds = new ArrayList<String>();
		for (Page src : srcList) {
			log.info(src.getId());
			srcIds.add(src.getId());
			InputTask it = new InputTask();
			it.linkWithSource(src);
			it.isActive=true;
			centerTaskRepo.add(it);
		}
		
		hqDb.replicateTo("http://"+centerDbIp+":5984/"+centerDbName, srcIds);

		log.info("Done.");
	}
	
	private void initDbs() {
		HttpClient hqHttpClient = new StdHttpClient.Builder().build();
		hqCouch = new StdCouchDbInstance(hqHttpClient);
		
		HttpClient centerHttpClient = new StdHttpClient.Builder().host(centerDbIp).build();
		centerCouch = new StdCouchDbInstance(centerHttpClient);
		
		hqDb = new IdCouchDbConnector(hqDbName, hqCouch);
		hqPageRepo = new PageRepo(hqDb);
		hqPageRepo.initStandardDesignDocument();

		centerDb = new IdCouchDbConnector(centerDbName, centerCouch);
		centerSrcRepo = new PageRepo(centerDb);
		centerTaskRepo = new InputTaskRepo(centerDb);
	}
	
	private void resetCenterDb() {
		centerCouch.deleteDatabase(centerDbName);
		centerCouch.createDatabase(centerDbName);

		centerSrcRepo.initStandardDesignDocument();
		centerTaskRepo.initStandardDesignDocument();
		new DebugRepo(centerDb).initStandardDesignDocument();
	}

}
