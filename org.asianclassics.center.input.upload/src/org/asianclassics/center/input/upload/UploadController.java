package org.asianclassics.center.input.upload;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.logging.Logger;

import org.asianclassics.center.input.db.Collection;
import org.asianclassics.center.input.db.CollectionRepo;
import org.asianclassics.center.input.db.DebugRepo;
import org.asianclassics.center.input.db.Page;
import org.asianclassics.center.input.db.PageRepo;
import org.ektorp.AttachmentInputStream;
import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbInstance;

import util.ektorp.IdCouchDbConnector;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class UploadController {

	private static final String dbName = "acip-hq-input";
	
	private int maxPages = 2;
	private String colName = "tengyur";
	private String baseDir = "C:/projects/ACIP/input_test_sample/";
	
	private Logger log;
	private CouchDbConnector db;
	private CouchDbInstance couch;
	private CollectionRepo colRepo;
	private PageRepo srcRepo;
	private Collection curCollection;

	private int pageIndex;
	private int bookIndex;

	
	@Inject
	public UploadController(Logger log) {
		this.log = log;
	}

	private void initDb() {
		HttpClient httpClient = new StdHttpClient.Builder().build();
		couch = new StdCouchDbInstance(httpClient);
		db = new IdCouchDbConnector(dbName, couch);
		colRepo = new CollectionRepo(db);
		srcRepo = new PageRepo(db);
	}
	
	private void resetDb() {
		couch.deleteDatabase(dbName);
		couch.createDatabase(dbName);
	}
	
	private void initDesignDocs() {
		colRepo.initStandardDesignDocument();
		srcRepo.initStandardDesignDocument();
		new DebugRepo(db).initStandardDesignDocument();		
	}
	
	public void setMaxPages(int pages) {
		maxPages = pages;
	}
	
	public void setColName(String name) {
		colName = name;
	}
	
	public void setBaseDir(String dir) {
		baseDir = dir;
	}
	
	public void reset() {
		initDb();
		log.info("Deleting and re-creating HQ DB....");	
		resetDb();
		initDesignDocs();
		log.info("Done.");
	}
	
	public void upload() {
	
		initDb();
		initDesignDocs();
		
		addCollection();
		
		bookIndex = 1;
		File dir = new File(baseDir+colName);
		File[] fileList = dir.listFiles();
		if (fileList!=null) {
			if (fileList[0].isDirectory()) {
				uploadBooks(dir);
			} else {
				uploadFiles(dir);
			}
			log.info("Done.");			
		} else {
			log.info("Path not found: "+baseDir+colName);
		}
	}
	
	private void addCollection() {
		curCollection = new Collection();
		curCollection.name = colName;
		colRepo.add(curCollection);
	}

	private void uploadBooks(File colDir) {
		File[] bookDirList = colDir.listFiles();
		for (File bookDir : bookDirList) {
			if (bookDir.isDirectory()) {
				log.info("Uploading book #"+bookIndex+" in directory "+bookDir.getName());
				uploadFiles(bookDir);
				++bookIndex;
			}
		}
	}

	
	public void uploadFiles(File dir) {
		pageIndex = 1;
		File[] fileList = dir.listFiles();
		Arrays.sort(fileList);
		for (File file : fileList) {
			log.info("  Uploading page #"+pageIndex+", filename="+file.getName());
			addSource(file);
			++pageIndex;
			if (pageIndex > maxPages) break;
		}
	}
	
	public void addSource(File file) {
		if (srcRepo==null) {
			log.info("Source Repository not initialized!");
			return;
		}
		Page s = new Page();
		s.collectionId=curCollection.getId();
		s.bookIndex=bookIndex;
		s.pageIndex=pageIndex;
		srcRepo.add(s);
		attachImage(s, file);
//		s.setStaus("uploaded");  //  TODO
//		srcRepo.update(s);
	}
	
	
	public void attachImage(Page src, File file) {
		InputStream is = null;
		try {
			is = new FileInputStream(file);
			AttachmentInputStream ais = new AttachmentInputStream("img.png", is, "image/png");
			db.createAttachment(src.getId(), src.getRevision(), ais);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	

}
