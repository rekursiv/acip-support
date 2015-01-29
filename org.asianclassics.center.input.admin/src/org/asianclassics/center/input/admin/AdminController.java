package org.asianclassics.center.input.admin;

import java.util.List;
import java.util.logging.Logger;

import org.asianclassics.center.input.db.CollectionRepo;
import org.asianclassics.center.input.db.DebugRepo;
import org.asianclassics.center.input.db.InputTask;
import org.asianclassics.center.input.db.InputTaskRepo;
import org.asianclassics.center.input.db.PageRepo;
import org.asianclassics.text.edit.DiffUtil;
import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbInstance;

import util.ektorp.IdCouchDbConnector;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class AdminController {

	private static final boolean printDetails = false;
	
	private static final String dbName = "acip-hq-input";
	private Logger log;
	
	private CouchDbConnector db;
	private CouchDbInstance couch;
	private InputTaskRepo taskRepo;
	private DiffUtil diff = new DiffUtil();

	@Inject
	public AdminController(Logger log) {
		this.log = log;
	}

	public void test() {
		initDb();
		initDesignDocs();
		
		printWorkerStats("A");
		printWorkerStats("B");
		
		log.info("Done.");
	}

	private void printWorkerStats(String worker) {
		System.out.println("---------------");
		System.out.println("Worker ID: "+worker);
		
		int numPagesInput = 0;
		int numCharsInput = 0;
		int numPagesCorrected = 0;
		int numCorrections = 0;
		List<InputTask> tasks = taskRepo.getByWorker(worker);
		for (InputTask it : tasks) {
			if (it.taskToFixId==null) {
				if (printDetails) System.out.println("I:"+it.pageIndex+":"+it.product.length());
				++numPagesInput;
				numCharsInput+=it.product.length();
			}
			else {
				InputTask toFix = taskRepo.get(it.taskToFixId);
				int numCor = diff.getNumCorrections(toFix.product, it.product);
				if (printDetails) System.out.println("C:"+it.pageIndex+":"+numCor);
				++numPagesCorrected;
				numCorrections+=numCor;
			}
		}
		System.out.println("Pages Input: "+numPagesInput);
		System.out.println("Characters Input: "+numCharsInput);
		System.out.println("Pages Corrected: "+numPagesCorrected);
		System.out.println("Corrections made: "+numCorrections);		
	
		
	}
	
	
	private void initDb() {
		HttpClient httpClient = new StdHttpClient.Builder().build();
		couch = new StdCouchDbInstance(httpClient);
		db = new IdCouchDbConnector(dbName, couch);
		taskRepo = new InputTaskRepo(db);
	}
	
	private void initDesignDocs() {
		taskRepo.initStandardDesignDocument();
		new DebugRepo(db).initStandardDesignDocument();		
	}
	
}
