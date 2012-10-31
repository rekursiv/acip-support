package org.asianclassics.center.catalog.entry.model;

import java.util.List;

import org.ektorp.support.Entity;
import org.joda.time.DateTime;

public class EntryModel extends Entity implements Model {
	private static final long serialVersionUID = -8518504850247837263L;

	public boolean _deleted;
	
	public String appId;
	public boolean isValid;
	public String inputBy;
	
	public int potiIndex;
	public int sutraIndex;

	public DateTime dateTimeFirstSubmitted;
	public DateTime dateTimeLastEdited;
	

	public String libraryNumber;
	public List<Integer> stamps;
	public String titleTibetan;
	public String titleSanskrit;
	
	
	public String extraLanguage;
	
	public String format;
	public String titleTibetanBrief;
	public String author;
	public String originDate;
	public String inkColor;
	
	public String paperColor;   ///
	
	public String paperSource;
	public String paperGrade;
	public String readability;
	public String volume;
	public SutraPageModel sutraPages;
	public List<MissingPageModel> missingPages;

	public int linesPerPage;
	public SizeModel pageSize;
	public SizeModel printedAreaSize;
	public String location;
	public List<DrawingModel> drawings;
	public String colophon;
	
	
}
