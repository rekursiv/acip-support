package org.asianclassics.text.edit;

import java.util.LinkedList;

import org.asianclassics.text.edit.diff_match_patch.Diff;

public class DiffUtil {
	
	
	public void test(String ref, String wrk) {
		diff_match_patch dmp = new diff_match_patch();
		LinkedList<Diff> diffs = dmp.diff_main(ref, wrk);
		dmp.diff_cleanupSemantic(diffs);
		for (Diff diff : diffs) {
			System.out.println(diff.toString());
		}
		// TODO:  count non-equal diffs???
	}

}
