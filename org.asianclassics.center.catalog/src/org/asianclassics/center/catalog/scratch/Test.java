package org.asianclassics.center.catalog.scratch;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;


public class Test {


	public static void main(String[] args) {

		test();
		
	}
	
	public static void test() {
		System.out.println(Test.class.getPackage().getImplementationVersion());
	}
	
	
	public static void test_file() {
		
		Path p = Paths.get("c:/scratch/temp/test.txt");
		String s;
		try {
			s = Files.readAllLines(p, Charset.defaultCharset()).get(0);
			System.out.println(s);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public static void test_dir() {
		
		String dir = System.getProperty("user.dir");  // dir where app runs from (or where JAR file is)
		System.out.println(dir);
	}
	
	
	public static void test_time() {
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