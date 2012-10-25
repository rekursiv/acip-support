package org.asianclassics.center.catalog.entry;

import org.asianclassics.center.catalog.entry.model.EntryModel;
import org.ektorp.CouchDbConnector;
import org.ektorp.support.CouchDbRepositorySupport;


public class EntryRepo extends CouchDbRepositorySupport<EntryModel> {


	public EntryRepo(CouchDbConnector db) {
		super(EntryModel.class, db, false);
	}
		

}
