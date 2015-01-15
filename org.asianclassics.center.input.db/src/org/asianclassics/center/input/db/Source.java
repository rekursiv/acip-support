package org.asianclassics.center.input.db;

import java.util.Date;

import org.ektorp.support.Entity;
import org.ektorp.support.TypeDiscriminator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@SuppressWarnings("serial")
public class Source extends Entity {

	private String type = "Source";
	
	private String collectionId = null;
	
	private int bookIndex = 1;
	private int pageIndex = 1;
	
	public int projectPriority = 10;	
	public boolean dispatched = false;

	private String text;  //  TODO?

	@TypeDiscriminator
	public String getType() {
		return type;
	}
	
	@JsonIgnore
	public void setDebugId(String colName) {
		String time = Long.toHexString(new Date().getTime());
		setId(type+"_"+colName+"-"+bookIndex+"-"+pageIndex+"_"+time);
	}
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getCollectionId() {
		return collectionId;
	}

	public void setCollectionId(String collectionId) {
		this.collectionId = collectionId;
	}

	public int getBookIndex() {
		return bookIndex;
	}

	public void setBookIndex(int bookIndex) {
		this.bookIndex = bookIndex;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}





}
