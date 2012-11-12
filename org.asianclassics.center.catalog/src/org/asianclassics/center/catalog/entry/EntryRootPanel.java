package org.asianclassics.center.catalog.entry;

import org.asianclassics.center.catalog.CatalogApp;
import org.asianclassics.center.catalog.event.CatalogTaskMakeTopEvent;
import org.asianclassics.center.catalog.event.EntryDeleteAllowedEvent;
import org.asianclassics.center.catalog.event.EntryUserMessageEvent;
import org.asianclassics.center.catalog.event.CatalogTaskMakeTopEvent.CatalogTaskViewType;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.wb.swt.SWTResourceManager;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.eclipse.swt.widgets.Control;

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
	private Button btnBack;
	private Button btnRaw;


	public EntryRootPanel(Composite parent, int style, Injector injector) {
		super(parent, style);
		setLayout(new FormLayout());
		
		controlPanel = new Group(this, SWT.NONE);
		controlPanel.setLayout(new FormLayout());
		FormData fd_controlPanel = new FormData();
		fd_controlPanel.bottom = new FormAttachment(0, 70);
		fd_controlPanel.right = new FormAttachment(100, -12);
		fd_controlPanel.top = new FormAttachment(0, 0);
		fd_controlPanel.left = new FormAttachment(0, 0);
		controlPanel.setLayoutData(fd_controlPanel);
		
		entryScroller = new EntryScroller(this, SWT.NONE, injector);
		FormData fd_composite = new FormData();
		fd_composite.bottom = new FormAttachment(100, 0);
		fd_composite.right = new FormAttachment(100, 0);
		fd_composite.top = new FormAttachment(controlPanel, 6);
		lblUserMessage = new Label(controlPanel, SWT.NONE);
		
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
				requestDelete();
			}
		});
		FormData fd_btnAnotherButton = new FormData();
		fd_btnAnotherButton.top = new FormAttachment(btnSave, 0, SWT.TOP);
		fd_btnAnotherButton.left = new FormAttachment(btnSave, 6);
		btnDelete.setLayoutData(fd_btnAnotherButton);
		btnDelete.setText("Delete");
		btnDelete.setEnabled(false);

		lblUserMessage.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
		
		
		if (CatalogApp.debugMode) {
			btnBack = new Button(controlPanel, SWT.NONE);
			btnBack.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					eb.post(new CatalogTaskMakeTopEvent(CatalogTaskViewType.SELECTION));
				}
			});
			FormData fd_btnBack = new FormData();
			fd_btnBack.top = new FormAttachment(btnSubmit, 0, SWT.TOP);
			fd_btnBack.left = new FormAttachment(btnSubmit, 6);
			btnBack.setLayoutData(fd_btnBack);
			btnBack.setText("<< Back");
			
			btnRaw = new Button(controlPanel, SWT.NONE);
			btnRaw.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent arg0) {
					ctlr.viewRawData();
				}
			});
			FormData fd_btnRaw = new FormData();
			fd_btnRaw.top = new FormAttachment(btnBack, 0, SWT.TOP);
			fd_btnRaw.left = new FormAttachment(btnBack, 6);
			btnRaw.setLayoutData(fd_btnRaw);
			btnRaw.setText("Raw");
			controlPanel.setTabList(new Control[]{btnSubmit});
		}

		if (injector!=null) injector.injectMembers(this);
	}

	@Inject
	public void inject(EventBus eb, EntryController ctlr) {
		this.eb = eb;
		this.ctlr = ctlr;
	}
	
	@Subscribe
	public void onUserMessage(EntryUserMessageEvent evt) {
		lblUserMessage.setText(evt.getMessage());
	}
	
	@Subscribe
	public void onDeleteAllowed(EntryDeleteAllowedEvent evt) {
		btnDelete.setEnabled(true);
	}
	
	public void requestDelete() {
		MessageBox messageBox = new MessageBox(this.getShell(), SWT.APPLICATION_MODAL|SWT.YES|SWT.NO);
        messageBox.setText("Delete Entry");
        messageBox.setMessage("Really delete this entry?");
        int userChoice = messageBox.open();
        if (userChoice==SWT.YES) ctlr.delete();
	}
	
	public void reset() {
		xpndItem.setExpanded(false);
		lblUserMessage.setText("");
		btnDelete.setEnabled(false);
	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}

}
