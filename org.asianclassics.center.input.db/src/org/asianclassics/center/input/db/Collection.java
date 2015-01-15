package org.asianclassics.center.input.db;

import org.ektorp.support.Entity;
import org.ektorp.support.TypeDiscriminator;

@SuppressWarnings("serial")
public class Collection extends Entity {

	private final String recordType = "Collection";
	
	public String name;
	
	@TypeDiscriminator
	public String getRecordType() {
		return recordType;
	}
	

}
