package org.asianclassics.center.input;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.asianclassics.center.event.LoginSuccessEvent;
import org.asianclassics.center.event.StatusPanelUpdateEvent;
import org.asianclassics.center.input.db.InputTask;
import org.asianclassics.center.input.db.InputTaskRepo;
import org.asianclassics.center.input.db.PageRepo;
import org.asianclassics.center.link.LinkManager;
import org.asianclassics.text.edit.AcipEditor;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Display;
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
	private InputTest test;
	private ScanPanel scan;
	private AcipEditor editor;
	

	@Inject
	public InputTaskController(Logger log, EventBus eb, LinkManager lm, InputTest test) {
		this.log = log;
		this.eb = eb;
		this.lm = lm;
		this.test = test;
	}
	
	public void setEditor(AcipEditor editor) {
		this.editor = editor;
	}

	public void setScan(ScanPanel scan) {
		this.scan = scan;
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

	
	
	public void finishTask() {
		if (curTask!=null && editor!=null) {
			curTask.product=editor.getWorkingText();
			curTask.isActive=false;
			curTask.dateTimeFinished=DateTimeStamp.gen();
			taskRepo.finalize(curTask);
			curTask=null;
		}
		getTask();
	}

	public void save() {
		if (curTask!=null && editor!=null) {
			curTask.product=editor.getWorkingText();
			taskRepo.update(curTask);
		}
		
	}

	public void testInput(InputTest.ErrorType errorType) {
		if (curTask!=null) editor.setWorkingText(test.getPage(curTask.pageIndex, errorType));
	}

	
	
	private void getTask() {
		String taskType = "EMPTY";
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
				taskType = "Input";
				workingTxt = curTask.product;
			} else {
				taskType = "Correction";
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
		
		log.info("taskType: "+taskType);
		log.info("workingText: "+workingTxt);
		log.info("referenceTxt: "+referenceTxt);

		scan.setImage(imgData);
		editor.setReferenceText(referenceTxt);
		editor.setWorkingText(workingTxt);
		setFocus();
		
		StringBuilder statMsg = new StringBuilder();
		statMsg.append(taskType);
		if (curTask!=null) {
			statMsg.append(":  Book ");
			statMsg.append(curTask.bookIndex);
			statMsg.append(", Page ");
			statMsg.append(curTask.pageIndex);			
			if (partnerWid!=null) {
				statMsg.append(", Partner=");
				statMsg.append(partnerWid);
			}
		}
		eb.post(new StatusPanelUpdateEvent(statMsg.toString()));
	}
	
	
	private void setFocus() {
		Display.getDefault().asyncExec(new Runnable() {
			@Override
			public void run() {
				editor.setFocus();
			}
		});
	}

}
