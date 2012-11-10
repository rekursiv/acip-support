package org.asianclassics.center.catalog.scratch;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

import org.asianclassics.center.catalog.entry.model.StampModel;
import org.asianclassics.center.catalog.entry.model.StampRepo;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.ektorp.Attachment;

import com.google.inject.Inject;
import com.google.inject.Injector;

public class TestPanel extends Composite {
	private Label lblTest;
	private StampRepo repo;
	private Logger log;


	public TestPanel(Composite parent, int style, Injector injector) {
		super(parent, style);
		
		lblTest = new Label(this, SWT.NONE);
		lblTest.setBounds(10, 10, 193, 176);
		lblTest.setText("TEST");

		if (injector!=null) injector.injectMembers(this);
		
	}
	
	@Inject
	public void inject(StampRepo sr, Logger log) {
		repo = sr;
		this.log = log;
		getImage(12);
	}

	public void getImage(int index) {
		try {
			lblTest.setImage(new Image(getDisplay(), repo.getImageByIndex(index)));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void test() {
		
//		StampModel stamp = repo.get("0001");
		StampModel stamp = repo.getByIndex(1);
		log.info(""+stamp.index);
		log.info(stamp.category);
		Map<String, Attachment> am = stamp.getAttachments();
		if (am!=null && am.size()==1) {
			String key = am.keySet().iterator().next();
			log.info(key);
			
			try {
				Image i = new Image(getDisplay(), repo.getImage("0001", key));
				lblTest.setImage(i);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			log.info("something wrong with attachments");
		}
		
	}
	

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
