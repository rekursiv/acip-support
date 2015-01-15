package org.asianclassics.center.input.db;

import java.util.Date;


import org.ektorp.support.Entity;
import org.ektorp.support.TypeDiscriminator;

import com.fasterxml.jackson.annotation.JsonIgnore;

//@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.PROPERTY, property="type")
//@TypeDiscriminator("doc.type === 'InputTask'")
@SuppressWarnings("serial")
public class InputTask extends Entity {
	

	private String type = "InputTask";
	
	private boolean isActive = true;
	private boolean isFinal = false;
	private String worker = "_init";
	
	private int taskPriority=10;
	private int projectPriority=10;
	
	private String collectionId;
	private int volumeIndex;
	private int pageIndex;
	
	private String sourceId;
	private String partnerId;
	private String taskToFixId;
	
	private String product;

	private String dateTimeAssigned;
	private String dateTimeStarted;
	private String dateTimeFinished;
	
	
	@TypeDiscriminator
	public String getType() {
		return type;
	}
	
	@JsonIgnore
	public void linkWithSource(Source source) {
		sourceId=source.getId();
		collectionId=source.getCollectionId();
		volumeIndex=source.getBookIndex();
		pageIndex=source.getPageIndex();
	}
	
	@JsonIgnore
	public void copySourceInfo(InputTask task) {
		sourceId=task.getSourceId();
		collectionId=task.getCollectionId();
		volumeIndex=task.getVolumeIndex();
		pageIndex=task.getPageIndex();
	}
	
	@JsonIgnore
	public void makeLowerPriority(InputTask task) {
		taskPriority=task.getTaskPriority()+1;
	}
	
	@JsonIgnore
	public void setDebugId() {
		String time = Long.toHexString(new Date().getTime());
		setId(type+"_"+pageIndex+"_"+time);  //  TODO:  project??
	}
	

	
	public boolean isActive() {
		return isActive;
	}
	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}
	public String getSourceId() {
		return sourceId;
	}
	public void setSourceId(String source) {
		this.sourceId = source;
	}
	public String getWorker() {
		return worker;
	}
	public void setWorker(String worker) {
		this.worker = worker;
	}
	public String getProduct() {
		return product;
	}
	public void setProduct(String product) {
		this.product = product;
	}
	public boolean isFinal() {
		return isFinal;
	}
	public void setFinal(boolean isFinal) {
		this.isFinal = isFinal;
	}
	public String getPartnerId() {
		return partnerId;
	}
	public void setPartnerId(String partnerId) {
		this.partnerId = partnerId;
	}
	public String getTaskToFixId() {
		return taskToFixId;
	}
	public void setTaskToFixId(String taskToFixId) {
		this.taskToFixId = taskToFixId;
	}
	public String getDateTimeAssigned() {
		return dateTimeAssigned;
	}
	public void setDateTimeAssigned(String dateTimeAssigned) {
		this.dateTimeAssigned = dateTimeAssigned;
	}
	public String getDateTimeStarted() {
		return dateTimeStarted;
	}
	public void setDateTimeStarted(String dateTimeStarted) {
		this.dateTimeStarted = dateTimeStarted;
	}
	public String getDateTimeFinished() {
		return dateTimeFinished;
	}
	public void setDateTimeFinished(String dateTimeFinished) {
		this.dateTimeFinished = dateTimeFinished;
	}

	public String getCollectionId() {
		return collectionId;
	}

	public void setCollectionId(String collectionId) {
		this.collectionId = collectionId;
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

	public int getTaskPriority() {
		return taskPriority;
	}

	public void setTaskPriority(int taskPriority) {
		this.taskPriority = taskPriority;
	}

	public int getProjectPriority() {
		return projectPriority;
	}

	public void setProjectPriority(int projectPriority) {
		this.projectPriority = projectPriority;
	}
	

}
