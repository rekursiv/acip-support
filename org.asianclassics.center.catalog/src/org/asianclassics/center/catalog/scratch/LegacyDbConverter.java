package org.asianclassics.center.catalog.scratch;

import java.util.List;

import org.asianclassics.center.catalog.entry.model.EntryModel;
import org.asianclassics.database.CustomCouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.DbPath;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbConnector;
import org.ektorp.impl.StdCouchDbInstance;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class LegacyDbConverter {

	private static final String dbOutputName = "acip-center-nlm_converted-catalog";   ////  CAREFUL - this entire DB will be DELETED and REPLACED
	
	private static LegacyDbConverter instance;
	private CustomCouchDbConnector dbInput;
	private StdCouchDbConnector dbOutput;
	private EntryModel entry;
	private ObjectMapper mapper;
	private JsonNode docInput;


	public static void main(String[] args) {
		instance = new LegacyDbConverter();
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
		dbInput = new CustomCouchDbConnector("acip-nlm-catalog", couch);
		
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
		String oldId = root.remove("_id").asText();
		root.remove("_rev");
		if (root.has("submitDate")) {
			long submitDate = root.remove("submitDate").asLong();
			root.put("dateTimeFirstSubmitted", submitDate);
		}
		root.remove("appVersion");
		if (root.has("extraLanguage")) {
			String out;
			JsonNode el = root.remove("extraLanguage");
			if (el.has("other")) {
				out = el.get("other").asText();
			} else {
				String script = el.get("script").asText();
				String coverage = el.get("coverage").asText();
				
				if (script.equals("lanycha")&&coverage.equals("titlePage")) out = "Lanycha script, Title page only";
				else if (script.equals("lanycha")&&coverage.equals("all")) out = "Lanycha script, Whole sutra";
				else if (script.equals("mongolian")&&coverage.equals("titlePage")) out = "Mongolian script, Title page only";
				else {
					System.out.println("!!! new extra language found:  "+script+":"+coverage);
					debug(el);
					throw new Exception();
				}
			}
			root.put("extraLanguage", out);
		}
		if (root.has("paperColor")) {
			JsonNode pc = root.remove("paperColor");
			String paper = pc.get("paper").asText();
			String edge = pc.get("edge").asText();
			String out;
			if (paper.equals("white")&&edge.equals("white")) out = "White paper, White edge";
			else if (paper.equals("white")&&edge.equals("yellow")) out = "White paper, Yellow edge";
			else if (paper.equals("white")&&edge.equals("brown")) out = "White paper, Brown edge";
			else if (paper.equals("white")&&edge.equals("red")) out = "White paper, Red edge";
			else {
				System.out.println("!!! new paper color found:  "+paper+":"+edge);
				debug(pc);
				throw new Exception();
			}
			root.put("paperColor", out);
		}
		if (root.has("format")&&root.get("format").isObject()) {
			ObjectNode format = (ObjectNode) root.get("format");
			if (format.has("other")) {
				JsonNode other = format.remove("other");
				root.remove("format");
				root.put("format", other.asText());
			}
		}

		entry = mapper.readValue(root.toString(), EntryModel.class);   //  NOTE:  updated for newer version of Jackson, NOT TESTED
		convertId(oldId);
		entry.isValid = true;
		entry.dateTimeLastEdited = new DateTime();
		entry.format = titleCase(entry.format);
		entry.inkColor = titleCase(entry.inkColor);
		entry.paperGrade = titleCase(entry.paperGrade);
		entry.readability = titleCase(entry.readability);
				
		dbOutput.create(entry);
//		debug(entry);
	}

	private String titleCase(String word) {
		if (word==null||word.isEmpty()) return word;
		return Character.toUpperCase(word.charAt(0)) + word.substring(1);
	}


	private void convertId(String id) {

		int dashPos = id.indexOf('-');
		String potiStr = id.substring(1, dashPos);
		String sutraStr = id.substring(dashPos+1);
		
		entry.potiIndex = Integer.parseInt(potiStr);
		entry.sutraIndex = Integer.parseInt(sutraStr);

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
