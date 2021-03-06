package org.acipview;

import java.util.List;

import org.ektorp.ComplexKey;
import org.ektorp.CouchDbConnector;
import org.ektorp.ViewQuery;
import org.ektorp.ViewResult;
import org.ektorp.ViewResult.Row;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.View;



public class EntryRepo extends CouchDbRepositorySupport<EntryModel> {

	public EntryRepo(CouchDbConnector db) {
		super(EntryModel.class, db,  true);
		initStandardDesignDocument();
	}

	@View(name="getLatestPotiIndex", map="function(doc) {emit(doc.potiIndex, doc.potiIndex)}")
	public int getLatestPotiIndex() {
		ViewQuery q = createQuery("getLatestPotiIndex").descending(true).limit(1).includeDocs(false);
		ViewResult r = db.queryView(q);
		if (r.isEmpty()) return 0;  // TODO:  throw exception??
		return r.getRows().get(0).getValueAsInt();
	}
	
	@View(name="getPotis", map="function(doc) {if (doc.sutraIndex===1) emit([doc.inputBy, doc.potiIndex], doc.potiIndex)}")
	public List<Row> getPotis(String worker, int limit) {
		ComplexKey endKey = ComplexKey.of(worker);
		ComplexKey startKey = ComplexKey.of(worker, ComplexKey.emptyObject());
		ViewQuery q = createQuery("getPotis").descending(true).limit(limit).includeDocs(false).startKey(startKey).endKey(endKey);
		return db.queryView(q).getRows();
	}
	
	@View(name="getSutras", map="function(doc) {emit([doc.potiIndex, doc.sutraIndex], {_id:doc._id, isValid:doc.isValid, dateTimeFirstSubmitted:doc.dateTimeFirstSubmitted, sutraIndex:doc.sutraIndex, titleTibetan:doc.titleTibetan})}")
	public List<Row> getSutras(int potiIndex, int limit) {
		ComplexKey endKey = ComplexKey.of(potiIndex);
		ComplexKey startKey = ComplexKey.of(potiIndex, ComplexKey.emptyObject());
		ViewQuery q = createQuery("getSutras").descending(true).limit(limit).includeDocs(false).startKey(startKey).endKey(endKey);
		return db.queryView(q).getRows();
	}
	
	
	@View(name="getCountByDay", map="function(doc) {emit(doc.dateTimeFirstSubmitted.substr(0, 10), 1);}", reduce="_sum")
	public List<Row> getCountByDay(String startKey, String endKey) {
		ViewQuery q = createQuery("getCountByDay").includeDocs(false).startKey(startKey).endKey(endKey).group(true);
		return db.queryView(q).getRows();
	}

}
