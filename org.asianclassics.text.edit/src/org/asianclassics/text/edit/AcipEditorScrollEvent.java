package org.asianclassics.text.edit;

public class AcipEditorScrollEvent {


	private int pos_x;
	private int pos_y;
	
	public AcipEditorScrollEvent(int pos_x, int pos_y) {
		this.pos_x = pos_x;
		this.pos_y = pos_y;
	}


	public int getX() {
		return pos_x;
	}

	public int getY() {
		return pos_y;
	}

	
}
