package org.asianclassics.center.catalog.entry.model;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.ektorp.support.Entity;
import org.joda.time.DateTime;

public class EntryModel extends Entity implements Model {
	private static final long serialVersionUID = -8518504850247837263L;

	public boolean _deleted;
	
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
	public String paperColor;
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
	
	
	
	@JsonIgnore
	public int getPageWidth() {
		if (pageSize==null) return 0;
		else return pageSize.width;
	}
	
	@JsonIgnore
	public void setPageWidth(int num) {
		if (pageSize==null) pageSize = new SizeModel();
		pageSize.width = num;
	}
	
	@JsonIgnore
	public int getPageHeight() {
		if (pageSize==null) return 0;
		else return pageSize.height;
	}
	
	@JsonIgnore
	public void setPageHeight(int num) {
		if (pageSize==null) pageSize = new SizeModel();
		pageSize.height = num;
	}
	
	
	@JsonIgnore
	public int getPrintedAreaWidth() {
		if (printedAreaSize==null) return 0;
		else return printedAreaSize.width;
	}
	
	@JsonIgnore
	public void setPrintedAreaWidth(int num) {
		if (printedAreaSize==null) printedAreaSize = new SizeModel();
		printedAreaSize.width = num;
	}
	
	@JsonIgnore
	public int getPrintedAreaHeight() {
		if (printedAreaSize==null) return 0;
		else return printedAreaSize.height;
	}
	
	@JsonIgnore
	public void setPrintedAreaHeight(int num) {
		if (printedAreaSize==null) printedAreaSize = new SizeModel();
		printedAreaSize.height = num;
	}
	
	
}
