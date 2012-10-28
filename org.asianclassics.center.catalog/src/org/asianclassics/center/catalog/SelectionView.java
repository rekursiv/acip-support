package org.asianclassics.center.catalog;

import org.asianclassics.center.catalog.entry.model.EntryModel;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TableViewerColumn;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Injector;


public class SelectionView extends Composite {

	private EventBus eb;
	private Button btnTest;
	private Group grpAddNewSutra;
	private Button btnAddSutra;
	private Button btnBeginPoti;
	private Table potiTable;
	private TableViewer potiTableViewer;
	private SelectionController ctlr;
	private Group grpEditASutra;
	private Table sutraTable;
	private TableViewer sutraTableViewer;
	private TableViewerColumn sutraCol;
	private TableViewerColumn titleCol;

	public SelectionView(Composite parent, int style, Injector injector) {
		super(parent, style);
		setLayout(new FormLayout());
		
		grpAddNewSutra = new Group(this, SWT.NONE);
		FormData fd_grpAddNewSutra = new FormData();
		fd_grpAddNewSutra.bottom = new FormAttachment(0, 130);
		fd_grpAddNewSutra.right = new FormAttachment(0, 292);
		fd_grpAddNewSutra.top = new FormAttachment(0, 10);
		fd_grpAddNewSutra.left = new FormAttachment(0, 10);
		grpAddNewSutra.setLayoutData(fd_grpAddNewSutra);
		grpAddNewSutra.setText("Add a new sutra");
		
		btnAddSutra = new Button(grpAddNewSutra, SWT.NONE);
		btnAddSutra.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ctlr.addSutra();
			}
		});
		btnAddSutra.setBounds(10, 27, 75, 25);
		btnAddSutra.setText("Add sutra");
		
		btnBeginPoti = new Button(grpAddNewSutra, SWT.NONE);
		btnBeginPoti.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ctlr.beginPoti();
			}
		});
		btnBeginPoti.setBounds(10, 63, 75, 25);
		btnBeginPoti.setText("Begin poti");
		
		btnTest = new Button(grpAddNewSutra, SWT.NONE);
		btnTest.setBounds(172, 27, 75, 25);
		btnTest.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ctlr.test();
//				updateTables();
			}


		});
		btnTest.setText("Test");
		
		grpEditASutra = new Group(this, SWT.NONE);
		grpEditASutra.setText("Edit a sutra you have worked on before");
		FormData fd_grpEditASutra = new FormData();
		fd_grpEditASutra.bottom = new FormAttachment(0, 520);
		fd_grpEditASutra.right = new FormAttachment(0, 530);
		fd_grpEditASutra.top = new FormAttachment(grpAddNewSutra, 6);
		fd_grpEditASutra.left = new FormAttachment(grpAddNewSutra, 0, SWT.LEFT);
		grpEditASutra.setLayoutData(fd_grpEditASutra);
		
		potiTableViewer = new TableViewer(grpEditASutra, SWT.BORDER | SWT.FULL_SELECTION);
		potiTableViewer.setContentProvider(ArrayContentProvider.getInstance());
		potiTable = potiTableViewer.getTable();
		potiTable.setBounds(10, 25, 101, 175);
		potiTable.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				int potiIndex = ((EntryModel)e.item.getData()).potiIndex;
				System.out.println("you selected "+potiIndex);
				updateSutraTable(potiIndex);
			}
		});
		potiTable.setLinesVisible(true);
		potiTable.setHeaderVisible(true);
		
		TableViewerColumn potiCol = new TableViewerColumn(potiTableViewer, SWT.NONE);
		potiCol.getColumn().setText("Poti");
		potiCol.getColumn().setWidth(80);
		potiCol.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return ((EntryModel)element).potiIndex+"";
			}
		});
		
		sutraTableViewer = new TableViewer(grpEditASutra, SWT.BORDER | SWT.FULL_SELECTION);
		sutraTableViewer.setContentProvider(ArrayContentProvider.getInstance());
		sutraTable = sutraTableViewer.getTable();
		sutraTable.setLinesVisible(true);
		sutraTable.setHeaderVisible(true);
		sutraTable.setBounds(117, 25, 368, 175);
		
		sutraCol = new TableViewerColumn(sutraTableViewer, SWT.NONE);
		sutraCol.getColumn().setWidth(100);
		sutraCol.getColumn().setText("Sutra");
		sutraCol.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return ((EntryModel)element).sutraIndex+"";
			}
		});
		
		titleCol = new TableViewerColumn(sutraTableViewer, SWT.NONE);
		titleCol.getColumn().setWidth(256);
		titleCol.getColumn().setText("Tibetan Title");
		titleCol.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				return ((EntryModel)element).titleTibetan;
			}
		});
		
		
		
		if (injector!=null) injector.injectMembers(this);
		
	}
	
	@Inject
	public void inject(EventBus eb, SelectionController ctlr) {
		this.eb = eb;
		this.ctlr = ctlr;
		
		updateTables();   ///   FIXME
	}
	

	private void updateTables() {
		potiTableViewer.setInput(ctlr.getPotiList());
		potiTableViewer.refresh();
	}
	
	private void updateSutraTable(int potiIndex) {
		ctlr.getSutraList(potiIndex);
	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
