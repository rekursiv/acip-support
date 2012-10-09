package org.asianclassics.tibetan.edit.hint;

import org.asianclassics.tibetan.edit.NewlineTracker;
import org.asianclassics.tibetan.edit.hint.HintRange.Hint;
import org.eclipse.jface.text.AbstractDocument;
import org.eclipse.jface.text.CopyOnWriteTextStore;
import org.eclipse.jface.text.GapTextStore;


public class DiffDocument extends AbstractDocument {

	protected DiffEngine diffEngine;
	
	/**
	 * Creates a new empty document.
	 */
	public DiffDocument() {
		super();
		setTextStore(new CopyOnWriteTextStore(new GapTextStore()));
		setLineTracker(new NewlineTracker());
		completeInitialization();
	}

	/**
	 * Creates a new document with the given initial content.
	 *
	 * @param initialContent the document's initial content
	 *
	public NewlineDiffDocument(String initialContent) {   //  FIXME
		super();
		setTextStore(new CopyOnWriteTextStore(new GapTextStore()));
		setLineTracker(new NewlineTracker());
		getStore().set(initialContent);
		getTracker().set(initialContent);
		completeInitialization();
	}*/
	
	@Override
	public void set(String text, long modificationStamp) {
		super.set(convertLineDelimiters(text), modificationStamp);
	}
	
	public boolean hasReference() {
		return diffEngine!=null;
	}
	
	public void setReference(String text) {
		if (text==null) diffEngine = null;
		else {
			initDiffEngine();
			diffEngine.setReferenceText(convertLineDelimiters(text));
		}
	}
	
	protected void initDiffEngine() {
		if (diffEngine==null) diffEngine = new DiffEngine();
	}
	
	public void diff() {
		if (diffEngine!=null) {
			diffEngine.setWorkingText(get());
			diffEngine.diff();
		}
	}
	
	public HintRange getHintRange(int pos) {
		if (diffEngine!=null) {
			return diffEngine.getHint(pos);
		} else {
			return new HintRange(0, 0, Hint.MARK);
		}
	}
	
	protected String convertLineDelimiters(String text) {
		return text.replace("\r", "");
	}
	
	
	
	public void dumpText() {  // shows line delimiters
		String text = get();
		text = text.replace("\r", "\\r");
		text = text.replace("\n", "\\n\n");
		System.out.println(text);
	}
	
}
