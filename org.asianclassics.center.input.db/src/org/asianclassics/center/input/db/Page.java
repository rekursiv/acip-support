package org.asianclassics.center.input.db;

import org.ektorp.support.Entity;
import org.ektorp.support.TypeDiscriminator;

@SuppressWarnings("serial")
public class Page extends Entity {
	private final String recordType = "Page";
	private final int dataVersion = 1;
	
	public String collectionId = null;
	
	public int bookIndex = 1;
	public int pageIndex = 1;
	
	public int projectPriority = 100;
	
	public String dispatchedTo = null;
	
	public String text = null;

	@TypeDiscriminator
	public String getRecordType() {
		return recordType;
	}
	
	public int getDataVersion() {
		return dataVersion;
	}

}
