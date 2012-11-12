package org.asianclassics.center.catalog.entry.cell;

import java.text.DecimalFormat;
import org.asianclassics.center.catalog.event.EntryModelPostReadEvent;
import org.eclipse.swt.widgets.Composite;
import com.google.common.eventbus.Subscribe;

public class SizeEntryCell extends TextEntryCell {

	protected int number = -1;
	
	public SizeEntryCell(Composite parent, String title, int titleWidth, int simpleBoxWidth) {
		super(parent, title, titleWidth, BoxType.SIMPLE, simpleBoxWidth);
	}
	
	@Subscribe
	public void onPostRead(EntryModelPostReadEvent evt) {
		onModelToView();
	}
	
	@Override
	protected void formatDataForGui() {
		if (number==0) {
			data = "";
		} else {
			data = new DecimalFormat("#.#").format((double)number/10.0);			
		}
	}
	
	@Override
	protected void formatDataForModel() {
		if (data.isEmpty()) number=0;
		else {
			float flt = 0;
			try {
				flt = Float.parseFloat(data);			
			} catch (NumberFormatException e) {
			}
			number = Math.round(flt*10.0f);
		}
	}

	@Override
	protected void onValidate() {
		float flt = -1;
		try {
			flt = Float.parseFloat(text.getText());			
		} catch (NumberFormatException e) {
		}
		if (flt<0.1f) {
			ctlr.invalidate();
			setHilite(HiliteMode.INVALID);
		}
	}
}
