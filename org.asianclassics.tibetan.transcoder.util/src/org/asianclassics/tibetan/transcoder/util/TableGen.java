package org.asianclassics.tibetan.transcoder.util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

public class TableGen {
	
	final String inputFilePath = "C:/UnicDocP/_current/";
	final String outputFilePath = "C:/projects/eclipse_workspace/org.asianclassics.tibetan.transcoder/";
	
	private TreeMap<String, String> map = new TreeMap<String, String>();
	
	public void generate() throws Exception {
		readInput();
		writeTable("syllable", map);
	}
	
	public void readInput() throws Exception {
	    BufferedReader acipReader = new BufferedReader(new InputStreamReader(new FileInputStream(inputFilePath+"tibsyl.act"), "UTF-8"));
	    BufferedReader tibReader = new BufferedReader(new InputStreamReader(new FileInputStream(inputFilePath+"tibsyl.utx"), "UTF-8"));	
	    while (acipReader.ready()&&tibReader.ready()) {
	    	String acip = acipReader.readLine();
	    	if (acip==null) break;
//	    	System.out.println(acip);
	    	String tib = tibReader.readLine();
	    	if (tib==null) break;
	    	tib = tib.substring(0, tib.length()-1);  // cut off trailing "tsek"
	    	map.put(acip, tib);
	    }
	    acipReader.close();
	    tibReader.close();
	    System.out.println("read "+map.size()+" pairs");
	}
	
	public void writeTable(String fileNamePrefix, Map<String, String> map) throws Exception {
		
		BufferedWriter oswTable = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFilePath+fileNamePrefix+"Table.txt"), "UTF-8"));

		oswTable.write("syllables:\t"+map.size()+'\n');
		Iterator<Map.Entry<String, String>> i = map.entrySet().iterator();
		while (i.hasNext()) {
			Entry<String, String> entry = i.next();
			oswTable.write(entry.getKey()+'\t');
			oswTable.write(entry.getValue()+'\n');
		}
		oswTable.close();
		
		System.out.println("table saved.");
	}
}
