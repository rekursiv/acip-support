package org.asianclassics.center.input.db;

import java.io.BufferedInputStream;
import java.io.InputStream;

import org.eclipse.swt.graphics.ImageData;
import org.ektorp.AttachmentInputStream;
import org.ektorp.CouchDbConnector;
import org.ektorp.support.CouchDbRepositorySupport;

public class SourceRepo extends CouchDbRepositorySupport<Source> {
	
	public SourceRepo(CouchDbConnector db) {
		super(Source.class, db, false);
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

	/*
	public String getSingleAttachmentId(Source src) {
		Map<String, Attachment> attachmentMap = src.getAttachments();
		if (attachmentMap!=null && !attachmentMap.isEmpty()) {
			return attachmentMap.keySet().iterator().next();
		} else {
			return null;
		}
	}
	*/
	
}
