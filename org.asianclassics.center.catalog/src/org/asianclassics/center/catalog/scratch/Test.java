package org.asianclassics.center.catalog.scratch;

import java.awt.Image;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;

import javax.imageio.ImageIO;

import org.asianclassics.center.catalog.entry.stamp.StampScanDialog;
import org.eclipse.swt.graphics.ImageData;



public class Test {


	public static void main(String[] args) {

		try {
			test();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public static void test() throws IOException {
		Path path = Paths.get("c:/scratch/temp/new_stamps/test2.jpg");
		InputStream is = Files.newInputStream(path);
//		ImageData id = new ImageData(is);
		Image image = ImageIO.read(is);
		ImageIO.createImageInputStream(image);
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
		
//		String dir = System.getProperty("user.dir");  // dir where app runs from (or where JAR file is)
		String dir = System.getProperty("user.home");  // user home dir
		System.out.println(dir);
	}
	
	
	public static void test_time() {
//		DateTime dt = new DateTime();
//		String s = dt.toString("h:m:ss a");
//		String s = dt.toString("d MMMM, y 'at' h:m a");
//		System.out.println(s);
	}

}

///		System.out.println(Test.class.getPackage().getImplementationVersion());