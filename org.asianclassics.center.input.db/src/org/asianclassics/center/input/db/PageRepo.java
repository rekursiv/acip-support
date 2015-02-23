package org.asianclassics.center.input.db;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.graphics.ImageData;
import org.ektorp.Attachment;
import org.ektorp.AttachmentInputStream;
import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.ektorp.ViewResult;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.View;

public class PageRepo extends CouchDbRepositorySupport<Page> {
	
	public PageRepo(CouchDbConnector db) {
		super(Page.class, db, false);
	}

	@View(name="byCollection", map="classpath:Page_map_byCollection.js")   //  TODO:  will we use this??
	public List<Page> getByCollection(String collectionId) {
		ViewQuery q = createQuery("byCollection").includeDocs(true).key(collectionId);
		return db.queryView(q, type);
	}

	@View(name="allNeedingDispatch", map="classpath:Page_map_allNeedingDispatch.js")
	public List<Page> getAllNeedingDispatch(int limit) {
		ViewQuery q = createQuery("allNeedingDispatch").limit(limit).includeDocs(true);
		return db.queryView(q, type);
	}

	@View(name="numPages", map="function(doc){ if (doc.recordType==='Page') emit(null, null); }", reduce="_count")
	public int getNumPages() {
		ViewQuery q = createQuery("numPages");
		ViewResult r = db.queryView(q);
		if (r.isEmpty()) return 0;
		else return r.getRows().get(0).getValueAsInt();
	}
	
	public ImageData getImage(String id) throws Exception {
		return getImage(id, this.get(id).fileName);
	}

	public ImageData getImage(String id, String attachmentId) throws Exception {
		AttachmentInputStream data = db.getAttachment(id, attachmentId);
		InputStream is = new BufferedInputStream(data);
        try {
        	return new ImageData(is);
        } finally {
    	   is.close();
    	   data.close();
        }
	}


}
