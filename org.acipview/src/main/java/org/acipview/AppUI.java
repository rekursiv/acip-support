package org.acipview;

import java.awt.Color;
import java.util.Date;
import java.util.List;

import org.ektorp.ViewResult.Row;
import org.joda.time.DateTime;

import com.vaadin.addon.timeline.Timeline;
import com.vaadin.addon.timeline.Timeline.ChartMode;
import com.vaadin.data.Container;
import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;
import com.vaadin.server.ClassResource;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.Link;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;



@SuppressWarnings("serial")
public class AppUI extends UI
{

    @Override
    protected void init(VaadinRequest request) {
        final VerticalLayout layout = new VerticalLayout();
        layout.setMargin(true);
        setContent(layout);
        
        Link acipLogo = new Link(null, new ExternalResource("http://asianclassics.org/"));
        acipLogo.setIcon(new ClassResource("acipLogo.png"));
//        acipIcon.setWidth("");
//        acipIcon.setHeight("");        
        layout.addComponent(acipLogo);
        
        Timeline timeline = new Timeline("Texts Cataloged at the National Library of Mongolia");
		timeline.setWidth("100%");
		timeline.setUniformBarThicknessEnabled(true);
		timeline.setChartMode(ChartMode.BAR);
		
		Container.Indexed graph = getDates();
		timeline.addGraphDataSource(graph, 
                Timeline.PropertyId.TIMESTAMP,
                Timeline.PropertyId.VALUE);
		
	
		layout.addComponent(timeline);
		
		timeline.setGraphCaption(graph, "Number of Texts Cataloged Per Day");

		timeline.setGraphOutlineColor(graph, new Color(121, 35, 37, 255));
		timeline.setGraphFillColor(graph, new Color(121, 35, 37, 100));
		timeline.setBrowserOutlineColor(graph, new Color(169, 154, 111, 255));
		timeline.setBrowserFillColor(graph, new Color(169, 154, 111, 100));
		
		timeline.addZoomLevel("Week", 7 * 86400000L);
		timeline.addZoomLevel("Month", 2629743830L);
		timeline.addZoomLevel("Year", (long) 3.15569e10);
    }
    
    
    
	public Container.Indexed getDates() {

		LinkManager lm = new LinkManager();
		
		List<Row> rows = lm.getDates();
		
		Container.Indexed container = new IndexedContainer();
	    container.addContainerProperty(Timeline.PropertyId.TIMESTAMP, Date.class, null);
	    container.addContainerProperty(Timeline.PropertyId.VALUE, Integer.class, null);
		
		for (Row row: rows) {
//			System.out.println(row.getKey()+" : "+row.getValue());
//			System.out.println(new DateTime(row.getKey()).toDate().toString()+" : "+row.getValue());				
			
			Date date = new DateTime(row.getKey()).toDate();
			Item item = container.addItem(date);
			item.getItemProperty(Timeline.PropertyId.TIMESTAMP).setValue(date);
			item.getItemProperty(Timeline.PropertyId.VALUE).setValue(row.getValueAsInt());
		}

		lm.destroy();
		
		return container;
	}
}
