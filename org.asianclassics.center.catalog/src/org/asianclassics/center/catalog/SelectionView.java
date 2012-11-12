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
import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.viewers.ArrayContentProvider;
import org.eclipse.jface.viewers.ColumnLabelProvider;
import org.eclipse.jface.viewers.ColumnWeightData;
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
	private Button btnLogout;
	private int curPotiSelIndex;
	private int curSutraSelIndex;
	private String idOfEntryToCopy;

	public SelectionView(Composite parent, int style, Injector injector) {
		super(parent, style);
		
		doingUpdate = false;
		idOfEntryToEdit = null;

		if (CatalogApp.debugMode) {
		
			btnTest = new Button(this, SWT.NONE);
			FormData fd_btnTest = new FormData();
			fd_btnTest.top = new FormAttachment(1, 0);
			fd_btnTest.right = new FormAttachment(100, -1);
			btnTest.setLayoutData(fd_btnTest);
			btnTest.setBounds(425, 5, 34, 25);
			btnTest.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					ctlr.test();
				}
			});
			btnTest.setText("Test");
			
			btnUpdate = new Button(this, SWT.NONE);
			FormData fd_btnUpdate = new FormData();
			fd_btnUpdate.bottom = new FormAttachment(btnTest, 0, SWT.BOTTOM);
			fd_btnUpdate.right = new FormAttachment(btnTest, -6);

			btnUpdate.setLayoutData(fd_btnUpdate);
			btnUpdate.setBounds(476, 5, 66, 25);
			btnUpdate.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					updateTables();
				}
			});
			btnUpdate.setText("Update");
		
		}

		setLayout(new FormLayout());
		
		btnAction = new Button(this, SWT.NONE);
		FormData fd_btnAction = new FormData();
		fd_btnAction.right = new FormAttachment(0, 264);
		fd_btnAction.top = new FormAttachment(0, 5);
		fd_btnAction.left = new FormAttachment(0, 12);
		btnAction.setLayoutData(fd_btnAction);
		btnAction.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				doAction();
			}
		});
		btnAction.setText("<action>");
		
		
		
		potiTableViewer = new TableViewer(this, SWT.BORDER | SWT.FULL_SELECTION);
		potiTableViewer.setContentProvider(ArrayContentProvider.getInstance());
		potiTable = potiTableViewer.getTable();
		FormData fd_potiTable = new FormData();
		fd_potiTable.bottom = new FormAttachment(100, 0);
		fd_potiTable.top = new FormAttachment(0, 36);
		fd_potiTable.left = new FormAttachment(0, 0);
		potiTable.setLayoutData(fd_potiTable);
		potiTable.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				onPotiSelect(e.item.getData());
			}
		});
		potiTable.setLinesVisible(true);
		potiTable.setHeaderVisible(true);
		
		TableViewerColumn potiCol = new TableViewerColumn(potiTableViewer, SWT.NONE);
		potiCol.getColumn().setResizable(false);
		potiCol.getColumn().setText("Poti #");
		potiCol.getColumn().setWidth(100);
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
		FormData fd_sutraTable = new FormData();
		fd_sutraTable.left = new FormAttachment(potiTable, 6);
		fd_sutraTable.bottom = new FormAttachment(100, 0);
		fd_sutraTable.right = new FormAttachment(100, -12);
		fd_sutraTable.top = new FormAttachment(0, 36);
		sutraTable.setLayoutData(fd_sutraTable);
		
		sutraTable.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				onSutraSelect(e.item.getData());
			}
		});
		sutraTable.setLinesVisible(true);
		sutraTable.setHeaderVisible(true);
		
		sutraCol = new TableViewerColumn(sutraTableViewer, SWT.NONE);
		sutraCol.getColumn().setResizable(false);
		sutraCol.getColumn().setWidth(100);
		sutraCol.getColumn().setText("Sutra #");
		sutraCol.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (isInt(element))	return "<Add sutra>";
				else return ((Row)element).getValueAsNode().get("sutraIndex").asText();
			}
		});
		
		dateCol = new TableViewerColumn(sutraTableViewer, SWT.NONE);
		dateCol.getColumn().setResizable(false);
		dateCol.getColumn().setWidth(120);
		dateCol.getColumn().setText("Date Submitted");
		dateCol.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (isInt(element))	return "";
				else {
					JsonNode date = ((Row)element).getValueAsNode().get("dateTimeFirstSubmitted");
					if (date==null) return "---";
					else return date.asText().substring(0, date.asText().lastIndexOf('T'));  // show only date part
				}
			}
		});
		
		titleCol = new TableViewerColumn(sutraTableViewer, SWT.NONE);
		titleCol.getColumn().setResizable(false);
		titleCol.getColumn().setWidth(200);
		titleCol.getColumn().setText("Tibetan Title");
		titleCol.setLabelProvider(new ColumnLabelProvider() {
			@Override
			public String getText(Object element) {
				if (isInt(element)) return "";
				else {
					JsonNode title = ((Row)element).getValueAsNode().get("titleTibetan");
					if (title==null) return "";
					else return title.asText();
				}
			}
		});
		
		btnLogout = new Button(this, SWT.NONE);
		btnLogout.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				eb.post(new LogoutEvent());
			}
		});
		FormData fd_btnLogout = new FormData();
		fd_btnLogout.top = new FormAttachment(btnAction, 0, SWT.TOP);
		fd_btnLogout.left = new FormAttachment(btnAction, 6);
		btnLogout.setLayoutData(fd_btnLogout);
		btnLogout.setText("Logout");
		
		if (injector!=null) injector.injectMembers(this);
		
	}
	



	@Inject
	public void inject(EventBus eb, SelectionController ctlr) {
		this.eb = eb;      ///  FIXME:  not used
		this.ctlr = ctlr;
		reset();
		updateTables();
	}
	
	@Subscribe
	public void onMakeTop(CatalogTaskMakeTopEvent evt) {   //  NOTE:  won't be fired on first "lazy load"
		if (evt.getViewType()==CatalogTaskViewType.SELECTION) {
			System.out.println("onMakeTop");
//			if (idOfEntryToEdit==null) updateTables();   //  FIXME:  this doesn't update the tables after entry is deleted
			updateTables();   //  WORKAROUND FOR NOW:  just update them every time
		}
	}
	
	@Subscribe
	public void onLogout(LogoutEvent evt) {
		reset();
	}
	
	private void reset() {
		idOfEntryToEdit=null;
		idOfEntryToCopy=null;
		doingUpdate = false;
		curPotiSelIndex = 1;
		curSutraSelIndex = 0;
	}
	

	private void updateTables() {
		System.out.println("update");
		doingUpdate = true;
		updatePotiTable();
		int initSelect = curPotiSelIndex;
		if (potiTableViewer.getTable().getItemCount()<=initSelect) initSelect = 0;
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
			curPotiSelIndex = 1;
			curSutraSelIndex = 0;
			potiIndex = -1;
			sutraIndex = 1;
			sutraTableViewer.setInput(null);
			sutraTableViewer.refresh();
			idOfEntryToEdit = null;
			updateAction();
		} else {
			curPotiSelIndex = potiTableViewer.getTable().getSelectionIndex();
			potiIndex = ((Row)data).getValueAsInt();
			List<Row> sutraList = ctlr.getSutraList(potiIndex);
			int nextSutra = sutraList.get(0).getValueAsNode().get("sutraIndex").asInt()+1;
			sutraTableViewer.setInput(sutraList);
			sutraTableViewer.refresh();
			sutraTableViewer.insert(nextSutra, 0);
			int initSelect = 1;
			if (doingUpdate) initSelect = curSutraSelIndex;
			if (sutraTableViewer.getTable().getItemCount()<=initSelect) initSelect = sutraTableViewer.getTable().getItemCount()-1;
			sutraTableViewer.getTable().setSelection(initSelect);  // does NOT fire a select event
			onSutraSelect(sutraTableViewer.getTable().getItem(initSelect).getData());
		}
		titleCol.getColumn().pack();
	}
	
	private void onSutraSelect(Object data) {
		idOfEntryToCopy=null;
		curSutraSelIndex = sutraTableViewer.getTable().getSelectionIndex();
		if (isInt(data)) {
			sutraIndex = (int)data;
			if (sutraTableViewer.getTable().getItemCount()>1) {
				Object copyFromData = sutraTableViewer.getTable().getItem(1).getData();
				idOfEntryToCopy = ((Row)copyFromData).getValueAsNode().get("_id").asText();
				System.out.println("*** idOfEntryToEdit="+idOfEntryToEdit);
			}
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
		ctlr.doAction(potiIndex, sutraIndex, idOfEntryToEdit, idOfEntryToCopy);
	}
	
	
	protected boolean isInt(Object obj) {
		return obj.getClass().isAssignableFrom(Integer.class);
	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
