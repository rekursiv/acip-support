package org.asianclassics.center.config;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CenterAppConfigModel {
	
	// meta
	public boolean useDefaults;  // TODO:  reconsider usefulness of this field
	public String saveToFile;
	public String loadFromFile;
	
	// logging
	public boolean logToConsole;
	public boolean logToFile=true;
	public boolean showLogView;
	
	// shell
	public boolean allowCloseWhileLoggedIn;
	public boolean openMaximized=true;

	// link
	public String dbOrgPrefix="acip-center-";
	public boolean testDirectLink;
	public boolean autoUpdateCouchViews;
	public String autoLoginId;
	
	//////////////////////////////
	
	// catalog
	public String catalogStartView;
	public String catalogInitModelId;
	public boolean catalogAddEntryDebugBtns;
	public boolean catalogDisableAutosave;

}

