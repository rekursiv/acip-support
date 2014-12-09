package org.asianclassics.text.edit;

import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.Segment;

import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;
import org.fife.ui.rsyntaxtextarea.Token;
import org.fife.ui.rsyntaxtextarea.TokenMaker;
import org.fife.ui.rsyntaxtextarea.TokenMakerFactory;
import org.fife.util.DynamicIntArray;

@SuppressWarnings("serial")
public class DiffSyntaxDocument extends RSyntaxDocument {

	private static final boolean DEBUG_TOKEN_CACHING = false;

//	private DiffTokenMaker diffTokenMaker;
	
//	public DiffSyntaxDocument() {
//		super(null);
//	}

//	public void setSyntaxStyle(TokenMaker tokenMaker) {
//		this.tokenMaker = tokenMaker;
//		updateSyntaxHighlightingInformation();
//	}
	
	public DiffSyntaxDocument(DiffTokenMaker diffTokenMaker) {
		putProperty(tabSizeAttribute, Integer.valueOf(5));
		lastTokensOnLines = new DynamicIntArray(400);
		lastTokensOnLines.add(Token.NULL); // Initial (empty) line.
		s = new Segment();
		this.tokenMaker = diffTokenMaker;
	}

	
	@Override    //   NOTE:  had to change lib to override
	public Token getTokenListForLine(int line) {

//		System.err.println(lastLine+":"+line);
		
		tokenRetrievalCount++;
		if (line==lastLine && cachedTokenList!=null) {
			if (DEBUG_TOKEN_CACHING) {
				useCacheCount++;
				System.err.println("--- Using cached line; ratio now: " +
						useCacheCount + "/" + tokenRetrievalCount);
			}
			return cachedTokenList;
		}
		lastLine = line;
		
		Element map = getDefaultRootElement();
		Element elem = map.getElement(line);
		int startOffset = elem.getStartOffset();
		//int endOffset = (line==map.getElementCount()-1 ? elem.getEndOffset() - 1:
		//									elem.getEndOffset() - 1);
		int endOffset = elem.getEndOffset() - 1; // Why always "-1"?
		try {
			getText(startOffset,endOffset-startOffset, s);
		} catch (BadLocationException ble) {
			ble.printStackTrace();
			return null;
		}
		int initialTokenType = line==0 ? Token.NULL :
								getLastTokenTypeOnLine(line-1);

		//return tokenMaker.getTokenList(s, initialTokenType, startOffset);
		DiffTokenMaker dtm = (DiffTokenMaker)tokenMaker;
		cachedTokenList = dtm.getTokenList(s, initialTokenType, startOffset, line);
		return cachedTokenList;

	}
	
	
	
	public Token _getTokenListForLine(int line) {  
		System.out.println("### "+line);
		return super.getTokenListForLine(line);
	}
	
	
}
