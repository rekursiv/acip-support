package org.asianclassics.center.catalog.entry.model;


import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.asianclassics.center.link.LinkManager;
import org.eclipse.swt.graphics.ImageData;
import org.ektorp.Attachment;
import org.ektorp.AttachmentInputStream;
import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.ektorp.ViewResult;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.View;

import com.google.inject.Inject;
import com.google.inject.Singleton;


@Singleton
public class StampRepo extends CouchDbRepositorySupport<StampModel> {
	private LinkManager lm;
	public static final String[] categories = new String[] {"Chinese", "Lanycha", "Mongolian", "Picture", "Tibetan Circle", "Tibetan Drawing", "Tibetan Single", "Tibetan Square", "New"};


	@Inject
	public StampRepo(LinkManager lm) {
		super(StampModel.class, lm.getDb("stamps"), true);
		this.lm = lm;
		initStandardDesignDocument();      //  TODO:  make configurable
	}
	
	@View(name="byIndex", map="function(doc) {emit(doc.index, null)}")
	public StampModel getByIndex(int index) {
		ViewQuery q = createQuery("byIndex").limit(1).includeDocs(true).key(index);
		List<StampModel> sl = db.queryView(q, type);
		if (sl==null||sl.isEmpty()) return null;
		else return sl.get(0);
	}
	
	@View(name="byCategory", map="function(doc) {emit(doc.category, null)}")
	public List<StampModel> getByCategory(String category) {
		ViewQuery q = createQuery("byCategory").includeDocs(true).key(category);
		return db.queryView(q, type);
	}
	
	@View(name="getLatestStampIndex", map="function(doc) {emit(doc.index, doc.index)}")
	public int getLatestStampIndex() {
		ViewQuery q = createQuery("getLatestStampIndex").descending(true).limit(1).includeDocs(false);
		ViewResult r = db.queryView(q);
		if (r.isEmpty()) return 0;  // TODO:  throw exception??
		return r.getRows().get(0).getValueAsInt();
	}

	public ImageData getImageByIndex(int index) throws Exception {
		StampModel stamp = getByIndex(index);
		if (stamp==null) throw new Exception("Index not found in database.");
		return getImage(stamp);
	}
	
	public ImageData getImage(StampModel stamp) throws Exception {
		String imageId = getSingleAttachmentId(stamp);
		if (imageId==null) throw new Exception("Problem with attached image.");
		return getImage(stamp.getId(), imageId);
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
	
	public String getSingleAttachmentId(StampModel stamp) {
		Map<String, Attachment> attachmentMap = stamp.getAttachments();
		if (attachmentMap!=null && !attachmentMap.isEmpty()) {
			return attachmentMap.keySet().iterator().next();
		} else {
			return null;
		}
	}

	public CouchDbConnector getDb() {
		return db;
	}
	
	
	public void lock() {
		lm.lock();
	}
	
	public void unlock() {
		lm.unlock();
	}

}
