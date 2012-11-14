package org.asianclassics.center.catalog.entry;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.asianclassics.center.catalog.entry.model.StampModel;
import org.asianclassics.center.catalog.entry.model.StampRepo;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

import com.google.inject.Inject;
import com.google.inject.Injector;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.widgets.Combo;

public class StampBrowseDialog extends Dialog {

	protected int stampNum;
	protected Shell shell;
	private StampRepo repo;
	private Logger log;
	private ScrolledComposite stampScroller;
	private Composite stampPanel;
	private int stampPanelComputedHeight;
	private Label lblSelectCategory;
	private Combo cmbCategory;
	private Label lblWait;

	/**
	 * Create the dialog.
	 * @param parent
	 * @param style
	 */
	public StampBrowseDialog(Shell parent, Injector injector) {
		super(parent, SWT.RESIZE | SWT.DIALOG_TRIM | SWT.APPLICATION_MODAL);
		setText("Stamp Browser");
		if (injector!=null) injector.injectMembers(this);
	}
	
	@Inject
	public void inject(StampRepo sr, Logger log) {
		repo = sr;
		this.log = log;
	}

	/**
	 * Open the dialog.
	 * @return the result
	 */
	public int open() {
		createContents();
		shell.open();
		shell.layout();
		
//		loadStamps("Tibetan_Single");                   ////////////////////
		
		Display display = getParent().getDisplay();
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		return stampNum;
	}

	/**
	 * Create contents of the dialog.
	 */
	private void createContents() {
		shell = new Shell(getParent(), getStyle());
		shell.setSize(1000, 750);
		shell.setText(getText());
		shell.setLayout(new FormLayout());

		stampScroller = new ScrolledComposite(shell, SWT.BORDER | SWT.V_SCROLL);
		FormData fd_stampScroller = new FormData();
		fd_stampScroller.bottom = new FormAttachment(100, -12);
		fd_stampScroller.right = new FormAttachment(100, -12);
		fd_stampScroller.left = new FormAttachment(0, 12);
		stampScroller.setLayoutData(fd_stampScroller);
		stampScroller.setExpandHorizontal(true);
		stampScroller.setExpandVertical(true);
		stampScroller.setShowFocusedControl(true);
		
		stampPanel = new Composite(stampScroller, SWT.NONE);
		stampPanel.setLayout(new RowLayout(SWT.HORIZONTAL));
		
		stampPanel.addListener(SWT.Resize, new Listener() {
			@Override
			public void handleEvent(Event arg0) {
				adjustScrollerHeight();
			}
		});
		
		// add a button to the initial stamp panel to give the user a chance to cancel before selecting a category
		// this button will be deleted when the stamps are loaded, but at that point the user has stamps to select
		Button btnCancel = new Button(stampPanel, SWT.NONE);
		btnCancel.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				stampNum = 0;
				shell.close();
			}
		});
		btnCancel.setText("Cancel");
		
		
		stampScroller.setContent(stampPanel);
		
		lblSelectCategory = new Label(shell, SWT.NONE);
		FormData fd_lblSelectCategory = new FormData();
		fd_lblSelectCategory.top = new FormAttachment(0, 12);
		fd_lblSelectCategory.left = new FormAttachment(stampScroller, 0, SWT.LEFT);
		lblSelectCategory.setLayoutData(fd_lblSelectCategory);
		lblSelectCategory.setText("Select Category:");
		
		cmbCategory = new Combo(shell, SWT.READ_ONLY);
		cmbCategory.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				lblWait.setVisible(true);
				lblWait.update();
				deleteStamps();
				String category = cmbCategory.getText().replace(" ", "_");
				log.info(category);
				loadStamps(category);
				lblWait.setVisible(false);
				stampScroller.setFocus();
			}
		});
		cmbCategory.setItems(new String[] {"Chinese", "Lanycha", "Mongolian", "Picture", "Tibetan Circle", "Tibetan Drawing", "Tibetan Single", "Tibetan Square"});
		FormData fd_cmbCategory = new FormData();
		fd_cmbCategory.top = new FormAttachment(lblSelectCategory, 0, SWT.TOP);
		fd_cmbCategory.left = new FormAttachment(lblSelectCategory, 6);
		cmbCategory.setLayoutData(fd_cmbCategory);
		fd_stampScroller.top = new FormAttachment(cmbCategory, 6);
		
		lblWait = new Label(shell, SWT.NONE);
		FormData fd_lblWait = new FormData();
		fd_lblWait.top = new FormAttachment(lblSelectCategory, -15);
		fd_lblWait.left = new FormAttachment(cmbCategory, 6);
		lblWait.setLayoutData(fd_lblWait);
		lblWait.setText("Loading stamps, please wait...");
		lblWait.setVisible(false);
	}

	
	private void deleteStamps() {
		Control[] stamps = stampPanel.getChildren();
		for (Control stamp : stamps) {
			stamp.dispose();
		}
		stampPanel.update();
	}

	private void loadStamps(String category) {
		List<StampModel> stamps = repo.getByCategory(category);
		log.info("numStamps="+stamps.size());
		for (StampModel stamp : stamps) {
			addImage(stamp);
		}		
		stampPanel.pack();
	}
	
	private void addImage(StampModel stamp) {
		try {
			ImageData imgData = repo.getImage(stamp);
			Image img = new Image(shell.getDisplay(), imgData);
			Button btnImage = new Button(stampPanel, SWT.NONE);
			btnImage.setImage(img);
			btnImage.setToolTipText(String.valueOf(stamp.index));
			btnImage.setData(stamp.index);
			btnImage.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent evt) {
					selectStamp((int)evt.widget.getData());
				}
			});
		} catch (Exception e) {
			log.log(Level.INFO, "", e);
		}
	}

	private void selectStamp(int num) {
		stampNum = num;
		shell.close();
	}

	
	private void adjustScrollerHeight() {
		int newHeight = stampPanel.computeSize(stampPanel.getSize().x, SWT.DEFAULT, true).y;
		if (newHeight!=stampPanelComputedHeight) {
//			log.info("set");
			stampPanelComputedHeight = newHeight;
			stampScroller.setMinHeight(newHeight);
		}
	}
	
	
	
	
	
	//////////////////
	
	
	private void addTest() {
		new Group(stampPanel, SWT.NONE);
		stampPanel.pack();
	}
	
	private void testStampPanelSize() {
		log.info("----");
		log.info("size: "+stampPanel.getSize().toString());
		log.info("cs x=def: "+stampPanel.computeSize(SWT.DEFAULT, SWT.DEFAULT, true).toString());
		log.info("cs x=100: "+stampPanel.computeSize(100, SWT.DEFAULT, true).toString());
	}
	
	
	private void loadStampsAsync() {        ///////////////////////   TEST
		Display.getDefault().timerExec(200, new Runnable() {
			@Override
			public void run() {
				String category = cmbCategory.getText().replace(" ", "_");
				log.info(category);
				loadStamps(category);
				lblWait.setVisible(false);
			}			
		});
	}
	
}
