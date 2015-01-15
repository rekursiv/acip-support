package org.asianclassics.center.input.db;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.List;

import org.eclipse.swt.graphics.ImageData;
import org.ektorp.AttachmentInputStream;
import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.View;

public class SourceRepo extends CouchDbRepositorySupport<Source> {
	
	public SourceRepo(CouchDbConnector db) {
		super(Source.class, db, false);
	}

	@View(name="byCollection", map="function(doc) {if (doc.type === 'Source') emit(doc.collectionId, null)}")
	public List<Source> getByCollection(String collectionId) {
		ViewQuery q = createQuery("byCollection").includeDocs(true).key(collectionId);
		return db.queryView(q, type);
	}

	@View(name="allNeedingDispatch", map="function(doc) {if (doc.type === 'Source' && !doc.dispatch) emit([doc.projectPriority, doc.collectionId, doc.volumeIndex, doc.pageIndex], null)}")
	public List<Source> getAllNeedingDispatch(int limit) {
		ViewQuery q = createQuery("allNeedingDispatch").limit(limit).includeDocs(true);
		return db.queryView(q, type);
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
