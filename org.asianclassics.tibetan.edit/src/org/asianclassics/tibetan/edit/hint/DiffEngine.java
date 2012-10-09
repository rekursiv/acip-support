package org.asianclassics.tibetan.edit.hint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.asianclassics.tibetan.edit.hint.HintRange.Hint;
import org.asianclassics.tibetan.edit.hint.diff_match_patch.Diff;
import org.asianclassics.tibetan.edit.hint.diff_match_patch.Operation;


public class DiffEngine {


	diff_match_patch dmp = new diff_match_patch();
	
	private ArrayList<HintRange> hints;
	private LinkedList<Diff> diffs;
	
	private List<String> refChunks;
	private Map<String, Character> refHash;
	private StringBuilder refDigest;

	private List<String> workChunks;
	private Map<String, Character> workHash;
	private StringBuilder workDigest;
	
	private int workTextLength = 0;
	
	
	public void setReferenceText(String text) {
		refDigest = new StringBuilder();
		refHash = new HashMap<String, Character>();
		refChunks = new ArrayList<String>();
		
		breakIntoChunks(text, true);
		
//		System.out.println(refChunks.toString().replace("\n", "\\n"));
	}
	
	public void setWorkingText(String text) {
		workTextLength = text.length();
		workDigest = new StringBuilder();
		workHash = new HashMap<String, Character>();
		workChunks = new ArrayList<String>();
		
		breakIntoChunks(text, false);
		
//		System.out.println(workChunks.toString().replace("\n", "\\n"));
	}
	
	public void breakIntoChunks(String text, boolean isRef) {
		StringBuilder chunk = new StringBuilder();
		char ch;
		boolean chunkSwitch;
		boolean chunkRef = false;
		for (int i=0; i<text.length(); ++i) {
			ch = text.charAt(i);
			chunkSwitch = testChunkMembership(ch);
			if (chunkSwitch!=chunkRef||ch=='\n') {
				if (chunk.length()>0) {
					processChunk(chunk.toString(), isRef);
					chunk.setLength(0);
				}
				chunkRef=chunkSwitch;
			}
			chunk.append(ch);
		}
		if (chunk.length()>0) processChunk(chunk.toString(), isRef);
	}
	
	public void processChunk(String chunk, boolean isRef) {
		if (isRef) processRefChunk(chunk);
		else processWorkChunk(chunk);
	}
	
	private void processWorkChunk(String chunk) {
		if (refHash.containsKey(chunk)) {
			workDigest.append(refHash.get(chunk));
		} else if (workHash.containsKey(chunk)) {
			workDigest.append(workHash.get(chunk));
		} else {
			char ch = (char)((refChunks.size())+(workChunks.size()));
			workHash.put(chunk, ch);
			workDigest.append(ch);
			workChunks.add(chunk);
		}
	}

	public void processRefChunk(String chunk) {
		if (refHash.containsKey(chunk)) {
			refDigest.append(refHash.get(chunk));
		} else {
			char ch = (char)(refChunks.size());
			refHash.put(chunk, ch);
			refDigest.append(ch);
			refChunks.add(chunk);
		}
	}
	
	public boolean testChunkMembership(char ch) {
//		return Character.isLetter(ch)&&!Character.isWhitespace(ch);
		return Character.isLetter(ch);
	}
	
	
	public void diff() {
		if (refDigest==null||workDigest==null) return;
		diffs = dmp.diff_main(refDigest.toString(), workDigest.toString(), false);
///		Debug.clearDebug();
///		debugDiffs();
		buildHints();
///		debugHints();
//		debugGetHint();
///		Debug.displayDebug();
	}

	private void debugGetHint() {
///		Debug.log("debugGetHint:\n");
		int c=0;
		int pos=0;
		HintRange hr = new HintRange(0, 0, Hint.OK);
		while (hr.getType()!=Hint.EOF) {
			hr = getHint(pos);
///			Debug.log(pos+"|"+hr+"   ");
			pos = hr.getEnd();
			c++;
			if (c>10) {
///				Debug.log("OVERRUN");
				break;
			}
		}
///		Debug.log("\n\n");
	}

	public HintRange getHint(int pos) {
		if (pos>=workTextLength) return new HintRange(0, 0, Hint.EOF);
		if (hints==null || hints.isEmpty()) return new HintRange(0, workTextLength, Hint.OK);
		
//		System.out.println("# pos="+pos);
//		int lastHintIndex = hints.size()-1;
		int curRp = 0;
		for (HintRange hint : hints) {
			curRp = hint.getRelativePosition(pos);
			if (curRp==0) {
//				System.out.println("# curRp==0");
				return hint;
			} else if (curRp<0) {
//				System.out.println("# curRp<0");
				return new HintRange(pos, pos-curRp, Hint.OK);
			}
		}
//		System.out.println("# end of loop");
		return new HintRange(pos, workTextLength, Hint.OK);
	}
	
	public void buildHints() {
		hints = new ArrayList<HintRange>();
		if (diffs==null) return;
		if (diffs.isEmpty()) return;
		
		int pos = 0;
		int max = diffs.size();
		boolean addOffset = false;
		for (int i=0; i<max; ++i) {
			addOffset = false;
			if (diffs.get(i).operation==Operation.DELETE) {
				if (i+1<max) {
					if (diffs.get(i+1).operation==Operation.EQUAL) {
						if (i>0) {
							hints.add(new HintRange(pos-1, pos, Hint.MISSING));
						}
						hints.add(new HintRange(pos, pos+1, Hint.MISSING));
						++i;
						addOffset = true;
					} else if (diffs.get(i+1).operation==Operation.INSERT) {
//						int len = diffs.get(i+1).text.length();
						int len = getDiffLength(diffs.get(i+1));
						hints.add(new HintRange(pos, pos+len, Hint.DIFFERENT));
						++i;
						addOffset = true;
					}
				} else if (i>0) {
					hints.add(new HintRange(pos-1, pos, Hint.MISSING));
				}

			} else {
				if (diffs.get(i).operation==Operation.INSERT) {
//					int len = diffs.get(i).text.length();
					int len = getDiffLength(diffs.get(i));
					hints.add(new HintRange(pos, pos+len, Hint.EXTRA));
				}
				addOffset = true;
			}
			
			if (addOffset) {
//				pos += diffs.get(i).text.length();
				pos += getDiffLength(diffs.get(i));
			}
		}
		
	}
//	corhint_missing		:	DELETE*|EQUAL	: blue bg  (both sides)
//	corhint_different	:	DELETE*|INSERT	: red bg
//	corhint_extra		:	INSERT*|EQUAL	: green bg
	
	
	public int getDiffLength(Diff diff) {
		int len = 0;
		for (int i=0; i<diff.text.length(); ++i) {
			len+=getChunkLength((int)diff.text.charAt(i));
		}
		return len;
	}
	
	public int getChunkLength(int index) {
		if (index<0) return 0;
		else if (index<refChunks.size()) {
			return refChunks.get(index).length();
		} else if (index<refChunks.size()+workChunks.size()) {
			return workChunks.get(index-refChunks.size()).length();
		} else return 0;
	}
	
	
	
	public String getDiffText(Diff diff) {
		StringBuilder text = new StringBuilder();
		for (int i=0; i<diff.text.length(); ++i) {
			text.append(getChunkText((int)diff.text.charAt(i)));
		}
		return text.toString().replace("\n", "\\n");
	}
	
	public String getChunkText(int index) {
		if (index<0) return "";
		else if (index<refChunks.size()) {
			return refChunks.get(index);
		} else if (index<refChunks.size()+workChunks.size()) {
			return workChunks.get(index-refChunks.size());
		} else return "";
	}
	
	
	
	public void debugLengths() {
		System.out.println("");
		for (int i=-1; i<12; ++i) {
			System.out.print("   "+getChunkLength(i));
		}
		System.out.println("");
	}
	
	public void debugAllText() {
		System.out.println("");
		for (int i=-1; i<12; ++i) {
			System.out.print(".'"+getChunkText(i)+"'");
		}
		System.out.println("");
	}
	
	public void debugDigest(String digest) {
		for (int i=0; i<digest.length(); ++i) {
			System.out.print(".'"+getChunkText(digest.charAt(i))+"'");
		}
	}
	
	
	public void debugDiffs() {
		if (diffs==null) return;
///		Debug.log(diffs.size()+" diffs:\n");
		for (Diff diff : diffs) {
///			Debug.log(diffToString(diff)+"    ");
		}
///		Debug.log("\n\n");
	}
	
	public String diffToString(Diff diff) {
		return diff.operation+":"+getDiffLength(diff)+":'"+getDiffText(diff)+"'";
	}

	public void debugHints() {
		if (hints==null) return;
///		Debug.log(hints.size()+" hints:\n");
		for (HintRange hint : hints) {
///			Debug.log(hint.toString()+"   ");
		}
///		Debug.log("\n\n");
	}
	
	
	public void debug() {
		debugLengths();
		debugAllText();
		System.out.println("\n\nrefDigest:");
		debugDigest(refDigest.toString());
		System.out.println("\n\nworkDigest:");
		debugDigest(workDigest.toString());
	}
	
}







