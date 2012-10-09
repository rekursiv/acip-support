package org.asianclassics.tibetan.transcoder;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class TibetanTranscoder {

	public static final int UNDEFINED = 0;
	public static final int ROMAN = 1;
	public static final int TIBETAN = 2;
	public static final int SYLLABLE = 3;
	public static final int SEPARATOR = 4;
	
	private HashMap<String, String> romanKeyedSyllableMap = null;
	private HashMap<String, String> romanKeyedSeparatorMap = null;
	private HashSet<Character> romanSyllableCharSet = null;
	
	private HashMap<String, String> tibetanKeyedSyllableMap = null;
	private HashMap<String, String> tibetanKeyedSeparatorMap = null;
	private HashSet<Character> tibetanSyllableCharSet = null;
	


	
	
	
	public TibetanTranscoder() {
		init();
	}
	
	private void init() {
		try {
			readInput(SYLLABLE);
			readInput(SEPARATOR);
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		buildCharSets();

//		testMap(romanKeyedSyllableMap);
	}
	
	private void readInput(int type) throws Exception {
		HashMap<String, String> romanKeyedMap = null;
		HashMap<String, String> tibetanKeyedMap = null;
		String typeStr;
		if (type==SEPARATOR) typeStr = "separator";
		else if (type==SYLLABLE) typeStr = "syllable";
		else throw new Exception("Invalid input type");

		InputStream is = getClass().getResourceAsStream("/"+typeStr+"Table.txt");
		if (is==null) {
			throw new Exception("Resource file not found.");
		}
	    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
	    int lineNum = 0;
	    while (reader.ready()) {
	    	String line = reader.readLine();
	    	if (line==null) break;
//	    	System.out.println(line);
	    	String [] tokens = line.split("\t");
	    	if (tokens.length!=2) throw new Exception("bad input file: syntax error on line # "+lineNum);
	    	
	    	if (lineNum==0) {
	    		if (!tokens[0].equals(typeStr+"s:")) {
	    			throw new Exception("bad input file: can't read table type");
	    		}
	    		int tableSize = 0;
	    		try {
	    			 tableSize = Integer.parseInt(tokens[1]);
	    		} catch (NumberFormatException e) {
	    			throw new Exception("bad input file: can't read table length");
	    		}
	//    		System.out.println("table size: "+tableSize);
	    		romanKeyedMap = new HashMap<String, String>(tableSize*2);
	    		tibetanKeyedMap = new HashMap<String, String>(tableSize*2);
	    	} else {
	    		romanKeyedMap.put(tokens[0], tokens[1]);
	    		tibetanKeyedMap.put(tokens[1], tokens[0]);
	    	}
	    	lineNum++;
	    }
	    reader.close();
		if (type==SYLLABLE) {
			romanKeyedSyllableMap = romanKeyedMap;
			tibetanKeyedSyllableMap = tibetanKeyedMap;
		} else if (type==SEPARATOR) {
			romanKeyedSeparatorMap = romanKeyedMap;
			tibetanKeyedSeparatorMap = tibetanKeyedMap;
		} 
	}
	
	private void buildCharSets() {
		romanSyllableCharSet = new HashSet<Character>();
		tibetanSyllableCharSet = new HashSet<Character>();
		
		Iterator<Map.Entry<String, String>> itr = romanKeyedSyllableMap.entrySet().iterator();
		while (itr.hasNext()) {
			Entry<String, String> entry = itr.next();

			String roman = entry.getKey();
			for (int i=0; i<roman.length(); i++) {
				romanSyllableCharSet.add(roman.charAt(i));
			}

			String tibetan = entry.getValue();
			for (int i=0; i<tibetan.length(); i++) {
				tibetanSyllableCharSet.add(tibetan.charAt(i));
			}
		}
	}

	public boolean belongsToSyllable(char ch) {
		if (ch>=0x0F00&&ch<=0x0FFF) return belongsToSyllable(ch, TIBETAN);
		else return belongsToSyllable(ch, ROMAN);
	}
	
	public boolean belongsToSyllable(char ch, int sourceType) {
		if (sourceType==TIBETAN) return tibetanSyllableCharSet.contains(ch);
		else return romanSyllableCharSet.contains(ch);
	}
	
		//  TODO:  handle special case:   NGA followed by SHAY needs TSEK inserted betwixt
	public String transcode(String input, int sourceType) {
		HashMap<String, String> sourceKeyedSyllableMap = null;
		HashMap<String, String> sourceKeyedSeparatorMap = null;	
		HashSet<Character> sourceSyllableCharSet = null;
		if (sourceType==ROMAN) {
//			System.out.println("=== ROMAN ===");
			sourceKeyedSyllableMap = romanKeyedSyllableMap;
			sourceKeyedSeparatorMap = romanKeyedSeparatorMap;	
			sourceSyllableCharSet = romanSyllableCharSet;
		} else if (sourceType==TIBETAN) {
//			System.out.println("=== TIBETAN ===");
			sourceKeyedSyllableMap = tibetanKeyedSyllableMap;
			sourceKeyedSeparatorMap = tibetanKeyedSeparatorMap;	
			sourceSyllableCharSet = tibetanSyllableCharSet;
		} else {
			// TODO:  throw exception??
			System.out.println("transcode ERROR:  invalid source type");
			return "";
		}
		
		StringBuilder curSyllable = new StringBuilder();
		StringBuilder curSeparator = new StringBuilder();
		StringBuilder output = new StringBuilder();
		
		char curChar;
		boolean enteredSeparator = false;
		boolean enteredSyllable = false;
		boolean madeTransition = false;
		
		for (int i=0; i<=input.length(); i++) {
//			System.out.println("++  i="+i);
			madeTransition = false;
			
			if (i==input.length()) {  // last pass
				madeTransition = true;
				enteredSyllable = true;
				enteredSeparator = true;
			} else {
				curChar = input.charAt(i);
				if (sourceSyllableCharSet.contains(curChar)) {
					curSyllable.append(curChar);
					if (enteredSeparator) {
						madeTransition = true;
						enteredSeparator = false;
					}
					enteredSyllable = true;
				} else {
					curSeparator.append(curChar);
					if (enteredSyllable) {
						madeTransition = true;
						enteredSyllable = false;
					}
					enteredSeparator = true;
				}
			}
			
//			System.out.println("mt="+madeTransition+"   entSyl="+enteredSyllable+"   entSep="+enteredSeparator);
			
			if (madeTransition&&enteredSeparator&&curSyllable.length()>0) {
//				System.out.println("-syl-");
//				System.out.println("'"+curSyllable.toString()+"'");
				if (sourceKeyedSyllableMap.containsKey(curSyllable.toString())) {
					output.append(sourceKeyedSyllableMap.get(curSyllable.toString()));
				} else {   //  unconvertable
					output.append(curSyllable);   
				}
				curSyllable.setLength(0);
//				System.out.println("^syl^");
			}
			
			
			if (madeTransition&&enteredSyllable&&curSeparator.length()>0) {				
//				System.out.println("-sep-");
				int len = curSeparator.length();
				int start = 0;
				int end = len;  //  TODO:  optimization idea:  set to longest separator string (no need to compare longer strings)
				while (start<len) {
					while (end>start) {
//						System.out.println(start+" "+end+"   '"+curSeparator.substring(start, end)+"'");					
						if (sourceKeyedSeparatorMap.containsKey(curSeparator.substring(start, end))) {
							output.append(sourceKeyedSeparatorMap.get(curSeparator.substring(start, end)));
							start+=(end-start)-1;
							break;
						} else if ((end-start)==1){   //  unconvertable
							// TODO:  optimization idea:  parse out newlines
							output.append(curSeparator.substring(start, end));
						}
						end--;
					}
					start++;
					end = len;
//					System.out.println("^sep^");
				}
				curSeparator.setLength(0);
			}
		}
		return output.toString();
	}
	
	
	
	//////////////////////////////////////////////////////////////////////////////////////   TEST    //////////////
	
	private void buildTestSepMap() {
		romanKeyedSeparatorMap = new HashMap<String, String>();
		romanKeyedSeparatorMap.put("12345", "abcde");
		romanKeyedSeparatorMap.put("#", "༄༅༅");
		romanKeyedSeparatorMap.put(", ", "། ");
		romanKeyedSeparatorMap.put(" ", "་");
		romanKeyedSeparatorMap.put(",", "།");
	}
	
	
	private void testMap(Map<String, String> map) {
		if (map==null) {
			System.out.println("in testMap(): map==null");
			return;
		}
		Iterator<Map.Entry<String, String>> itr = map.entrySet().iterator();
		while (itr.hasNext()) {
			Entry<String, String> entry = itr.next();
			System.out.println("|"+entry.getKey()+"|"+entry.getValue()+"|");
		}
	}

}
