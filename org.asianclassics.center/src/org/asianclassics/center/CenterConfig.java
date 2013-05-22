package org.asianclassics.center;

import util.config.ConfigBase;


public class CenterConfig extends ConfigBase {
	
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
	public String dbCenterCode;
	public String directLinkServerIp;

}

