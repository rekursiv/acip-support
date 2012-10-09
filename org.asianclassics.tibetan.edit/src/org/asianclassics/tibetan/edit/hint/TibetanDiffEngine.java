package org.asianclassics.tibetan.edit.hint;

import org.asianclassics.tibetan.transcoder.TibetanTranscoder;

public class TibetanDiffEngine extends DiffEngine {
	
	private TibetanTranscoder xcdr;
	
	public TibetanDiffEngine(TibetanTranscoder xcdr) {
		this.xcdr = xcdr;
	}

	@Override
	public boolean testChunkMembership(char ch) {
		return xcdr.belongsToSyllable(ch);
	}
	
	
}
