package org.asianclassics.center.catalog.entry.cell;


import java.util.concurrent.atomic.AtomicInteger;

import org.asianclassics.center.catalog.entry.model.StampRepo;
import org.asianclassics.center.catalog.event.EntryCellListDeleteElementEvent;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.google.inject.Inject;


public class StampEntryCell extends LinkedEntryCell {

//	private Text txtTest;
	
	private Button btnDelete;
	private AtomicInteger stampNum;
	private Label lblImage;
	private Label lblStampNum;
	private StampRepo repo;


	public StampEntryCell(Composite parent, Object object) {
		super(parent, null);
		this.stampNum = (AtomicInteger) object;
		/*
		txtTest = new Text(this, SWT.NONE);
		FormData fd_txtTest = new FormData();
		fd_txtTest.right = new FormAttachment(0, 80);
		fd_txtTest.top = new FormAttachment(btnDelete, 6);
		fd_txtTest.left = new FormAttachment(0, 12);
		txtTest.setLayoutData(fd_txtTest);
		*/
		
		lblImage = new Label(this, SWT.NONE);
		FormData fd_lblImage = new FormData();
		fd_lblImage.top = new FormAttachment(0, 12);
		fd_lblImage.left = new FormAttachment(0, 12);
		lblImage.setLayoutData(fd_lblImage);
		lblImage.setText("<image goes here>");
		
		lblStampNum = new Label(this, SWT.NONE);
		FormData fd_lblStampNum = new FormData();
		fd_lblStampNum.top = new FormAttachment(lblImage, 0, SWT.TOP);
		fd_lblStampNum.left = new FormAttachment(lblImage, 6);
		lblStampNum.setLayoutData(fd_lblStampNum);
		lblStampNum.setText("12");
		
		
		btnDelete = new Button(this, SWT.NONE);
		btnDelete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				onDelete();
			}
		});
		FormData fd_btnDelete_1 = new FormData();
		fd_btnDelete_1.left = new FormAttachment(lblImage, 6);
//		fd_btnDelete_1.right = new FormAttachment(0, 100);
		fd_btnDelete_1.top = new FormAttachment(lblStampNum, 12);
		btnDelete.setLayoutData(fd_btnDelete_1);
		btnDelete.setText("Delete Stamp");
		
		addHorizSep();
		onModelToView();
	}
	
	@Inject
	public void inject(StampRepo repo) {
		this.repo = repo;
		System.out.println("StampRepo injection");
	}
	
	@Override
	public void addHorizSep() {
		Label horizSep = new Label(this, SWT.SEPARATOR | SWT.HORIZONTAL);
		FormData fd_horizSep = new FormData();
		fd_horizSep.top = new FormAttachment(lblImage, 10);
		fd_horizSep.right = new FormAttachment(100, 0);
		fd_horizSep.left = new FormAttachment(0, 0);
		horizSep.setLayoutData(fd_horizSep);
	}

	protected void onDelete() {
		eb.post(new EntryCellListDeleteElementEvent(this));
	}
	
	@Override
	public void onModelToView() {
		lblStampNum.setText(stampNum.toString());
		try {
			ImageData imgData = repo.getImageByIndex(stampNum.get());
			Image img = new Image(getDisplay(), imgData);
			lblImage.setImage(img);
		} catch (Exception e) {
			lblImage.setText("ERROR");
		}
		
	}

	@Override
	public void onViewToModel() {
//		stampNum.set(Integer.parseInt(txtTest.getText()));
//		System.out.println("StampEntryCell#onViewToModel()   "+stampNum);
	}
	
	@Override
	public void dispose() {
		System.out.println("StampEntryCell#dispose()");
		ctlr.getModel().stamps.remove(stampNum);
		eb.unregister(this);
		super.dispose();
	}
}
