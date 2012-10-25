package org.asianclassics.center.catalog;

import org.asianclassics.center.catalog.entry.EntryController;
import org.asianclassics.center.catalog.event.CatalogTaskMakeTopEvent;
import org.asianclassics.center.catalog.event.CatalogTaskMakeTopEvent.CatalogTaskViewType;
import org.eclipse.swt.widgets.Composite;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class SelectionView extends Composite {

	private EventBus eb;
	private Label lblSelectionView;
	private Button btnTest;

	public SelectionView(Composite parent, int style, Injector injector) {
		super(parent, style);
		
		lblSelectionView = new Label(this, SWT.NONE);
		lblSelectionView.setBounds(38, 23, 137, 15);
		lblSelectionView.setText("Selection View");
		
		btnTest = new Button(this, SWT.NONE);
		btnTest.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent e) {
				eb.post(new CatalogTaskMakeTopEvent(CatalogTaskViewType.ENTRY));
			}
		});
		btnTest.setBounds(46, 67, 75, 25);
		btnTest.setText("Show Entry");
		

		if (injector!=null) injector.injectMembers(this);
		
	}
	
	@Inject
	public void inject(EventBus eb) {
		this.eb = eb;
	}

	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
