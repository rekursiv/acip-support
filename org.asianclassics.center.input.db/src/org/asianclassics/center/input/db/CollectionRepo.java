package org.asianclassics.center.input.db;

import org.ektorp.CouchDbConnector;
import org.ektorp.support.CouchDbRepositorySupport;

public class CollectionRepo extends CouchDbRepositorySupport<Collection> {
	
	public CollectionRepo(CouchDbConnector db) {
		super(Collection.class, db, false);
	}


}
