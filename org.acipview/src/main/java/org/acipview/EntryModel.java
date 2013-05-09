package org.acipview;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.ektorp.support.Entity;
import org.joda.time.DateTime;

public class EntryModel extends Entity {
	private static final long serialVersionUID = -8518504850247837263L;

	public boolean _deleted;
	
	public boolean isValid;
	public String inputBy;
	
	public int potiIndex;
	public int sutraIndex;

	public DateTime dateTimeFirstSubmitted;
	public DateTime dateTimeLastEdited;
	

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

	public int linesPerPage;

	public String location;

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
	
	
	
}
