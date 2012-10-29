package org.asianclassics.center.catalog;

import java.util.List;

import org.asianclassics.center.catalog.entry.model.EntryModel;
import org.asianclassics.center.catalog.event.CatalogTaskMakeTopEvent;
import org.asianclassics.center.catalog.event.CatalogTaskMakeTopEvent.CatalogTaskViewType;
import org.asianclassics.center.event.LogoutEvent;
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
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Injector;


public class SelectionView extends Composite {

	private EventBus eb;
	private Button btnTest;
	private Button btnAction;
	private Button btnUpdate;
	private Table potiTable;
	private TableViewer potiTableViewer;
	private SelectionController ctlr;
	private Table sutraTable;
	private TableViewer sutraTableViewer;
	private TableViewerColumn sutraCol;
	private TableViewerColumn titleCol;
	private int sutraIndex;
	private int potiIndex;
	private boolean doingUpdate;
	private String idOfEntryToEdit;
	private TableViewerColumn dateCol;

	public SelectionView(Composite parent, int style, Injector injector) {
		super(parent, style);
		setLayout(null);
		
		doingUpdate = false;
		idOfEntryToEdit = null;
		
		btnTest = new Button(this, SWT.NONE);
		btnTest.setBounds(425, 5, 34, 25);
		btnTest.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				ctlr.test();
			}
		});
		btnTest.setText("Test");
		
		btnAction = new Button(this, SWT.NONE);
		btnAction.setBounds(12, 5, 252, 25);
		btnAction.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				doAction();
			}
		});
		btnAction.setText("<action>");
		
		btnUpdate = new Button(this, SWT.NONE);
		btnUpdate.setBounds(476, 5, 66, 25);
		btnUpdate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				updateTables();
			}
		});
		btnUpdate.setText("Update");
		
		potiTableViewer = new TableViewer(this, SWT.BORDER | SWT.FULL_SELECTION);
		potiTableViewer.setContentProvider(ArrayContentProvider.getInstance());
		potiTable = potiTableViewer.getTable();
		potiTable.setBounds(12, 36, 101, 432);
		potiTable.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				onPotiSelect(e.item.getData());
			}
		});
		potiTable.setLinesVisible(true);
		potiTable.setHeaderVisible(true);
		
		TableViewerColumn potiCol = new TableViewerColumn(potiTableViewer, SWT.NONE);
		potiCol.getColumn().setText("Poti #");
		potiCol.getColumn().setWidth(80);
		potiCol.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (isInt(element)) return "<Begin poti>";
				else return ((Row)element).getValue();
			}
		});
		
		sutraTableViewer = new TableViewer(this, SWT.BORDER | SWT.FULL_SELECTION);
		sutraTableViewer.setContentProvider(ArrayContentProvider.getInstance());
		sutraTable = sutraTableViewer.getTable();
		sutraTable.setBounds(119, 36, 619, 432);
		sutraTable.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				onSutraSelect(e.item.getData());
			}
		});
		sutraTable.setLinesVisible(true);
		sutraTable.setHeaderVisible(true);
		
		sutraCol = new TableViewerColumn(sutraTableViewer, SWT.NONE);
		sutraCol.getColumn().setWidth(60);
		sutraCol.getColumn().setText("Sutra #");
		sutraCol.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (isInt(element))	return "<Add sutra>";
				else return ((Row)element).getValueAsNode().get("sutraIndex").asText();
			}
		});
		
		dateCol = new TableViewerColumn(sutraTableViewer, SWT.NONE);
		dateCol.getColumn().setWidth(106);
		dateCol.getColumn().setText("Date Submitted");
		dateCol.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (isInt(element))	return "";
				else {
					JsonNode date = ((Row)element).getValueAsNode().get("submitDate");
					if (date==null) return "null";
					else return date.asText();   ///   TODO:  decode date
				}
			}
		});
		
		titleCol = new TableViewerColumn(sutraTableViewer, SWT.NONE);
		titleCol.getColumn().setWidth(427);
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
		this.eb = eb;      ///  FIXME:  not used
		this.ctlr = ctlr;

		updateTables();
	}
	
	@Subscribe
	public void onMakeTop(CatalogTaskMakeTopEvent evt) {   //  NOTE:  won't be fired on first "lazy load"
		if (evt.getViewType()==CatalogTaskViewType.SELECTION) {
			System.out.println("onMakeTop");
			if (idOfEntryToEdit==null) updateTables();
		}
	}
	
	@Subscribe
	public void onLogout(LogoutEvent evt) {
		System.out.println("onLogout");
		idOfEntryToEdit=null;
	}
	

	private void updateTables() {
		System.out.println("update");
		doingUpdate = true;
		updatePotiTable();
		int initSelect = 1;
		if (potiTableViewer.getTable().getItemCount()==1) initSelect = 0;
		potiTableViewer.getTable().setSelection(initSelect);  // does NOT fire a select event
		onPotiSelect(potiTableViewer.getTable().getItem(initSelect).getData());
		doingUpdate = false;
	}
	
	private void updatePotiTable() {
		potiTableViewer.setInput(ctlr.getPotiList());
		potiTableViewer.refresh();
		potiTableViewer.insert(0, 0);
	}
	
	private void onPotiSelect(Object data) {
		if (isInt(data)) {
			potiIndex = -1;
			sutraIndex = 1;
			sutraTableViewer.setInput(null);
			sutraTableViewer.refresh();
			idOfEntryToEdit = null;
			updateAction();
		} else {
			potiIndex = ((Row)data).getValueAsInt();
			List<Row> sutraList = ctlr.getSutraList(potiIndex);
			int nextSutra = sutraList.get(0).getValueAsNode().get("sutraIndex").asInt()+1;
			sutraTableViewer.setInput(sutraList);
			sutraTableViewer.refresh();
			sutraTableViewer.insert(nextSutra, 0);
			int initSelect = 1;
			if (doingUpdate) initSelect = 0;
			sutraTableViewer.getTable().setSelection(initSelect);  // does NOT fire a select event
			onSutraSelect(sutraTableViewer.getTable().getItem(initSelect).getData());
		}
	}
	
	private void onSutraSelect(Object data) {
		if (isInt(data)) {
			sutraIndex = (int)data;
			idOfEntryToEdit = null;
			updateAction();
		}
		else {
			sutraIndex = ((Row)data).getValueAsNode().get("sutraIndex").asInt();
			idOfEntryToEdit = ((Row)data).getValueAsNode().get("_id").asText();
			System.out.println("id="+idOfEntryToEdit);
			updateAction();
		}
	}
	
	private void updateAction() {
		System.out.println("updateAction()    poti="+potiIndex+"   sutra="+sutraIndex+"   id="+idOfEntryToEdit);
		String action;
		if (potiIndex==-1) {
			action = "Begin new poti";
		}
		else if (idOfEntryToEdit==null) {
			action = "Add new sutra "+sutraIndex+" to poti "+potiIndex;
		}
		else {
			action = "Edit sutra "+sutraIndex+" in poti "+potiIndex;
		}
		btnAction.setText(action);
	}
	
	private void doAction() {
		System.out.println("doAction()   poti="+potiIndex+"   sutra="+sutraIndex+"   id="+idOfEntryToEdit);
		ctlr.doAction(potiIndex, sutraIndex, idOfEntryToEdit);
	}
	
	
	protected boolean isInt(Object obj) {
		return obj.getClass().isAssignableFrom(Integer.class);
	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
