package org.asianclassics.center.input.db;


import java.util.Date;

import org.ektorp.support.Entity;
import org.ektorp.support.TypeDiscriminator;

import com.fasterxml.jackson.annotation.JsonIgnore;


//@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.PROPERTY, property="type")
//@TypeDiscriminator("doc.type === 'Source'")
@SuppressWarnings("serial")
public class Source extends Entity {

	private String type = "Source";
	
	private int collectionIndex = 1;
	private int volumeIndex = 1;
	private int pageIndex = 1;
	
	private String text;

	@TypeDiscriminator
	public String getType() {
		return type;
	}
	
	@JsonIgnore
	public void setDebugId() {
		String time = Long.toHexString(new Date().getTime());
		setId(type+"_"+pageIndex+"_"+time);
	}
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getCollectionIndex() {
		return collectionIndex;
	}

	public void setCollectionIndex(int collectionIndex) {
		this.collectionIndex = collectionIndex;
	}

	public int getVolumeIndex() {
		return volumeIndex;
	}

	public void setVolumeIndex(int volumeIndex) {
		this.volumeIndex = volumeIndex;
	}

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}





}
