package org.asianclassics.text.edit;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.BadLocationException;
import javax.swing.text.Element;

import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;
import org.fife.ui.rsyntaxtextarea.parser.AbstractParser;
import org.fife.ui.rsyntaxtextarea.parser.DefaultParseResult;
import org.fife.ui.rsyntaxtextarea.parser.DefaultParserNotice;
import org.fife.ui.rsyntaxtextarea.parser.ParseResult;



public class SpellCheckParser extends AbstractParser {

	HashSet<String> dict;
	private DefaultParseResult result;
	
	public SpellCheckParser() {
		result = new DefaultParseResult(this);
		dict = new HashSet<String>();
		try {
			loadDict();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public ParseResult parse(RSyntaxDocument doc, String style) {
//		System.out.println("** parse **");
		Element root = doc.getDefaultRootElement();
		int lineCount = root.getElementCount();
		result.clearNotices();
		result.setParsedLines(0, lineCount-1);

		String lineTxt = null;
		for (int lineNum=0; lineNum<lineCount; lineNum++) {
			Element lineEl = root.getElement(lineNum);
			try {
				lineTxt = doc.getText(lineEl.getStartOffset(), lineEl.getEndOffset()-lineEl.getStartOffset()-1);
			} catch (BadLocationException e) {
				e.printStackTrace();
				break;
			}

//			System.out.println("***"+lineNum+":"+lineTxt);

			Matcher matcher = Pattern.compile("[a-zA-Z']+").matcher(lineTxt);
			while (matcher.find()) {
//				System.out.println(matcher.start()+":"+matcher.end()+"  '"+matcher.group()+"'");
				if (!dict.contains(matcher.group())) {
					DefaultParserNotice pn = new DefaultParserNotice(this, "", lineNum, lineEl.getStartOffset()+matcher.start(), matcher.end()-matcher.start());
					result.addNotice(pn);
				}
			}
		}
		
		return result;
	}

		
	public void loadDict() throws Exception {
		InputStream is = getClass().getResourceAsStream("/engdict.txt");
		if (is==null) {
			throw new Exception("Resource file not found.");
		}
	    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
	    while (reader.ready()) {
	    	String line = reader.readLine();
	    	if (line==null) break;
	    	dict.add(line.trim());
	    }
	    reader.close();
	}
	
}
