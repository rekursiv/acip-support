package org.asianclassics.center.input.db;

import java.util.Date;

import org.ektorp.support.Entity;
import org.ektorp.support.TypeDiscriminator;

import com.fasterxml.jackson.annotation.JsonIgnore;

@SuppressWarnings("serial")
public class Collection extends Entity {


	private String type = "Collection";
	private String name;
	
	@TypeDiscriminator
	public String getType() {
		return type;
	}
	
	@JsonIgnore
	public void setDebugId() {
		String time = Long.toHexString(new Date().getTime());
		setId(type+"_"+name+"_"+time);
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
}
