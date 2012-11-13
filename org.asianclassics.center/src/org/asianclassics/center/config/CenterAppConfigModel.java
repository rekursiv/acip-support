package org.asianclassics.center.config;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CenterAppConfigModel {
	
	// meta
	public boolean useDefaults=false;
	public String saveToFile;
	public String loadFromFile;
	
	// logging
	public boolean logToConsole=false;
	public boolean logToFile=true;
	public boolean showLogView=false;
	
	// shell
	public boolean allowCloseWhileLoggedIn=false;

	// link
	public String dbOrgPrefix="acip-center-";
	public boolean testDirectLink=false;	
	public boolean autoUpdateCouchViews=false;	
	
	
	
	
	@JsonIgnore
	public void setDebugConfig() {  //  FIXME  put into file instead
		logToConsole=true;
		logToFile=false;
		showLogView=true;
		allowCloseWhileLoggedIn=true;
		autoUpdateCouchViews=true;
		testDirectLink=true;
	}
}
