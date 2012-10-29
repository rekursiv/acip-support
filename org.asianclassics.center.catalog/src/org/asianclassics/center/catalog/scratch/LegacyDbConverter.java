package org.asianclassics.center.catalog.scratch;

import java.util.List;

import org.asianclassics.center.catalog.entry.model.EntryModel;
import org.asianclassics.database.CustomCouchDbConnector;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.node.ObjectNode;
import org.ektorp.CouchDbInstance;
import org.ektorp.DbPath;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbConnector;
import org.ektorp.impl.StdCouchDbInstance;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class LegacyDbConverter {

	private static final String dbOutputName = "acip-center-test-catalog";   ////  CAREFUL - this entire DB will be DELETED and REPLACED
	
	private static LegacyDbConverter instance;
	private CustomCouchDbConnector dbInput;
	private StdCouchDbConnector dbOutput;
	private EntryModel entry;
	ObjectMapper mapper;

/*	
	public static void main(String[] args) {

		instance = new LegacyDbConverter();
		
		try {
//			instance.convertDb();
//			instance.test();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	*/
	
	
	public void test() throws Exception {
		
	}
	
	public void timeTest() throws Exception {
//		System.getProperties().setProperty("user.timezone", "UTC");
		System.out.println(DateTimeZone.getDefault().getID());
		System.out.println(new DateTime().toString());
	}
	
	public void convertDb() throws Exception {
		
		System.out.println("Setting up DBs...");
		mapper = new ObjectMapper();
		mapper.configure(SerializationConfig.Feature.WRITE_DATES_AS_TIMESTAMPS, false);
		
		HttpClient httpClient = new StdHttpClient.Builder().build();
		CouchDbInstance couch = new StdCouchDbInstance(httpClient);
		dbInput = new CustomCouchDbConnector("acip-nlm-catalog", couch);
		
		if (couch.checkIfDbExists(new DbPath(dbOutputName))) couch.deleteDatabase(dbOutputName);
		dbOutput = new CustomCouchDbConnector(dbOutputName, couch);
		dbOutput.createDatabaseIfNotExists();
		
		List<String> ids = dbInput.getAllDocIds();	
		System.out.println("Input DB contains "+ids.size()+" documents.");
		System.out.println("Converting...");
		for (String id : ids) {
			convertDoc(id);
		}
		
//		convertDoc("M0057416-003");
		
		httpClient.shutdown();
		
		
		System.out.println("Done.");
	}
	

	public void convertDoc(String id) throws Exception {
		if (id.startsWith("_")) return;  // don't try to convert design docs, etc
		System.out.println("  "+id);
		JsonNode doc = dbInput.get(JsonNode.class, id);
//		System.out.println("   > "+doc.toString());
		ObjectNode root = (ObjectNode) doc;
		String oldId = root.remove("_id").asText();
		root.remove("_rev");
		if (root.has("submitDate")) {
			long submitDate = root.remove("submitDate").asLong();
			root.put("dateTimeFirstSubmitted", submitDate);
		}
		root.remove("appVersion");
		entry = mapper.readValue(root, EntryModel.class);
		convertId(oldId);
		entry.inputBy = entry.inputBy.toLowerCase();
		
		
		dbOutput.create(entry);
		
//		debug();
	}

	private void convertId(String id) {

		int dashPos = id.indexOf('-');
		String potiStr = id.substring(1, dashPos);
		String sutraStr = id.substring(dashPos+1);
		
		entry.potiIndex = Integer.parseInt(potiStr);
		entry.sutraIndex = Integer.parseInt(sutraStr);

	}

	
	private void debug() throws Exception {
		System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(entry));
	}



	

}
