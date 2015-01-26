
package org.asianclassics.center.input.db;



import java.util.List;

import org.ektorp.ComplexKey;
import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.View;

import util.ektorp.DateTimeStamp;
import util.ektorp.Id;

public class InputTaskRepo extends CouchDbRepositorySupport<InputTask> {
	
	private static final int taskBlockSize = 2;
	
	public InputTaskRepo(CouchDbConnector db) {
		super(InputTask.class, db, false);
	}

	
	//  TODO:  handle exception thrown when design doc missing
	@View(name="assignedTasks", map="classpath:InputTask_map_assignedTasks.js")
	public List<InputTask> getAssignedTasks(String worker, int limit) {
		ComplexKey startKey = ComplexKey.of(worker);
		ComplexKey endKey = ComplexKey.of(worker, ComplexKey.emptyObject());
		ViewQuery q = createQuery("assignedTasks").limit(limit).includeDocs(true).startKey(startKey).endKey(endKey);
		return db.queryView(q, type);
	}
	
	public InputTask assignTasks(List<InputTask> taskList, String worker) {
		InputTask validTask = null;
		for (InputTask task : taskList) {
			// make sure this worker doesn't input the same page twice
			String partner = db.get(InputTask.class, task.partnerId).worker;
//			System.out.println("w="+worker+"  p="+partner);
			if (partner.compareTo(worker)!=0) {
//				System.out.println("different");
				task.worker=worker;
				task.dateTimeAssigned=DateTimeStamp.gen();
				update(task);
				if (validTask==null) validTask = task;
			}
		}
//		System.out.print("task==null:  ");
//		System.out.println(validTask==null);
		return validTask;
	}
	
	
	public InputTask getTask(String worker) {
		List<InputTask> taskList = getAssignedTasks(worker, 1);
		log.info("W:"+taskList.size());
		if (!taskList.isEmpty()) return taskList.get(0);
		
		taskList = getAssignedTasks("_any", taskBlockSize);
		log.info("A:"+taskList.size());
		if (!taskList.isEmpty()) {
			InputTask task = assignTasks(taskList, worker);
			if (task!=null) return task;
		}

		taskList = getAssignedTasks("_init", taskBlockSize);
		log.info("I:"+taskList.size());
		if (!taskList.isEmpty()) {
			return buildInputTasks(taskList, worker);
		} else return null;

	}
	
	
	public InputTask buildInputTasks(List<InputTask> taskList, String worker) {
		InputTask inputTask = null;
		for (InputTask task : taskList) {
			if (inputTask==null) inputTask = buildInitialInputPair(task, worker);
			else buildInitialInputPair(task, worker);
			task.isActive=false;
			update(task);
		}
		return inputTask;
	}
	
	public InputTask buildInitialInputPair(InputTask task, String worker) {
		InputTask input_me = new InputTask();
		InputTask input_any = new InputTask();

		input_me.copySourceInfo(task);
		input_me.worker=worker;
		input_me.setId(Id.gen());
		input_me.dateTimeAssigned=DateTimeStamp.gen();
	
		input_any.copySourceInfo(task);
		input_any.worker="_any";
		input_any.setId(Id.gen());
		input_any.dateTimeAssigned=DateTimeStamp.gen();
		
		input_me.partnerId=input_any.getId();
		input_any.partnerId=input_me.getId();
		
		add(input_me);
		add(input_any);
		
		return input_me;
	}
	
	// updates the task in the db and builds a new correction task if necessary
	// call after deactivating task and setting product
	public void finalize(InputTask taskJustFinished) {
		InputTask partner;
		partner = db.get(InputTask.class, taskJustFinished.partnerId);
		
		// if my partner is not active, I expect my partner to have produced something
		if (!partner.isActive) {
			String myProduct = taskJustFinished.product;
			String partnerProduct = partner.product;
//			System.out.println("mine:  "+myProduct+"   partner:  "+partnerProduct);
			if (myProduct==null || partnerProduct==null) return;	  	//  TODO: throw exception?
			// compare my product with my partner's product
			if (myProduct.equals(partnerProduct)) {						// TODO:  better matching (ie ignore blank lines)
				// if they match, we're done - mark one of them as "finished"
				taskJustFinished.isFinal=true;  
			} else {
				// if not, build a correction task for my partner
				buildCorrectionTask(taskJustFinished, partner);
			}
		}
		update(taskJustFinished);
	}
	
	public void buildCorrectionTask(InputTask me, InputTask partner) {
		InputTask forPartnerToDo = new InputTask();
		forPartnerToDo.makeLowerPriority(partner);
		forPartnerToDo.copySourceInfo(partner);
		forPartnerToDo.worker=partner.worker;
		forPartnerToDo.taskToFixId=partner.getId();
		forPartnerToDo.dateTimeAssigned=DateTimeStamp.gen();
		forPartnerToDo.partnerId=me.getId();
		add(forPartnerToDo);
	}
	

}
