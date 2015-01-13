package org.asianclassics.center.input;

public class ScanAlignModeEvent {

	private boolean isEnabled;
	
	public ScanAlignModeEvent(boolean isEnabled) {
		this.isEnabled = isEnabled;
	}
	
	public boolean isEnabled() {
		return isEnabled;
	}

}
