package org.asianclassics.center.input;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class InputTest {

	public enum ErrorType {NONE, OBVIOUS, SIMPLE, COMPLEX};
	
	private static final int offset = 1;
	
	private Logger log;
	private int startPos=10;
	private int endPos;

	@Inject
	public InputTest(Logger log) {
		this.log = log;
	}

	public void test() {
		System.out.println(getPage(1, ErrorType.OBVIOUS));
	}
	
	public String getPage(int page, ErrorType errorType) {
		StringBuffer buf = new StringBuffer(getPage(page));
		endPos=buf.length();
		if (endPos>startPos && errorType!=ErrorType.NONE) {
			switch (errorType) {
				case OBVIOUS: addObviousErrors(buf); break;
				case SIMPLE: addSimpleErrors(buf); break;
				case COMPLEX: addComplexErrors(buf); break;
				default: break;
			}
		}
		return buf.toString();
	}

	private void addObviousErrors(StringBuffer buf) {
		buf.insert(rand(), "x");
		buf.insert(rand(), "x");
	}
	
	private void addSimpleErrors(StringBuffer buf) {
		buf.deleteCharAt(rand());
		buf.insert(rand(), "D");
		int r = rand();
		buf.replace(r, r+1, "K");
	}
	
	private void addComplexErrors(StringBuffer buf) {
		buf.deleteCharAt(rand());
		buf.deleteCharAt(rand());
		buf.deleteCharAt(rand());
		buf.insert(rand(), "D");
		buf.insert(rand(), "NG");
		buf.insert(rand(), "P");
		int r = rand();
		buf.replace(r, r+1, "K");
		r = rand();
		buf.replace(r, r+1, "A");
		r = rand();
		buf.replace(r, r+1, "B");
	}
	
	public String getPage(int page) {
		int pageToGet = page+offset;
		int curPageIndex = 0;
		StringBuilder sb = new StringBuilder();
		try {
			InputStream is = InputTest.class.getResourceAsStream("080_KA.txt");
			BufferedReader reader = new BufferedReader(new InputStreamReader(is));
			String line;
			while ((line = reader.readLine()) != null) {
				if (line.startsWith("@")) {
					++curPageIndex;
					line = line.replaceFirst("@\\d[AB] ", "$0\n");
					if (sb.length()>0) {
//						System.out.print(String.format("%04d", curPageIndex)+"|"+sb.toString());
						return sb.toString();
					}
				}
				if (curPageIndex<pageToGet) continue;
				line = line.replace("|", "");
				if (line.length()>0) {
					sb.append(line);
					sb.append(" ");
				} else {
					sb.append("\n");
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private int rand() {
		return (int)(Math.random()*(endPos-startPos))+startPos;
	}
	
}
