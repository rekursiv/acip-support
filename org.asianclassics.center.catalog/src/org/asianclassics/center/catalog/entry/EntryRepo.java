package org.asianclassics.center.catalog.entry;

import java.util.List;

import org.asianclassics.center.catalog.entry.model.EntryModel;
import org.asianclassics.center.link.LinkManager;
import org.ektorp.ComplexKey;
import org.ektorp.ViewQuery;
import org.ektorp.ViewResult;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.View;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class EntryRepo extends CouchDbRepositorySupport<EntryModel> {

	@Inject
	public EntryRepo(LinkManager lm) {
		super(EntryModel.class, lm.getDb("catalog"), true);
	}

	@View(name="getLatestPotiIndex", map="function(doc) {emit(doc.potiIndex, doc.potiIndex)}")
	public int getLatestPotiIndex() {
		ViewQuery q = createQuery("getLatestPotiIndex").descending(true).limit(1).includeDocs(false);
		ViewResult r = db.queryView(q);
		if (r.isEmpty()) return 0;  // TODO:  throw exception??
		return r.getRows().get(0).getValueAsInt();
	}

	
	@View(name="getPotis", map="function(doc) {if (doc.sutraIndex===1) emit([doc.inputBy, doc.potiIndex], null)}")
	public List<EntryModel> getPotis(String worker, int limit) {
		ComplexKey startKey = ComplexKey.of(worker);
		ComplexKey endKey = ComplexKey.of(worker, ComplexKey.emptyObject());
		ViewQuery q = createQuery("getPotis").limit(limit).includeDocs(true).startKey(startKey).endKey(endKey);
		return db.queryView(q, type);
	}
	
	
	/*	
	@View(name="getAssignedTasks", map="function(doc) {if (doc.type === 'InputTask' && doc.active && doc.worker) emit([doc.worker, doc.taskPriority, doc.projectPriority, doc.collectionIndex, doc.volumeIndex, doc.pageIndex], null)}")
	public List<EntryModel> getAssignedTasks(String worker, int limit) {
		ComplexKey startKey = ComplexKey.of(worker);
		ComplexKey endKey = ComplexKey.of(worker, ComplexKey.emptyObject());
		ViewQuery q = createQuery("getAssignedTasks").limit(limit).includeDocs(true).startKey(startKey).endKey(endKey);
		return db.queryView(q, type);
	}
*/	
}
