package org.asianclassics.center.catalog.scratch;

import java.text.DecimalFormat;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class Test {


	public static void main(String[] args) {

		test();
		
	}
	
	public static void test() {
		DateTime dt = new DateTime();
		String s = dt.toString("h:m:ss a");
//		String s = dt.toString("d MMMM, y 'at' h:m a");
		System.out.println(s);
	}

}

/*
 
 		DateTime dt = new DateTime();
		System.out.println(dt.getZone().toString());
		System.out.println(dt.withZone(DateTimeZone.getDefault()).getZone().toString());
		String s = dt.toString("d MMMM, y 'at' h:m a");

*/