package org.asianclassics.center.input;

import java.util.logging.Level;
import java.util.logging.Logger;


import org.asianclassics.center.event.LoginSuccessEvent;
import org.asianclassics.center.event.StatusPanelUpdateEvent;
import org.asianclassics.center.input.db.InputTask;
import org.asianclassics.center.input.db.InputTaskRepo;
import org.asianclassics.center.input.db.PageRepo;
import org.asianclassics.center.link.LinkManager;
import org.eclipse.swt.graphics.ImageData;
import org.ektorp.CouchDbConnector;

import util.ektorp.DateTimeStamp;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class InputTaskController {
	
	private Logger log;
	private EventBus eb;
	private LinkManager lm;
	private String workerId;
	private CouchDbConnector taskDb;
	private InputTaskRepo taskRepo;
	private PageRepo srcRepo;
	private InputTask curTask;
	private InputTaskView view;
	

	@Inject
	public InputTaskController(Logger log, EventBus eb, LinkManager lm) {
		this.log = log;
		this.eb = eb;
		this.lm = lm;
	}
	
	public void setView(InputTaskView view) {
		this.view = view;
	}
	
	@Subscribe
	public void onLogin(LoginSuccessEvent evt) {
		try {
			workerId = evt.getWorkerId();
			log.info(workerId);
			
			taskDb = lm.getDb("tasks");
			taskRepo = new InputTaskRepo(taskDb);
			srcRepo = new PageRepo(taskDb);
			
			getTask();
	//		log.info(taskDb.getDatabaseName());
	//		log.info(taskDb.getDbInfo().getDocCount()+"");
	//		log.info(""+srcRepo.getAll().size());
		} catch (Exception e) {
			e.printStackTrace();   // FIXME
		}
	}

	private void getTask() {
		String taskType = "empty";
		String workingTxt = "";
		String referenceTxt = null;
		
		curTask = taskRepo.getTask(workerId);
		
		String partnerWid = null;
		
		ImageData imgData = null;
		if (curTask!=null) {
//			srcTxt = taskDb.get(Source.class, curTask.getSourceId()).getText();    //  TEST
			try {
				imgData = srcRepo.getImage(curTask.pageId, "img.png");
			} catch (Exception e) {
				log.log(Level.WARNING, "", e);
			}

			String fixmeId = curTask.taskToFixId;
			if (fixmeId==null) {
				taskType = "input";
			} else {
				taskType = "correction";
				workingTxt = taskDb.get(InputTask.class, curTask.taskToFixId).product;
				InputTask partnerTask = taskDb.get(InputTask.class, curTask.partnerId);
				if (partnerTask!=null) {
					referenceTxt = partnerTask.product;
					partnerWid = partnerTask.worker;
				}
			}
			curTask.dateTimeStarted=DateTimeStamp.gen();
			taskDb.update(curTask);
		}
		
//		log.info("taskType: "+taskType);
//		log.info("workingText: "+workingTxt);
//		log.info("referenceTxt: "+referenceTxt);

		view.setImage(imgData);
		view.setReferenceText(referenceTxt);
		view.setWorkingText(workingTxt);
		view.setFocus();
		
		StringBuilder statMsg = new StringBuilder();
		statMsg.append(taskType);
		if (curTask!=null) {
			statMsg.append(":  b");
			statMsg.append(curTask.bookIndex);
			statMsg.append(", p");
			statMsg.append(curTask.pageIndex);			
			if (partnerWid!=null) {
				statMsg.append(", partner=");
				statMsg.append(partnerWid);
			}
		}
		eb.post(new StatusPanelUpdateEvent(statMsg.toString()));
	}
	
	public void finishTask(String product) {
		if (curTask!=null) {
			log.info(product);
			curTask.product=product;
			curTask.isActive=false;
			curTask.dateTimeFinished=DateTimeStamp.gen();
			taskRepo.finalize(curTask);
			curTask=null;
		}
		getTask();
	}

	public void save(String product) {
		if (curTask!=null) {
			curTask.product=product;
			taskRepo.update(curTask);
		}
		
	}
	
	

}
