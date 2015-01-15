package org.asianclassics.center.input.db;

import org.ektorp.support.Entity;
import org.ektorp.support.TypeDiscriminator;

@SuppressWarnings("serial")
public class Source extends Entity {
	private final String recordType = "Source";
	
	public String collectionId = null;
	public int bookIndex = 1;
	public int pageIndex = 1;
	public int projectPriority = 100;
	public boolean dispatched = false;

	public String text;

	@TypeDiscriminator
	public String getRecordType() {
		return recordType;
	}
	

}
