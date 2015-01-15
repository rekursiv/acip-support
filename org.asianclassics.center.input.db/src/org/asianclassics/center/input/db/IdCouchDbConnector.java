package org.asianclassics.center.input.db;

import org.ektorp.CouchDbInstance;
import org.ektorp.impl.StdCouchDbConnector;
import org.ektorp.util.Documents;

public class IdCouchDbConnector extends StdCouchDbConnector {

	public IdCouchDbConnector(String databaseName, CouchDbInstance dbInstance) {
		super(databaseName, dbInstance);
	}
	
    @Override
    public void create(final Object o) {
    	if (Documents.getId(o)==null) Documents.setId(o, Id.gen());
    	super.create(o);
    }

}
