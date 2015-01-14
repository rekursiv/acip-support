package org.asianclassics.center.input.db;

import java.util.Date;

import org.ektorp.CouchDbConnector;
import org.ektorp.support.CouchDbRepositorySupport;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class CollectionRepo extends CouchDbRepositorySupport<Collection> {
	
	public CollectionRepo(CouchDbConnector db) {
		super(Collection.class, db, false);
	}


}
