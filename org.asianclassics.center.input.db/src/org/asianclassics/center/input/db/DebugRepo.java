package org.asianclassics.center.input.db;

import org.ektorp.CouchDbConnector;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.View;

class Debug {
	
}

@View(name="all", map="classpath:Debug_map_all.js")
public class DebugRepo extends CouchDbRepositorySupport<Debug> {
	
	public DebugRepo(CouchDbConnector db) {
		super(Debug.class, db, false);
	}


}
