package org.asianclassics.center.catalog.entry.model;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.ektorp.support.Entity;



public class EntryModel extends Entity {
	private static final long serialVersionUID = -8518504850247837263L;

//	public long submitDate;
//	public int appVersion;
	
	public int potiIndex;
	public int sutraIndex;
	
	public String inputBy;
	

	public String titleTibetan;
	public String titleSanskrit;

	
	
	
	
}
