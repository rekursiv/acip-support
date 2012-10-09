package org.asianclassics.tibetan.edit;


import java.util.Arrays;

import org.asianclassics.tibetan.edit.hint.DiffDocument;
import org.asianclassics.tibetan.edit.hint.TibetanDiffDocument;
import org.asianclassics.tibetan.transcoder.TibetanTranscoder;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.jface.text.source.SourceViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;


public class TibetanEditor extends Composite {

	private DiffDocument document = null;
	private SourceViewer textViewer = null;
	private TibetanPartitionScanner scanner = null;
	private EditManager editManager = null;

	public TibetanEditor(Composite parent, int style) {
		super(parent, style);
		setLayout(new FillLayout(SWT.HORIZONTAL));
		textViewer = new SourceViewer(this, null, SWT.BORDER);
	}
	
	public void init(TibetanTranscoder xcdr) {
		if (xcdr==null) document = new DiffDocument();
		else document = new TibetanDiffDocument(xcdr);
		textViewer.setDocument(document);
		textViewer.configure(new TibetanSourceViewerConfiguration(xcdr));		
		if (xcdr!=null) {
			scanner = new TibetanPartitionScanner();
			FastPartitioner parter = new FastPartitioner(scanner, scanner.getLegalContentTypes());
//			document.setDocumentPartitioner("tibetan", parter);
			document.setDocumentPartitioner(parter);
			parter.connect(document);			
			editManager = new EditManager(scanner, textViewer, xcdr);
		}
	}
	
	public void setReferenceText(String text) {
		document.setReference(text);
	}
	
	public void setWorkingText(String text) {
		document.set(text);
		if (editManager!=null) editManager.update();
	}
	
	public String getWorkingText() {
		return document.get();
	}
	
	public DiffDocument getDocument() {
		return document;
	}
	
	
	////////////////////////
	
	
	public void debug() {
//		Debug.debugDelims(document);
		System.out.println(Arrays.toString(document.getPartitionings()));
	}
	
	public void test(int arg) {
		if (editManager!=null) editManager.test(arg);
//		Debug.printParts(document);
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

	
	
}

