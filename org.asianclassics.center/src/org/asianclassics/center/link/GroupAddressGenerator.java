package org.asianclassics.center.link;

import org.jgroups.Address;
import org.jgroups.stack.AddressGenerator;
import org.jgroups.util.PayloadUUID;

public class GroupAddressGenerator implements AddressGenerator {

	private String ip;
	
	public GroupAddressGenerator(String ip) {
		this.ip = ip;
	}

	@Override
	public Address generateAddress() {
		return PayloadUUID.randomUUID(ip);
	}


}
