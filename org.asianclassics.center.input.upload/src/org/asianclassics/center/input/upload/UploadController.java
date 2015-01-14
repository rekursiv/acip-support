package org.asianclassics.center.input.upload;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.logging.Logger;

import org.asianclassics.center.input.db.Collection;
import org.asianclassics.center.input.db.CollectionRepo;
import org.asianclassics.center.input.db.Source;
import org.asianclassics.center.input.db.SourceRepo;
import org.ektorp.AttachmentInputStream;
import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbConnector;
import org.ektorp.impl.StdCouchDbInstance;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class UploadController {
	
	private static final String colName = "books";
	private static final String baseDir = "C:/projects/ACIP/input_test_sample/";
	private static final String dbName = "acip-hq-input";
	
	private Logger log;
	private CouchDbConnector db;
	private CouchDbInstance couch;
	private CollectionRepo colRepo;
	private SourceRepo srcRepo;
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
		db = new StdCouchDbConnector(dbName, couch);
		colRepo = new CollectionRepo(db);
		srcRepo = new SourceRepo(db);
	}
	
	private void resetDb() {
		couch.deleteDatabase(dbName);
		couch.createDatabase(dbName);
	}
	
	public void test() {
	
		initDb();
		resetDb();
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
		}
	}
	
	private void addCollection() {
		curCollection = new Collection();
		curCollection.setName(colName);
		curCollection.setDebugId();
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
//			break;
		}
	}
	
	public void addSource(File file) {
		if (srcRepo==null) {
			log.info("Source Repository not initialized!");
			return;
		}
		Source s = new Source();
		s.setCollectionId(curCollection.getId());
		s.setBookIndex(bookIndex);
		s.setPageIndex(pageIndex);
		s.setDebugId(colName);
		srcRepo.add(s);
		attachImage(s, file);
//		s.setStaus("uploaded");  //  TODO
//		srcRepo.update(s);
	}
	
	public void attachImage(Source src, File file) {
		InputStream is = null;
		try {
			is = new FileInputStream(file);
			AttachmentInputStream ais = new AttachmentInputStream("img.png", is, "image/png");
			db.createAttachment(src.getId(), src.getRevision(), ais);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void sortTest() {
		File dir = new File("C:/projects/ACIP/renovationpictures");
		
		File[] fileList = dir.listFiles();
		File x = fileList[2];
		fileList[2]=fileList[5];
		fileList[5]=x;
		
		for (File file : fileList) {
			log.info(file.getName());
		}
		log.info("----");
		
		Arrays.sort(fileList);
		for (File file : fileList) {
			log.info(file.getName());
		}
	}
}
