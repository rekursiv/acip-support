package org.asianclassics.tibetan.edit;

import org.eclipse.jface.text.AbstractLineTracker;

public class NewlineTracker extends AbstractLineTracker {

	protected static class MyDelimiterInfo extends DelimiterInfo {}  // gets around lack of public ctor in AbstractLineTracker.DelimiterInfo
	
	/** A predefined delimiter information which is always reused as return value */
	private MyDelimiterInfo fDelimiterInfo = new MyDelimiterInfo();
	
	
	@Override
	public String[] getLegalLineDelimiters() {
		return new String [] { "\n" };
	}

	/*
	 * @see org.eclipse.jface.text.AbstractLineTracker#nextDelimiterInfo(java.lang.String, int)
	 */
	@Override
	protected DelimiterInfo nextDelimiterInfo(String text, int offset) {
		char ch;
		int length= text.length();
		for (int i= offset; i < length; i++) {
			ch= text.charAt(i);
			if (ch == '\n') {
				fDelimiterInfo.delimiter= "\n";
				fDelimiterInfo.delimiterIndex= i;
				fDelimiterInfo.delimiterLength= 1;
				return fDelimiterInfo;
			}
		}
		return null;
	}

}
