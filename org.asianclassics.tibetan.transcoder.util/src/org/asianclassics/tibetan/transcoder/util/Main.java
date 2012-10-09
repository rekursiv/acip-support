package org.asianclassics.tibetan.transcoder.util;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		TableGen tg = new TableGen();
		try {
			tg.generate();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
