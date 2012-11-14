package org.asianclassics.center.catalog;

import org.asianclassics.center.TaskView;
import org.asianclassics.center.catalog.event.CatalogTaskMakeTopEvent;
import org.asianclassics.center.catalog.event.EntryEditEvent;
import org.asianclassics.center.catalog.event.CatalogTaskMakeTopEvent.CatalogTaskViewType;
import org.asianclassics.center.config.AppConfig;
import org.asianclassics.center.event.LoginSuccessEvent;
import org.asianclassics.center.event.LogoutEvent;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;

import com.google.common.eventbus.EventBus;
import com.google.common.eventbus.Subscribe;
import com.google.inject.Inject;
import com.google.inject.Injector;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Control;

public class CatalogTaskView extends TaskView {
	private EventBus eb;
	private CatalogTaskStackView stack;
	private AppConfig cfg;

	
	public CatalogTaskView(Composite parent, int style, Injector injector) {
		super(parent, style);
		setLayout(new FormLayout());
		
		stack = new CatalogTaskStackView(this, SWT.NONE, injector);
		FormData fd_composite = new FormData();
		fd_composite.top = new FormAttachment(0, 0);
		fd_composite.left = new FormAttachment(0, 12);
		fd_composite.bottom = new FormAttachment(100, -12);
		fd_composite.right = new FormAttachment(100, 0);
		stack.setLayoutData(fd_composite);
		setTabList(new Control[]{stack});

		if (injector!=null) injector.injectMembers(this);
	}

	@Inject
	public void inject(EventBus eb, AppConfig cfg) {
		this.eb = eb;
		this.cfg = cfg;
	}

	@Subscribe
	public void onLoginSuccess(LoginSuccessEvent evt) {
		CatalogTaskViewType startingView = CatalogTaskViewType.SELECTION;
		if (cfg.get().catalogStartView!=null) {
			if (cfg.get().catalogStartView.equalsIgnoreCase("entry")) startingView = CatalogTaskViewType.ENTRY;
			else startingView = CatalogTaskViewType.TEST;
		}
		eb.post(new CatalogTaskMakeTopEvent(startingView));
		
		if (cfg.get().catalogInitModelId!=null) eb.post(new EntryEditEvent(null));
	}
	
	@Override
	protected void checkSubclass() {
		// Disable the check that prevents subclassing of SWT components
	}
}
