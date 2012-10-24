package org.asianclassics.center.catalog.entry.model;

import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.ektorp.support.Entity;



//@JsonIgnoreProperties(ignoreUnknown = true)
public class EntryModel extends Entity {
	private static final long serialVersionUID = -8518504850247837263L;

	public long submitDate;
	public int appVersion;
	public String inputBy;
	
	
	private String libraryNumber;
	private List<Integer> stamps;
	private String titleTibetan;
	private String titleSanskrit;
	private ExtraLanguageModel extraLanguage;
	private String format;
	private String titleTibetanBrief;
	private String author;
	private String originDate;
	private String inkColor;
	private PaperColorModel paperColor;
	private String paperSource;
	private String paperGrade;
	private String readability;
	private String volume;
	private PageModel sutraPages;
	private List<PageModel> missingPages;
	private int linesPerPage;
	private SizeModel pageSize;
	private SizeModel printedAreaSize;
	private String location;
	private List<DrawingModel> drawings;
	private String colophon;
	
	
	
	
	public String getLibraryNumber() {
		return libraryNumber;
	}
	public void setLibraryNumber(String libraryNumber) {
		this.libraryNumber = libraryNumber;
	}
	public List<Integer> getStamps() {
		return stamps;
	}
	public void setStamps(List<Integer> stamps) {
		this.stamps = stamps;
	}
	public String getTitleTibetan() {
		return titleTibetan;
	}
	public void setTitleTibetan(String titleTibetan) {
		this.titleTibetan = titleTibetan;
	}
	public String getTitleSanskrit() {
		return titleSanskrit;
	}
	public void setTitleSanskrit(String titleSanskrit) {
		this.titleSanskrit = titleSanskrit;
	}
	public ExtraLanguageModel getExtraLanguage() {
		return extraLanguage;
	}
	public void setExtraLanguage(ExtraLanguageModel extraLanguage) {
		this.extraLanguage = extraLanguage;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public String getTitleTibetanBrief() {
		return titleTibetanBrief;
	}
	public void setTitleTibetanBrief(String titleTibetanBrief) {
		this.titleTibetanBrief = titleTibetanBrief;
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getOriginDate() {
		return originDate;
	}
	public void setOriginDate(String originDate) {
		this.originDate = originDate;
	}
	public String getInkColor() {
		return inkColor;
	}
	public void setInkColor(String inkColor) {
		this.inkColor = inkColor;
	}
	public PaperColorModel getPaperColor() {
		return paperColor;
	}
	public void setPaperColor(PaperColorModel paperColor) {
		this.paperColor = paperColor;
	}
	public String getPaperSource() {
		return paperSource;
	}
	public void setPaperSource(String paperSource) {
		this.paperSource = paperSource;
	}
	public String getPaperGrade() {
		return paperGrade;
	}
	public void setPaperGrade(String paperGrade) {
		this.paperGrade = paperGrade;
	}
	public String getReadability() {
		return readability;
	}
	public void setReadability(String readability) {
		this.readability = readability;
	}
	public String getVolume() {
		return volume;
	}
	public void setVolume(String volume) {
		this.volume = volume;
	}
	public PageModel getSutraPages() {
		return sutraPages;
	}
	public void setSutraPages(PageModel sutraPages) {
		this.sutraPages = sutraPages;
	}
	public List<PageModel> getMissingPages() {
		return missingPages;
	}
	public void setMissingPages(List<PageModel> missingPages) {
		this.missingPages = missingPages;
	}
	public int getLinesPerPage() {
		return linesPerPage;
	}
	public void setLinesPerPage(int linesPerPage) {
		this.linesPerPage = linesPerPage;
	}
	public SizeModel getPageSize() {
		return pageSize;
	}
	public void setPageSize(SizeModel pageSize) {
		this.pageSize = pageSize;
	}
	public SizeModel getPrintedAreaSize() {
		return printedAreaSize;
	}
	public void setPrintedAreaSize(SizeModel printedAreaSize) {
		this.printedAreaSize = printedAreaSize;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public List<DrawingModel> getDrawings() {
		return drawings;
	}
	public void setDrawings(List<DrawingModel> drawings) {
		this.drawings = drawings;
	}
	public String getColophon() {
		return colophon;
	}
	public void setColophon(String colophon) {
		this.colophon = colophon;
	}
	


	
}
