package org.asianclassics.tibetan.edit.hint;

import org.asianclassics.tibetan.transcoder.TibetanTranscoder;

public class TibetanDiffDocument extends DiffDocument {

	private TibetanTranscoder xcdr;
	
	public TibetanDiffDocument(TibetanTranscoder xcdr) {
		this.xcdr = xcdr;
	}

	@Override
	protected void initDiffEngine() {
		if (diffEngine==null) diffEngine = new TibetanDiffEngine(xcdr);
	}
	
	
	
}
