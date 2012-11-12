package org.asianclassics.center.link;

import org.jgroups.Address;
import org.jgroups.stack.AddressGenerator;
import org.jgroups.util.PayloadUUID;

public class PayloadAddressGenerator implements AddressGenerator {

	private String payload;
	
	public PayloadAddressGenerator(String payload) {
		this.payload = payload;
	}

	@Override
	public Address generateAddress() {
		return PayloadUUID.randomUUID(payload);
	}


}
