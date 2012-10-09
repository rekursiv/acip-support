package org.asianclassics.tibetan.edit;

import java.util.Arrays;

import org.asianclassics.tibetan.transcoder.TibetanTranscoder;
import org.eclipse.jface.text.formatter.IFormattingStrategy;

public class TranscodingStrategy implements IFormattingStrategy {
	
	private TibetanTranscoder xcdr;
	private int sourceType;
	
	public TranscodingStrategy(TibetanTranscoder xcdr, int sourceType) {
		this.xcdr = xcdr;
		this.sourceType = sourceType;
	}
	
	@Override
	public String format(String content, boolean isLineStart, String indentation, int[] positions) {
		
//		StringBuilder debug = new StringBuilder("@ ");
//		if (sourceType==TibetanTranscoder.ROMAN) debug.append("roman    ");
//		else debug.append("tibetan  ");
//		debug.append(Arrays.toString(positions));

		String xcoded = xcdr.transcode(content, sourceType);
//		debug.append("       '"+content+"' > '"+xcoded+"'");
		
		for (int i=0; i<positions.length; ++i) {
			if (positions[i]>0) positions[i]=xcoded.length();
		}
		
//		System.out.println(debug.toString());
		return xcoded;
	}

	@Override
	public void formatterStarts(String initialIndentation) {
//		System.out.println("= start");
	}

	@Override
	public void formatterStops() {
//		System.out.println("= stop");
	}


}
