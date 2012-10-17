package org.asianclassics.center.catalog.entry;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;


public class HeightAdaptingText extends StyledText {

	public HeightAdaptingText(Composite parent, int style) {
		super(parent, style);
		
		addListener(SWT.Modify, new Listener() {
			public void handleEvent(Event event) {
				Point curSize = getSize();
				Point newSize = computeSize(curSize.x, SWT.DEFAULT);
				if (newSize.y != curSize.y) {
					setSize(newSize);
				}
			}
		});

	}

}
