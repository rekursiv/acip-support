package org.asianclassics.tibetan.edit;

import org.asianclassics.tibetan.edit.hint.TibetanHintStyler;
import org.asianclassics.tibetan.transcoder.TibetanTranscoder;
import org.eclipse.jface.resource.JFaceResources;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.formatter.ContentFormatter;
import org.eclipse.jface.text.formatter.IContentFormatter;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Font;
import org.eclipse.wb.swt.SWTResourceManager;


public class TibetanSourceViewerConfiguration extends SourceViewerConfiguration {
	
	private TibetanTranscoder xcdr = null;
	
	public TibetanSourceViewerConfiguration(TibetanTranscoder xcdr) {
		this.xcdr = xcdr;
	}

	
	public IContentFormatter getContentFormatter(ISourceViewer sourceViewer) {
		if (xcdr==null) {
			return super.getContentFormatter(sourceViewer);
		}
		else {
			ContentFormatter formatter = new ContentFormatter();
			formatter.setFormattingStrategy(new TranscodingStrategy(xcdr, TibetanTranscoder.TIBETAN), "tibetan_edit");
			formatter.setFormattingStrategy(new TranscodingStrategy(xcdr, TibetanTranscoder.ROMAN), "roman_display");
			formatter.enablePartitionAwareFormatting(true);
			return formatter;
		}
	}
	
	public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
		PresentationReconciler recon = new PresentationReconciler();
		setupReconciler(recon, "tibetan_display");
		setupReconciler(recon, "roman_display");
		setupReconciler(recon, "tibetan_edit");
		setupReconciler(recon, "roman_edit");
		setupReconciler(recon, "comment");			
		return recon;
	}
	
	private void setupReconciler(PresentationReconciler recon, String contentType) {
		DefaultDamagerRepairer ddr= new DefaultDamagerRepairer(new TibetanHintStyler(contentType));
		recon.setDamager(ddr, contentType);
		recon.setRepairer(ddr, contentType);
	}
	
	
	
	
	
	
	
	
	///////////////////   DEBUG COLORS   ///////////////////  TODO:  move into TibetanHintStyler
	
	
	public IPresentationReconciler _getPresentationReconciler(ISourceViewer sourceViewer) {
		PresentationReconciler recon = new PresentationReconciler();
		
//		DefaultDamagerRepairer dr= new DefaultDamagerRepairer(new HintStyler());
//		recon.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
//		recon.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);
		
		if (xcdr!=null) {
		
			DefaultDamagerRepairer drTibDisp= new DefaultDamagerRepairer(new AttributeScanner(getStyle(255, 255, 220, true)));
			recon.setDamager(drTibDisp, "tibetan_display");
			recon.setRepairer(drTibDisp, "tibetan_display");
			
			DefaultDamagerRepairer drRomanDisp= new DefaultDamagerRepairer(new AttributeScanner(getStyle(220, 255, 255, false)));
			recon.setDamager(drRomanDisp, "roman_display");
			recon.setRepairer(drRomanDisp, "roman_display");
		
			DefaultDamagerRepairer drTibEdit= new DefaultDamagerRepairer(new AttributeScanner(getStyle(255, 240, 100, true)));
			recon.setDamager(drTibEdit, "tibetan_edit");
			recon.setRepairer(drTibEdit, "tibetan_edit");
			DefaultDamagerRepairer drRomanEdit= new DefaultDamagerRepairer(new AttributeScanner(getStyle(100, 255, 255, false)));
			recon.setDamager(drRomanEdit, "roman_edit");
			recon.setRepairer(drRomanEdit, "roman_edit");
			
			DefaultDamagerRepairer drComment= new DefaultDamagerRepairer(new AttributeScanner(getStyle(222, 240, 200, false)));
			recon.setDamager(drComment, "comment");
			recon.setRepairer(drComment, "comment");
		
		}
		return recon;
	}
	
	private TextAttribute getStyle(int bgRed, int bgGreen, int bgBlue, boolean isTibetan) {
		Font font;
		if (isTibetan) font = SWTResourceManager.getFont("TibetanMachineUni", 26, SWT.NORMAL);
		else font = JFaceResources.getBannerFont();
		return new TextAttribute(
				SWTResourceManager.getColor(0, 0, 0),
				SWTResourceManager.getColor(bgRed, bgGreen, bgBlue),
				SWT.NORMAL,
				font);
	}
		
	  
}
