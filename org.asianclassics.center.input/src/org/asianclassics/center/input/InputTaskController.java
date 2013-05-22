package org.asianclassics.center.input;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.asianclassics.center.event.LoginSuccessEvent;
import org.asianclassics.center.event.StatusPanelUpdateEvent;
import org.asianclassics.center.input.db.InputTask;
import org.asianclassics.center.input.db.InputTaskRepo;
import org.asianclassics.center.input.db.Source;
import org.asianclassics.center.input.db.SourceRepo;
import org.asianclassics.center.link.LinkManager;
import org.asianclassics.database.DateTimeStamp;


import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Display;
import org.ektorp.CouchDbConnector;

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
	private SourceRepo srcRepo;
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
		workerId = evt.getWorkerId();
		log.info(workerId);
		
		taskDb = lm.getDb("tasks");
		taskRepo = new InputTaskRepo(taskDb);
		srcRepo = new SourceRepo(taskDb);
		
		getTask();
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
				imgData = srcRepo.getImage(curTask.getSourceId(), "img.png");
			} catch (Exception e) {
				log.log(Level.WARNING, "", e);
			}

			String fixmeId = curTask.getTaskToFixId();
			if (fixmeId==null) {
				taskType = "input";
			} else {
				taskType = "correction";
				workingTxt = taskDb.get(InputTask.class, curTask.getTaskToFixId()).getProduct();
				InputTask partnerTask = taskDb.get(InputTask.class, curTask.getPartnerId());
				if (partnerTask!=null) {
					referenceTxt = partnerTask.getProduct();
					partnerWid = partnerTask.getWorker();
				}
			}
			curTask.setDateTimeStarted(DateTimeStamp.gen());
			taskDb.update(curTask);
		}
		
//		log.info("taskType: "+taskType);
		log.info("workingText: "+workingTxt);
		log.info("referenceTxt: "+referenceTxt);


		StringBuilder statMsg = new StringBuilder();
		statMsg.append(taskType);
		if (curTask!=null) {
			statMsg.append(":  v");
			statMsg.append(curTask.getVolumeIndex());
			statMsg.append(", p");
			statMsg.append(curTask.getPageIndex());			
			if (partnerWid!=null) {
				statMsg.append(", partner=");
				statMsg.append(partnerWid);
			}
		}

		
		view.setStatus(statMsg.toString());
		
		view.setImage(imgData);
		view.setReferenceText(referenceTxt);
		view.setWorkingText(workingTxt);

	}
	
	public void finishTask(String product) {
		if (curTask!=null) {
			log.info(product);
			curTask.setProduct(product);
			curTask.setActive(false);
			curTask.setDateTimeFinished(DateTimeStamp.gen());
			taskRepo.finalize(curTask);
			curTask=null;
		}
		getTask();
	}
	
	

}
