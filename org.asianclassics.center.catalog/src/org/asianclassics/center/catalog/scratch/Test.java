package org.asianclassics.center.catalog.scratch;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class Test {


	public static void main(String[] args) {

		DateTime dt = new DateTime();
		
		System.out.println(dt.getZone().toString());
		System.out.println(dt.withZone(DateTimeZone.getDefault()).getZone().toString());
		
		String s = dt.toString("d MMMM, y 'at' h:m a");
		
		System.out.println(s);
		
		
	}

}
