package org.asianclassics.tibetan.edit.test;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.TextUtilities;

public class TibDebug {

	public static void dumpChars(String s) {
		System.out.println("--------------");
		for (int i=0; i<s.length(); ++i) {
			System.out.println(s.charAt(i)+"="+s.codePointAt(i));
		}
	}
	
	
	public static void debugDelims(IDocument document) {
		dumpChars(TextUtilities.getDefaultLineDelimiter(document));
	
		String [] sa = document.getLegalLineDelimiters();
		for (String s : sa) {
			dumpChars(s);
		}
	}
	
	public static ITypedRegion getPart(IDocument document, int offset) {
		try {
			return document.getPartition(offset);
		} catch (BadLocationException e) {
            e.printStackTrace();
        }
		return null;
	}
	
	public static String getPartInfo(IDocument document, ITypedRegion part) {
		String info = "";
		try {
			info = "txt: '"+document.get(part.getOffset(), part.getLength())+"'  typ: "+part.getType()+"  ost: "+part.getOffset()+"  len: "+part.getLength();
		}  catch (BadLocationException e) {
		}
		return info;
	}
	
	public static void printParts(IDocument document) {
		StringBuffer buffer = new StringBuffer("------------------------------------------------------------\n");
		try {
			ITypedRegion[] partitions = document.computePartitioning(0, document.getLength());
			for (ITypedRegion part : partitions) {
	            buffer.append(getPartInfo(document, part));
                buffer.append("\n");
			}
			
		} catch (BadLocationException e) {
            e.printStackTrace();
        }
		System.out.print(buffer);
	}
	
	public static void printInfoAtPos(IDocument document, int pos) {
		System.out.println("pos: "+pos+"  "+getPartInfo(document, getPart(document, pos)));
	}
	
	public static void forceRepartition(IDocument document) {
		try {
			document.replace(0, 0, null);
		} catch (BadLocationException e) {
		}		
	}
	
	public static void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
		}
	}
	
	public static String getType(IDocument document, int offset) {
		try {
//			return doc.getPartition(offset).getType();
			return document.getContentType(offset);
		} catch (BadLocationException e) {
            e.printStackTrace();
        }
		return null;
	}
	
}
