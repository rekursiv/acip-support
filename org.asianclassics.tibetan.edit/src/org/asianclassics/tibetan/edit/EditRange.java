package org.asianclassics.tibetan.edit;

import org.eclipse.jface.text.Position;

public class EditRange extends Position {

	
	public void setBegin(int begin) {
		offset = begin;
	}
	
	public int getBegin() {
		return offset;
	}
	
	public void setEnd(int end) {
		if (end-offset>=0) length = end-offset;
		else end = 0;
	}
	
	public int getEnd() {
		return offset+length;
	}

	public String debugStartEnd() {
		return offset+":"+(offset+length);
	}
	
}
