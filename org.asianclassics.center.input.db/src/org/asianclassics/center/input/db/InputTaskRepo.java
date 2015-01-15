
package org.asianclassics.center.input.db;


import java.util.List;

import org.ektorp.ComplexKey;
import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.View;

public class InputTaskRepo extends CouchDbRepositorySupport<InputTask> {
	
	private static final int taskBlockSize = 2;
	
	public InputTaskRepo(CouchDbConnector db) {
		super(InputTask.class, db, false);
	}

	
//	@Override
//	@GenerateView
//	public List<InputTask> getAll() {
//		return super.getAll();
//	}
	
	
//	@View(name="getAssignedTasks", map="function(doc) {if (doc.type === 'InputTask' && doc.active && doc.worker) emit([doc.worker, doc.priority], null)}")
	@View(name="getAssignedTasks", map="function(doc) {if (doc.type === 'InputTask' && doc.active && doc.worker) emit([doc.worker, doc.taskPriority, doc.projectPriority, doc.collectionId, doc.volumeIndex, doc.pageIndex], null)}")
//	@View(name="getAssignedTasks", map="function(doc) {if (doc.active && doc.worker) emit([doc.worker, doc.taskPriority, doc.projectPriority, doc.collectionIndex, doc.volumeIndex, doc.pageIndex], null)}")

	public List<InputTask> getAssignedTasks(String worker, int limit) {
		ComplexKey startKey = ComplexKey.of(worker);
		ComplexKey endKey = ComplexKey.of(worker, ComplexKey.emptyObject());
		ViewQuery q = createQuery("getAssignedTasks").limit(limit).includeDocs(true).startKey(startKey).endKey(endKey);
		return db.queryView(q, type);
	}
	
	public InputTask assignTasks(List<InputTask> taskList, String worker) {
		InputTask validTask = null;
		for (InputTask task : taskList) {
			// make sure this worker doesn't input the same page twice
			String partner = db.get(InputTask.class, task.getPartnerId()).getWorker();
//			System.out.println("w="+worker+"  p="+partner);
			if (partner.compareTo(worker)!=0) {
//				System.out.println("different");
				task.setWorker(worker);
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
			task.setActive(false);
			update(task);
		}
		return inputTask;
	}
	
	public InputTask buildInitialInputPair(InputTask task, String worker) {
		InputTask input_me = new InputTask();
		InputTask input_any = new InputTask();

		input_me.copySourceInfo(task);
		input_me.setWorker(worker);
//		input_me.setId(Id.gen());
		input_me.setDebugId();
		input_me.setDateTimeAssigned(DateTimeStamp.gen());
	
		input_any.copySourceInfo(task);
		input_any.setWorker("_any");
//		input_any.setId(Id.gen());
		input_any.setDebugId();
		input_any.setDateTimeAssigned(DateTimeStamp.gen());
		
		input_me.setPartnerId(input_any.getId());
		input_any.setPartnerId(input_me.getId());
		
		add(input_me);
		add(input_any);
		
		return input_me;
	}
	
	// updates the task in the db and builds a new correction task if necessary
	// call after deactivating task and setting product
	public void finalize(InputTask taskJustFinished) {
		InputTask partner;
		partner = db.get(InputTask.class, taskJustFinished.getPartnerId());
		
		// if my partner is not active, I expect my partner to have produced something
		if (!partner.isActive()) {
			String myProduct = taskJustFinished.getProduct();
			String partnerProduct = partner.getProduct();
//			System.out.println("mine:  "+myProduct+"   partner:  "+partnerProduct);
			if (myProduct==null || partnerProduct==null) return;	  	//  TODO: throw exception?
			// compare my product with my partner's product
			if (myProduct.equals(partnerProduct)) {						// TODO:  better matching (ie ignore blank lines)
				// if they match, we're done - mark one of them as "finished"
				taskJustFinished.setFinal(true);  
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
		forPartnerToDo.setWorker(partner.getWorker());
		forPartnerToDo.setTaskToFixId(partner.getId());
		forPartnerToDo.setDateTimeAssigned(DateTimeStamp.gen());
		forPartnerToDo.setPartnerId(me.getId());
		forPartnerToDo.setDebugId();
		add(forPartnerToDo);
	}
	
	
	
/*	
	@Override
	public void add(InputTask entity) {       //   DEBUG IDs
		entity.setDebugId();
		super.add(entity);
	}
	*/
	
	
	
	
	
	/*
	
	public void buildCorrectionPair(InputTask myPrev, InputTask partnerPrev) {
		InputTask myCur = new InputTask();
		myCur.setSourceId(myPrev.getSourceId());
		myCur.setWorker(myPrev.getWorker());
		myCur.setPriority(myPrev.getPriority());
		myCur.setMyPrevInputId(myPrev.getId());
		myCur.setId(Id.gen());
		myCur.setDateTimeAssigned(DateTimeStamp.gen());
		
		InputTask partnerCur = new InputTask();
		partnerCur.setSourceId(partnerPrev.getSourceId());
		partnerCur.setWorker(partnerPrev.getWorker());
		partnerCur.setPriority(partnerPrev.getPriority());
		partnerCur.setMyPrevInputId(partnerPrev.getId());
		partnerCur.setId(Id.gen());
		partnerCur.setDateTimeAssigned(DateTimeStamp.gen());

		myCur.setPartnerId(partnerCur.getId());
		partnerCur.setPartnerId(myCur.getId());

		add(myCur);
		add(partnerCur);
	}
	*/

}
