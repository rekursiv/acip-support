package org.asianclassics.center.catalog;

import java.util.List;

import org.asianclassics.center.catalog.entry.model.EntryModel;
import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonNode;
import org.codehaus.jackson.node.IntNode;
import org.codehaus.jackson.node.TextNode;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.StructuredSelection;
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
import org.ektorp.ViewResult.Row;

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
	private boolean isNewPotiSelected;
	private int sutraIndex;
	private int potiIndex;

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
				onPotiSelect(e.item.getData());
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
				if (isString(element)) return (String)element;
				return ((Row)element).getValue();
			}
		});
		
		sutraTableViewer = new TableViewer(grpEditASutra, SWT.BORDER | SWT.FULL_SELECTION);
		sutraTableViewer.setContentProvider(ArrayContentProvider.getInstance());
		sutraTable = sutraTableViewer.getTable();
		sutraTable.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				onSutraSelect(e.item.getData());
			}
		});
		sutraTable.setLinesVisible(true);
		sutraTable.setHeaderVisible(true);
		sutraTable.setBounds(117, 25, 368, 175);
		
		sutraCol = new TableViewerColumn(sutraTableViewer, SWT.NONE);
		sutraCol.getColumn().setWidth(100);
		sutraCol.getColumn().setText("Sutra");
		sutraCol.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (isInt(element))	return "<Add sutra>";
				else return ((Row)element).getValueAsNode().get("sutraIndex").asText();
			}
		});
		
		titleCol = new TableViewerColumn(sutraTableViewer, SWT.NONE);
		titleCol.getColumn().setWidth(256);
		titleCol.getColumn().setText("Tibetan Title");
		titleCol.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (isInt(element)) return "";
				else return ((Row)element).getValueAsNode().get("titleTibetan").asText();
			}
		});
		
		
		
		if (injector!=null) injector.injectMembers(this);
		
	}
	





	@Inject
	public void inject(EventBus eb, SelectionController ctlr) {
		this.eb = eb;
		this.ctlr = ctlr;
		
		updateTables();   ///   FIXME
		
		potiTableViewer.getTable().setSelection(1);  // does NOT fire a select event
		onPotiSelect(potiTableViewer.getTable().getItem(1).getData());  // FIXME
	}
	

	private void updateTables() {
		potiTableViewer.setInput(ctlr.getPotiList());
		potiTableViewer.refresh();
		potiTableViewer.insert("<Add poti>", 0);   ///   FIXME
	}
	
	private void onPotiSelect(Object data) {
		if (isString(data)) {
			potiIndex = -1;
			sutraIndex = -1;
			sutraTableViewer.setInput(null);
			sutraTableViewer.refresh();
//			sutraTableViewer.insert("sutra #1 will be added", 0);
			isNewPotiSelected = true;
			updateAction(true);
		} else {
			potiIndex = ((Row)data).getValueAsInt();
			List<Row> sutraList = ctlr.getSutraList(potiIndex);
			int nextSutra = sutraList.get(0).getValueAsNode().get("sutraIndex").asInt()+1;
			sutraTableViewer.setInput(sutraList);
			sutraTableViewer.refresh();
			sutraTableViewer.insert(nextSutra, 0);
			
			
			sutraTableViewer.getTable().setSelection(1);  // does NOT fire a select event
			onSutraSelect(sutraTableViewer.getTable().getItem(1).getData());
		}
	}
	
	private void onSutraSelect(Object data) {
		if (isInt(data)) {
			sutraIndex = (int)data;
			updateAction(true);
		}
		else {
			sutraIndex = ((Row)data).getValueAsNode().get("sutraIndex").asInt();
			updateAction(false);
		}
	}
	
	private void updateAction(boolean isNew) {
		System.out.println("poti="+potiIndex+"   sutra="+sutraIndex+"   new="+isNew);
	}
	
	
	protected boolean isInt(Object obj) {
		return obj.getClass().isAssignableFrom(Integer.class);
	}


	private boolean isString(Object obj) {
		return obj.getClass().isAssignableFrom(String.class);
	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
