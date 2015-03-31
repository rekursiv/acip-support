package org.asianclassics.center.catalog.entry.model;

import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.ektorp.support.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class EntryModel extends Entity {
	private static final long serialVersionUID = -8518504850247837263L;

	public boolean _deleted;
	
	public boolean isValid;
	public String inputBy;
	
	public int potiIndex;
	public int sutraIndex;

	public Date dateTimeFirstSubmitted;
	public Date dateTimeLastEdited;
	

	public String libraryNumber;
	public List<AtomicInteger> stamps;
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
	public boolean isCopy = false;
	
	@JsonIgnore
	public String getSerial() {
		StringBuilder serial = new StringBuilder("M");
		serial.append(String.format("%07d", potiIndex));
		serial.append("-");
		serial.append(String.format("%03d", sutraIndex));
		return serial.toString();
	}
	
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

	
	@JsonIgnore
	public void copySizes(EntryModel src) {
		if (src!=null) {
			if (src.pageSize!=null) {
				if (pageSize==null) pageSize = new SizeModel();
				pageSize.width = src.pageSize.width;
				pageSize.height = src.pageSize.height;	
			}
			if (src.printedAreaSize!=null) {
				if (printedAreaSize==null) printedAreaSize = new SizeModel();
				printedAreaSize.width = src.printedAreaSize.width;
				printedAreaSize.height = src.printedAreaSize.height;	
			}
		}
	}
	
	
}
