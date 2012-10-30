package org.asianclassics.center.catalog.scratch;


import org.asianclassics.center.catalog.entry.EntryView;
import org.asianclassics.center.link.LinkManager;
import org.eclipse.swt.widgets.Composite;

import com.google.common.eventbus.EventBus;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Text;


public class TestComposite extends Composite {

	private EventBus eb;
	private LinkManager lm;
	private Label lblTestcomposite;
	private Text text;

	/**
	 * @wbp.parser.constructor
	 */
	public TestComposite(Composite composite) {
		super(composite, SWT.NONE);
		init();
	}

	public TestComposite() {
		super(EntryView.getInstance(), SWT.NONE);
		Injector injector = null;
		
		if (injector!=null) injector.injectMembers(this);
	}
	
	public void init() {
		lblTestcomposite = new Label(this, SWT.NONE);
		lblTestcomposite.setBounds(10, 10, 244, 15);
		lblTestcomposite.setText("TestComposite");		
		
		text = new Text(this, SWT.BORDER);
		text.setBounds(20, 31, 76, 21);
	}
	
	@Inject
	public void inject(EventBus eb, LinkManager lm) {
		this.eb = eb;
		this.lm = lm;
	}

	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
