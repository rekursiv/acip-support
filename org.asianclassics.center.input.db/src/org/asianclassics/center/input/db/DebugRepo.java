package org.asianclassics.center.input.db;

import org.ektorp.CouchDbConnector;
import org.ektorp.support.CouchDbRepositorySupport;
import org.ektorp.support.View;
import org.ektorp.support.Views;

class Debug {
	
}

@Views({
	@View(name="all", map="classpath:Debug_map_all.js"),
	@View(name="pg1", map="classpath:Debug_map_pg1.js")
})
public class DebugRepo extends CouchDbRepositorySupport<Debug> {
	
	public DebugRepo(CouchDbConnector db) {
		super(Debug.class, db, false);
	}


}
