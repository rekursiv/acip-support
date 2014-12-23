package org.asianclassics.text.edit;

public class AcipEditorCaretMoveEvent {
	
	private int line;
	private int offset;
	
	public AcipEditorCaretMoveEvent(int line, int offset) {
		this.line = line;
		this.offset = offset;
	}
	
	public int getLine() {
		return line;
	}
	
	public int getOffset() {
		return offset;
	}
	

}
