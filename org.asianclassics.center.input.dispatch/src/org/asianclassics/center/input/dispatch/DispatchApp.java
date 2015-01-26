package org.asianclassics.center.input.dispatch;

public class DispatchApp {

	public static void main(String[] args) {
		DispatchApp instance = new DispatchApp();
		instance.test();
	}

	
	private void test() {
		DispatchManager dm = new DispatchManager();
		dm.dispatch(2);
	}

}
