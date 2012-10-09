package org.asianclassics.center.input.db;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonTypeInfo;
import org.ektorp.support.Entity;
import org.ektorp.support.TypeDiscriminator;

@JsonTypeInfo(use=JsonTypeInfo.Id.NAME, include=JsonTypeInfo.As.PROPERTY, property="type")
@TypeDiscriminator("doc.type === 'InputTask'")
public class InputTask extends Entity {

	private static final long serialVersionUID = -896660677691894212L;

	private boolean isActive = true;
	private boolean isFinal = false;
	private String worker = "_init";

	private int taskPriority=10;
	private int projectPriority=10;
	
	private int collectionIndex;
	private int volumeIndex;
	private int pageIndex;
	
	private String sourceId;
	private String partnerId;
	private String taskToFixId;
	
	private String product;

	private String dateTimeAssigned;
	private String dateTimeStarted;
	private String dateTimeFinished;
	
	
	@JsonIgnore
	public void linkWithSource(Source source) {
		sourceId=source.getId();
		collectionIndex=source.getCollectionIndex();
		volumeIndex=source.getVolumeIndex();
		pageIndex=source.getPageIndex();
	}
	
	@JsonIgnore
	public void copySourceInfo(InputTask task) {
		sourceId=task.getSourceId();
		collectionIndex=task.getCollectionIndex();
		volumeIndex=task.getVolumeIndex();
		pageIndex=task.getPageIndex();
	}
	
	@JsonIgnore
	public void makeLowerPriority(InputTask task) {
		taskPriority=task.getTaskPriority()+1;
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
