package org.asianclassics.center.catalog.scratch;

import java.util.List;

import org.asianclassics.database.CustomCouchDbConnector;
import org.ektorp.AttachmentInputStream;
import org.ektorp.CouchDbInstance;
import org.ektorp.DbPath;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbConnector;
import org.ektorp.impl.StdCouchDbInstance;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class LegacyStampDbConverter {

	private static final String dbOutputName = "acip-center-nlm_converted-stamps";   ////  CAREFUL - this entire DB will be DELETED and REPLACED
	
	private static LegacyStampDbConverter instance;
	private CustomCouchDbConnector dbInput;
	private StdCouchDbConnector dbOutput;
	private ObjectMapper mapper;
	private JsonNode docInput;


	public static void main(String[] args) {
		instance = new LegacyStampDbConverter();
//		instance.runConversion();
	}

	public void runConversion() {
		try {
			convertDb();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				debug(docInput);
			} catch (Exception e1) {
			}
		}
	}
	
	
	public void convertDb() throws Exception {
		
		System.out.println("Setting up DBs...");
		mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		
		HttpClient httpClient = new StdHttpClient.Builder().build();
		CouchDbInstance couch = new StdCouchDbInstance(httpClient);
		dbInput = new CustomCouchDbConnector("acip-nlm-catalog-stamps", couch);
		
		if (couch.checkIfDbExists(new DbPath(dbOutputName))) couch.deleteDatabase(dbOutputName);
		dbOutput = new CustomCouchDbConnector(dbOutputName, couch);
		dbOutput.createDatabaseIfNotExists();
		
		List<String> ids = dbInput.getAllDocIds();	
		System.out.println("Input DB contains "+ids.size()+" documents.");
		System.out.println("Converting...");
		for (String id : ids) {
			convertDoc(id);
//			break;
		}
		
		httpClient.shutdown();
		
		System.out.println("Done.");
	}
	


	public void convertDoc(String id) throws Exception {
		if (id.startsWith("_")) return;  // don't try to convert design docs, etc
		System.out.println("  "+id);
		docInput = dbInput.get(JsonNode.class, id);
		ObjectNode root = (ObjectNode) docInput;
		root.remove("_rev");
		root.remove("_id");
		JsonNode aNode = root.remove("_attachments");
		String oldKey = aNode.fieldNames().next();
		dbOutput.create(root);
		String newId = root.get("_id").asText();
		String newRev = root.get("_rev").asText();
		AttachmentInputStream dataIn = dbInput.getAttachment(id, oldKey);
		AttachmentInputStream dataOut = new AttachmentInputStream("thumb.jpg", dataIn, dataIn.getContentType());
		dbOutput.createAttachment(newId, newRev, dataOut);
		dataIn.close();
		dataOut.close();
		
	}



	
	private void debug(Object obj) throws Exception {
		if (obj==null) {
			System.out.println("!!!   NULL");
			return;
		}
		System.out.println("--------------");
		System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj));
		System.out.println("^^^^^^^^^^^^^^");
	}
	



	

}
