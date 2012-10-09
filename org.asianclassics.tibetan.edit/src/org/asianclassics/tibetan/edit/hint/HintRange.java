package org.asianclassics.tibetan.edit.hint;

public class HintRange {
	
	public enum Hint {
		OK, MISSING, DIFFERENT, EXTRA, MARK, EOF
	}
	
	private Hint hint;
	private int begin;
	private int end;
	
	public HintRange(int begin, int end, Hint hint) {
		this.begin = begin;
		this.end = end;
		this.hint = hint;
	}
	
	public Hint getType() {
		return hint;
	}

	public int getBegin() {
		return begin;
	}

	public int getEnd() {
		return end;
	}
	
	public boolean contains(int pos) {
		if (pos>=begin&&pos<end) return true;
		else return false;
	}
	
	public int getRelativePosition(int pos) {
		if (pos>=begin&&pos<end) return 0;
		else if (pos<begin) return pos-begin;
		else return (pos-end)+1;
	}
	
	public String toString() {
		return begin+":"+end+":"+hint;
	}

}
