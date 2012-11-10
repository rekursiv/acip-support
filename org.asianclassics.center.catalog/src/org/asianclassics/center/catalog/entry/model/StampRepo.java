package org.asianclassics.center.catalog.entry.model;


import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.asianclassics.center.link.LinkManager;
import org.eclipse.swt.graphics.ImageData;
import org.ektorp.Attachment;
import org.ektorp.AttachmentInputStream;
import org.ektorp.ViewQuery;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.View;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class StampRepo extends CouchDbRepositorySupport<StampModel> {

	private LinkManager lm;

	@Inject
	public StampRepo(LinkManager lm) {
		super(StampModel.class, lm.getDb("stamps"), true);
		this.lm = lm;
		initStandardDesignDocument();    //  DEBUG
	}
	
	@View(name="byIndex", map="function(doc) {emit(doc.index, null)}")
	public StampModel getByIndex(int index) {
		ViewQuery q = createQuery("byIndex").limit(1).includeDocs(true).key(index);
		List<StampModel> sl = db.queryView(q, type);
		if (sl==null||sl.isEmpty()) return null;
		else return sl.get(0);
	}

	public ImageData getImageByIndex(int index) throws Exception {
		StampModel stamp = getByIndex(index);
		if (stamp==null) throw new Exception("Index not found in database.");
		String imageId = getSingleAttachmentId(stamp);
		if (imageId==null) throw new Exception("Problem with attached image.");
		return getImage(stamp.getId(), imageId);
	}
	
	public String getSingleAttachmentId(StampModel stamp) {
		Map<String, Attachment> attachmentMap = stamp.getAttachments();
		if (attachmentMap!=null && attachmentMap.size()==1) {
			return attachmentMap.keySet().iterator().next();
		} else {
			return null;
		}
	}
	
	public ImageData getImage(String id, String attachmentId) throws IOException {
		AttachmentInputStream data = db.getAttachment(id, attachmentId);
		InputStream is = new BufferedInputStream(data);
		return new ImageData(is);
	}

	
	
	public void lock() {
		lm.lock();
	}
	
	public void unlock() {
		lm.unlock();
	}

}
