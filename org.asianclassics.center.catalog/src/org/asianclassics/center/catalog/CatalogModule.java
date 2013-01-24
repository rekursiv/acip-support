package org.asianclassics.center.catalog;


import org.asianclassics.center.CenterConfig;
import org.asianclassics.center.CenterModule;

import util.config.ConfigManager;

import com.google.common.eventbus.EventBus;

public class CatalogModule extends CenterModule {

	public CatalogModule() {
		filePrefix = "acipcatalog";
		cfgMgr = new ConfigManager<CatalogConfig>(CatalogConfig.class, filePrefix+"-config.js");
		eventBus = new EventBus();
	}
	
	@Override
	protected void setupConfig() {
		config = (CatalogConfig) cfgMgr.load();
		bind(CatalogConfig.class).toInstance((CatalogConfig)config);
		bind(CenterConfig.class).toInstance(config);
	}
	
}
