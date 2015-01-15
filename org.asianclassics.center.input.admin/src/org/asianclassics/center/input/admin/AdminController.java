package org.asianclassics.center.input.admin;

import java.util.logging.Logger;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class AdminController {

	private Logger log;

	@Inject
	public AdminController(Logger log) {
		this.log = log;
	}

	public void test() {
		// TODO Auto-generated method stub
		
	}
	
}
