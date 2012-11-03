package org.asianclassics.center.catalog.scratch;

import java.text.DecimalFormat;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class Test {


	public static void main(String[] args) {

		test(1);
		test(2.34556);
		test(5.5);
		test(5.55);
		test(34.999);
		
	}
	
	public static void test(double f) {
		new DecimalFormat("#.##").format(f);
//		String s = String.format("%.1f", f);
		String s = new DecimalFormat("#.#").format(f);
		System.out.println(s);
	}

}

/*
 
 		DateTime dt = new DateTime();
		System.out.println(dt.getZone().toString());
		System.out.println(dt.withZone(DateTimeZone.getDefault()).getZone().toString());
		String s = dt.toString("d MMMM, y 'at' h:m a");

*/