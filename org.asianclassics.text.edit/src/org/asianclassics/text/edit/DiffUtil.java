package org.asianclassics.text.edit;

import java.util.LinkedList;

import org.asianclassics.text.edit.diff_match_patch.Diff;
import org.asianclassics.text.edit.diff_match_patch.Operation;

public class DiffUtil {
	
	diff_match_patch dmp = new diff_match_patch();
	
	// returns how many "corrections" need to be done to make "wrk" look like "ref"
	public int getNumCorrections(String ref, String wrk) {
		LinkedList<Diff> diffs = dmp.diff_main(wrk, ref);
		dmp.diff_cleanupSemantic(diffs);
		int count = 0;
		Diff diff = null;
		Operation prevOp = null;
		for (int i=0; i<diffs.size(); ++i) {
			diff = diffs.get(i);
			if (diff.operation!=Operation.EQUAL) {
				if (diff.operation==Operation.INSERT && prevOp==Operation.DELETE) {
					// "replace" operation was already counted, so do nothing this time
				} else {
					++count;
				}
			}
			prevOp = diff.operation;
//			System.out.println(diffs.get(i).toString());
		}
		return count;
	}

}
