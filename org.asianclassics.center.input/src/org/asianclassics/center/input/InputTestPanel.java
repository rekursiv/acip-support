package org.asianclassics.center.input;

import java.util.logging.Logger;

import org.asianclassics.center.event.LoginRequestEvent;
import org.asianclassics.center.event.LogoutEvent;
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
		btnInputNoErrors.setBounds(10, 21, 170, 25);
		btnInputNoErrors.setText("Input With No Errors");
		
		Button btnInputWithObvious = new Button(this, SWT.NONE);
		btnInputWithObvious.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				itCon.testInput(InputTest.ErrorType.OBVIOUS);
			}
		});
		btnInputWithObvious.setText("Input With Obvious Errors");
		btnInputWithObvious.setBounds(186, 21, 193, 25);
		
		Button btnInputWithSimple = new Button(this, SWT.NONE);
		btnInputWithSimple.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				itCon.testInput(InputTest.ErrorType.SIMPLE);
			}
		});
		btnInputWithSimple.setText("Input With Simple Errors");
		btnInputWithSimple.setBounds(385, 21, 185, 25);
		
		Button btnInputWithComplex = new Button(this, SWT.NONE);
		btnInputWithComplex.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				itCon.testInput(InputTest.ErrorType.COMPLEX);
			}
		});
		btnInputWithComplex.setText("Input With Complex Errors");
		btnInputWithComplex.setBounds(576, 21, 193, 25);
		
		Button btnSave = new Button(this, SWT.NONE);
		btnSave.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				itCon.save();
			}
		});
		btnSave.setBounds(837, 21, 75, 25);
		btnSave.setText("Save");
		
		Button btnTest = new Button(this, SWT.NONE);
		btnTest.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				itCon.test();
			}
		});
		btnTest.setBounds(935, 21, 75, 25);
		btnTest.setText("TEST");
		
		Button btnA = new Button(this, SWT.NONE);
		btnA.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				switchUser("A");
			}
		});
		btnA.setBounds(1067, 21, 25, 25);
		btnA.setText("A");
		
		Button btnNewButton = new Button(this, SWT.NONE);
		btnNewButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				switchUser("B");
			}
		});
		btnNewButton.setBounds(1098, 21, 25, 25);
		btnNewButton.setText("B");
		
		Button btnC = new Button(this, SWT.NONE);
		btnC.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				switchUser("C");
			}
		});
		btnC.setBounds(1129, 21, 25, 25);
		btnC.setText("C");
		
		Button btnD = new Button(this, SWT.NONE);
		btnD.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(SelectionEvent arg0) {
				switchUser("D");
			}
		});
		btnD.setBounds(1160, 21, 25, 25);
		btnD.setText("D");
		
		if (injector!=null) injector.injectMembers(this);
	}
	
	@Inject
	public void inject(Logger log, EventBus eb, InputTaskController itCon) {
		this.log = log;
		this.eb = eb;
		this.itCon = itCon;
	}

	private void switchUser(String newUser) {
		eb.post(new LogoutEvent());
		eb.post(new LoginRequestEvent(newUser));
	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
