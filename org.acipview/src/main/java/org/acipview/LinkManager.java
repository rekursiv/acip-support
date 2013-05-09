package org.acipview;

import java.util.List;

import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.ViewQuery;
import org.ektorp.ViewResult.Row;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbConnector;
import org.ektorp.impl.StdCouchDbInstance;
import org.joda.time.DateTime;

public class LinkManager {

	CouchDbConnector db = null;
	EntryRepo repo = null;
	
	public LinkManager() {
		CouchDbInstance dbInstance = new StdCouchDbInstance(new StdHttpClient.Builder().build());
		db = new StdCouchDbConnector("acip-center-nlm-catalog", dbInstance);
		repo = new EntryRepo(db);
	}
	
	public void destroy() {
		if (db!=null) {
			if (db.getConnection()!=null) db.getConnection().shutdown();
		}
	}
	
	public List<Row> getDates() {
		String startKey = "2013-01-01";
		String endKey = new DateTime().toString("yyyy-MM-dd");  //  today
		return repo.getCountByDay(startKey, endKey);
	}
	
	
	public void test() {
//		List<Row> rows = repo.getCountByDay(startKey, endKey);
//		System.out.println(rows.toString());
		DateTime dt = new DateTime();
		System.out.println(dt.toString("yyyy-MM-dd"));
	}
	
}
