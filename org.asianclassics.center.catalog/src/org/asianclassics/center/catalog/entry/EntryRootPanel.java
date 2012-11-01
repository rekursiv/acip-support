package org.asianclassics.center.catalog.entry;

import org.eclipse.swt.widgets.Composite;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.widgets.ExpandBar;

public class EntryRootPanel extends Composite {

	private EventBus eb;
	private EntryController ctlr;
	private Group controlPanel;
	private EntryScroller entryScroller;
	private Label lblUserMessage;
	private Button btnSubmit;

	private ExpandBar expandBar;
	private ExpandItem xpndItem;
	private Composite xpndPanel;
	private Button btnSave;
	private Button btnDelete;


	public EntryRootPanel(Composite parent, int style, Injector injector) {
		super(parent, style);
		setLayout(new FormLayout());
		
		controlPanel = new Group(this, SWT.NONE);
		controlPanel.setLayout(new FormLayout());
		FormData fd_controlPanel = new FormData();
		fd_controlPanel.bottom = new FormAttachment(0, 70);
		fd_controlPanel.right = new FormAttachment(100, 0);
		fd_controlPanel.top = new FormAttachment(0, 0);
		fd_controlPanel.left = new FormAttachment(0, 0);
		controlPanel.setLayoutData(fd_controlPanel);
		
		entryScroller = new EntryScroller(this, SWT.NONE, injector);
		FormData fd_composite = new FormData();
		fd_composite.bottom = new FormAttachment(100, 0);
		fd_composite.right = new FormAttachment(100, 0);
		fd_composite.top = new FormAttachment(controlPanel, 6);
		
		lblUserMessage = new Label(controlPanel, SWT.NONE);
		lblUserMessage.setText("This is the user message");
		FormData fd_lblUserMessage = new FormData();
		fd_lblUserMessage.right = new FormAttachment(0, 280);
		fd_lblUserMessage.top = new FormAttachment(8, -5);
		fd_lblUserMessage.left = new FormAttachment(0, 7);
		lblUserMessage.setLayoutData(fd_lblUserMessage);
		
		btnSubmit = new Button(controlPanel, SWT.NONE);
		FormData fd_btnSubmit = new FormData();
		fd_btnSubmit.top = new FormAttachment(lblUserMessage, 6);
		fd_btnSubmit.right = new FormAttachment(lblUserMessage, 75);
		fd_btnSubmit.left = new FormAttachment(lblUserMessage, 0, SWT.LEFT);
		btnSubmit.setLayoutData(fd_btnSubmit);
		btnSubmit.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				ctlr.submit();
			}
		});
		btnSubmit.setText("Submit");
		
		expandBar = new ExpandBar(controlPanel, SWT.NONE);
		expandBar.setSpacing(0);
		FormData fd_expandBar = new FormData();
		fd_expandBar.right = new FormAttachment(lblUserMessage, 200, SWT.RIGHT);
		fd_expandBar.bottom = new FormAttachment(0, 55);
		fd_expandBar.top = new FormAttachment(lblUserMessage, 0, SWT.TOP);
		fd_expandBar.left = new FormAttachment(lblUserMessage, 6);
		expandBar.setLayoutData(fd_expandBar);
		fd_composite.left = new FormAttachment(0, 0);
		entryScroller.setLayoutData(fd_composite);
		
		xpndItem = new ExpandItem(expandBar, SWT.NONE, 0);
		xpndItem.setExpanded(false);
		xpndItem.setText("Advanced");
		
		xpndPanel = new Composite(expandBar, SWT.NONE);
		xpndItem.setControl(xpndPanel);
		xpndItem.setHeight(55);
		xpndPanel.setLayout(new FormLayout());
		
		btnSave = new Button(xpndPanel, SWT.NONE);
		btnSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				ctlr.saveAsDraft();
			}
		});
		FormData fd_btnNewButton = new FormData();
		fd_btnNewButton.top = new FormAttachment(0, 1);
		fd_btnNewButton.left = new FormAttachment(0);
		btnSave.setLayoutData(fd_btnNewButton);
		btnSave.setText("Save As Draft");
		
		btnDelete = new Button(xpndPanel, SWT.NONE);
		btnDelete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				ctlr.test();
			}
		});
		FormData fd_btnAnotherButton = new FormData();
		fd_btnAnotherButton.top = new FormAttachment(btnSave, 0, SWT.TOP);
		fd_btnAnotherButton.left = new FormAttachment(btnSave, 6);
		btnDelete.setLayoutData(fd_btnAnotherButton);
		btnDelete.setText("Delete");


		if (injector!=null) injector.injectMembers(this);
	}

	@Inject
	public void inject(EventBus eb, EntryController ctlr) {
		this.eb = eb;
		this.ctlr = ctlr;
	}
	
	public void reset() {
		xpndItem.setExpanded(false);
	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}