package org.asianclassics.database;

import org.joda.time.DateTime;
import org.joda.time.chrono.ISOChronology;

public class DateTimeStamp {
	
	public static String gen() {
		return new DateTime(ISOChronology.getInstanceUTC()).toString();
	}

}
