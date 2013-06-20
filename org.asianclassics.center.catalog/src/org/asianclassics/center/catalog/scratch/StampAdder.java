package org.asianclassics.center.catalog.scratch;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.asianclassics.center.CenterModule;
import org.asianclassics.center.catalog.entry.model.StampModel;
import org.asianclassics.center.catalog.entry.model.StampRepo;
import org.asianclassics.center.link.LinkManager;
import org.ektorp.AttachmentInputStream;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.inject.Guice;
import com.google.inject.Injector;

public class StampAdder {

	private static StampAdder instance;
	private ObjectMapper mapper;
	private StampRepo repo;

	public static void main(String[] args) {
		instance = new StampAdder();
//		instance.run();
	}

	public void run() {
		try {
			addStamp();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void addStamp() throws Exception {
		System.getProperties().setProperty("org.ektorp.support.AutoUpdateViewOnChange", "true");
		mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
		
		Injector injector = Guice.createInjector(new CenterModule());
		LinkManager linkManager = injector.getInstance(LinkManager.class);
		linkManager.init();
		Thread.sleep(3000);  // wait for bootstrap
		
		repo = injector.getInstance(StampRepo.class);
		int nextStamp = repo.getLatestStampIndex()+1;	
		System.out.println("next stamp # "+nextStamp);

		Path p = Paths.get("c:/scratch/temp/new_stamps/1022.jpg");
		String category = "New";
		addStamp(nextStamp, category, p);

		
		List<StampModel> stamps = repo.getByCategory("New");
		for (StampModel stamp : stamps) {
			debug(stamp);
		}
		
		linkManager.destroy();	
		System.out.println("Done.");
	}
	

	private void addStamp(int index, String category, Path path) throws Exception {
		StampModel stamp = new StampModel();
		stamp.index = index;
		stamp.category = category;
		repo.add(stamp);
		InputStream is = Files.newInputStream(path);
		AttachmentInputStream ais = new AttachmentInputStream("stamp.jpg", is, "image/jpeg");
		repo.getDb().createAttachment(stamp.getId(), stamp.getRevision(), ais);
	}


	
	private void debug(Object obj) throws Exception {
		if (obj==null) {
			System.out.println("!!!   NULL");
			return;
		}
		System.out.println("--------------");
		System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj));
		System.out.println("^^^^^^^^^^^^^^");
	}
	
}
