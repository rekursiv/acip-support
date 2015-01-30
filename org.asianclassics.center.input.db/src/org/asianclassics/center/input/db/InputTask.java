package org.asianclassics.center.input.db;

import org.ektorp.support.Entity;
import org.ektorp.support.TypeDiscriminator;
import com.fasterxml.jackson.annotation.JsonIgnore;


@SuppressWarnings("serial")
public class InputTask extends Entity {
	private final String recordType = "InputTask";
	private final int dataVersion = 1;
	
	public boolean isActive = true;
	public boolean isFinal = false;
	
	public String worker = "_init";
	public String center;
	
	public int taskIndex = 0;
	public int projectPriority = 100;
	
	public int bookIndex;
	public int pageIndex;
	
	public String collectionId;
	public String pageId;
	public String partnerId;
	public String taskToFixId;
	
	public String product;
	
	public String dateTimeAssigned;
	public String dateTimeStarted;
	public String dateTimeFinished;
	public int secondsWorked;
	

	@TypeDiscriminator
	public String getRecordType() {
		return recordType;
	}
	
	public int getDataVersion() {
		return dataVersion;
	}
	
	@JsonIgnore
	public void linkWithSource(Page page) {
		pageId=page.getId();
		collectionId=page.collectionId;
		bookIndex=page.bookIndex;
		pageIndex=page.pageIndex;
		projectPriority=page.projectPriority;
	}
	
	@JsonIgnore
	public void copySourceInfo(InputTask task) {
		pageId=task.pageId;
		collectionId=task.collectionId;
		bookIndex=task.bookIndex;
		pageIndex=task.pageIndex;
		projectPriority=task.projectPriority;
		center=task.center;
	}
	
	@JsonIgnore
	public void incrementTaskIndex(InputTask task) {
		taskIndex=task.taskIndex+1;
	}
	

}
