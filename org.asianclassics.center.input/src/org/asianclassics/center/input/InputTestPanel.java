package org.asianclassics.center.input;

import java.util.logging.Logger;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Injector;

import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

public class InputTestPanel  extends Group {

	private Logger log;
	private EventBus eb;
	private InputTaskController itCon;


	public InputTestPanel(Composite parent, Injector injector) {
		super(parent, SWT.NONE);
		setText("TEST");
		
		Button btnInputNoErrors = new Button(this, SWT.NONE);
		btnInputNoErrors.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				itCon.testInput(InputTest.ErrorType.NONE);
			}
		});
		btnInputNoErrors.setBounds(10, 21, 133, 25);
		btnInputNoErrors.setText("Input With No Errors");
		
		Button btnInputWithObvious = new Button(this, SWT.NONE);
		btnInputWithObvious.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				itCon.testInput(InputTest.ErrorType.OBVIOUS);
			}
		});
		btnInputWithObvious.setText("Input With Obvious Errors");
		btnInputWithObvious.setBounds(149, 21, 163, 25);
		
		Button btnInputWithSimple = new Button(this, SWT.NONE);
		btnInputWithSimple.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				itCon.testInput(InputTest.ErrorType.SIMPLE);
			}
		});
		btnInputWithSimple.setText("Input With Simple Errors");
		btnInputWithSimple.setBounds(318, 21, 156, 25);
		
		Button btnInputWithComplex = new Button(this, SWT.NONE);
		btnInputWithComplex.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				itCon.testInput(InputTest.ErrorType.COMPLEX);
			}
		});
		btnInputWithComplex.setText("Input With Complex Errors");
		btnInputWithComplex.setBounds(480, 21, 156, 25);
		
		Button btnSave = new Button(this, SWT.NONE);
		btnSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				itCon.save();
			}
		});
		btnSave.setBounds(732, 21, 75, 25);
		btnSave.setText("Save");
		
		if (injector!=null) injector.injectMembers(this);
	}
	
	@Inject
	public void inject(Logger log, EventBus eb, InputTaskController itCon) {
		this.log = log;
		this.eb = eb;
		this.itCon = itCon;
	}

	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
