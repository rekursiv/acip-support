package org.asianclassics.test.taskdb;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import org.asianclassics.center.input.db.InputTask;
import org.asianclassics.center.input.db.InputTaskRepo;
import org.asianclassics.center.input.db.Source;
import org.asianclassics.center.input.db.SourceRepo;
import org.asianclassics.database.CustomCouchDbConnector;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.wb.swt.layout.grouplayout.GroupLayout;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Button;
import org.ektorp.AttachmentInputStream;
import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.DbPath;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbInstance;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class DbSetup extends Composite {
	
//	private static final String dbName = "acip-center-input-tasks";
	private static final String dbName = "acip-center-test-tasks";
	
	private CouchDbConnector db = null;
	private CouchDbInstance couch = null;
	private InputTaskRepo taskRepo = null;
	private SourceRepo srcRepo = null;
	
	private Table table;
	private Label lblDbStatus;
	
	private List<String> docIds = null;
	
	private boolean dbExists = false;

	
	private int pageIndex = 1;
	
	

	public DbSetup(Composite parent, int style) {
		super(parent, style);
		
		table = new Table(this, SWT.BORDER | SWT.FULL_SELECTION | SWT.VIRTUAL);
		table.setHeaderVisible(true);
		table.setLinesVisible(true);
		
		lblDbStatus = new Label(this, SWT.NONE);
		lblDbStatus.setText("DB status");
		
		Button btnUpdate = new Button(this, SWT.NONE);
		btnUpdate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				uiUpdate();
			}
		});
		btnUpdate.setText("Update UI");
		
		Button btnDelete = new Button(this, SWT.NONE);
		btnDelete.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				deleteDb();
			}
		});
		btnDelete.setText("Delete");
		
		Button btnCreate = new Button(this, SWT.NONE);
		btnCreate.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				createDb();
			}
		});
		btnCreate.setText("Create");
		
		Button btnSetup = new Button(this, SWT.NONE);
		btnSetup.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				setupDb();
			}
		});
		btnSetup.setText("Setup");
		GroupLayout groupLayout = new GroupLayout(this);
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(GroupLayout.LEADING)
				.add(GroupLayout.TRAILING, groupLayout.createSequentialGroup()
					.addContainerGap()
					.add(groupLayout.createParallelGroup(GroupLayout.TRAILING)
						.add(GroupLayout.LEADING, table, GroupLayout.DEFAULT_SIZE, 571, Short.MAX_VALUE)
						.add(GroupLayout.LEADING, groupLayout.createSequentialGroup()
							.add(btnUpdate)
							.add(77)
							.add(btnCreate)
							.add(18)
							.add(btnSetup)
							.add(18)
							.add(btnDelete))
						.add(lblDbStatus, GroupLayout.DEFAULT_SIZE, 571, Short.MAX_VALUE))
					.addContainerGap())
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(GroupLayout.TRAILING)
				.add(GroupLayout.LEADING, groupLayout.createSequentialGroup()
					.addContainerGap()
					.add(groupLayout.createParallelGroup(GroupLayout.BASELINE)
						.add(btnUpdate)
						.add(btnCreate)
						.add(btnSetup)
						.add(btnDelete))
					.add(18)
					.add(lblDbStatus)
					.add(18)
					.add(table, GroupLayout.DEFAULT_SIZE, 344, Short.MAX_VALUE)
					.addContainerGap())
		);
		setLayout(groupLayout);
		

		initDb();
		initTable();
		
		uiUpdate();

	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
	
	private void initTable() {
		final TableColumn tc1 = new TableColumn(table, SWT.LEFT);
		tc1.setText("ID");
		tc1.setWidth(120);
		final TableColumn tc2 = new TableColumn(table, SWT.LEFT);
		tc2.setText("Type");
		tc2.setWidth(80);
		final TableColumn tc3 = new TableColumn(table, SWT.LEFT);
		tc3.setText("Active");
		tc3.setWidth(80);
		final TableColumn tc4 = new TableColumn(table, SWT.LEFT);
		tc4.setText("Worker");
		tc4.setWidth(80);
		final TableColumn tc5 = new TableColumn(table, SWT.LEFT);
		tc5.setText("Text");
		tc5.setWidth(120);
		
	    table.addListener (SWT.SetData, new Listener () {
	        public void handleEvent (Event event) {
	        	if (docIds!=null && dbExists) {
	        		TableItem item = (TableItem) event.item;
	        		int index = table.indexOf(item);
	        		Map<String, Object> row = db.get(Map.class, docIds.get(index));
	        		item.setText(0, getStringFromRow("_id", row));
	        		item.setText(1, getStringFromRow("type", row));
	        		item.setText(2, getStringFromRow("active", row));
	        		item.setText(3, getStringFromRow("worker", row));
	        		item.setText(4, getStringFromRow("text", row));
	        	}
	        }
	    }); 
	}
	
	
	private String getStringFromRow(String key, Map<String, Object> row) {
		Object obj = row.get(key);
		if (obj!=null) return obj.toString();
		else return "";
	}
	
	private void initDb() {
		HttpClient httpClient = new StdHttpClient.Builder().build();
		couch = new StdCouchDbInstance(httpClient);
		db = new CustomCouchDbConnector(dbName, couch);
	}
	
	private void uiUpdate() {
		statusUpdate();
		tableUpdate();
	}
	
	private void statusUpdate() {
		String status;
		if (couch.checkIfDbExists(new DbPath(dbName))) {
			status = "Database '"+dbName+"' exists.";
			dbExists = true;
		} else {
			status = "Database '"+dbName+"' does not exist.";
			dbExists = false;
		}
		lblDbStatus.setText(status);
	}
	
	private void tableUpdate() {
		table.clearAll();
		if (dbExists) {
			docIds = db.getAllDocIds();
			table.setItemCount(docIds.size());
		} else {
			table.setItemCount(0);
			docIds = null;
		}
	}

	private void createDb() {
		if (!dbExists) {
			couch.createDatabase(dbName);
			uiUpdate();
		}
	}
	
	private void deleteDb() {
		if (dbExists) {
			couch.deleteDatabase(dbName);
			uiUpdate();
		}
	}
	
	private void setupDb() {
		pageIndex = 1;
		if (dbExists && db.getAllDocIds().isEmpty()) {
			taskRepo = new InputTaskRepo(db);
			taskRepo.initStandardDesignDocument();
			srcRepo = new SourceRepo(db);
			
			initPage();  ///
			initPage();
			
//			initPage();  ///
//			initPage();
			
//			initPage();  ///
//			initPage();
			
//			initPage();  ///
//			initPage();
			
			uiUpdate();
		}
	}
	
	public void initPage() {
		Source src = new Source();
//		s.setText(text);
		src.setPageIndex(pageIndex);
		srcRepo.add(src);
		attachImage(src);
		InputTask it = new InputTask();
		it.linkWithSource(src);
		it.setActive(true);
		taskRepo.add(it);
		++pageIndex;
	}
	
	
	public void attachImage(Source src) {
		Path path = Paths.get("C:/projects/ACIP/input_test_sample/tib_test/"+pageIndex+".png");
//		Path path = Paths.get("/home/acip/input_test_sample/tib_test/"+pageIndex+".png");
		
		InputStream is = null;
		try {
			is = Files.newInputStream(path);
			AttachmentInputStream ais = new AttachmentInputStream("img.png", is, "image/png");
			db.createAttachment(src.getId(), src.getRevision(), ais);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
}
