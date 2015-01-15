package org.asianclassics.center.input.db;

import java.nio.ByteBuffer;
import java.security.SecureRandom;
import java.util.Date;

public class Id {
	
	private static final boolean addExtraRandom = true;
	
	private static short idSequence=0;

	private static String toHexString(byte[] bytes) {
	    char[] hexArray = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
	    char[] hexChars = new char[bytes.length<<1];
	    int v;
	    for (int j=0; j<bytes.length; j++) {
	        v = bytes[j]&0xFF;
	        hexChars[j<<1] = hexArray[v>>>4];
	        hexChars[(j<<1)+1] = hexArray[v&0x0F];
	    }
	    return new String(hexChars);
	}
	
	public static void reset() {
		idSequence = 0;
	}
	
	public static String gen() {
		
		String time = Long.toHexString(new Date().getTime());
	
		ByteBuffer seqBuf = ByteBuffer.allocate(2);
		seqBuf.putShort(0, idSequence);
		String seq = toHexString(seqBuf.array());
		++idSequence;
		
		if (addExtraRandom) {
			SecureRandom rndGen = new SecureRandom();
			byte rndBytes[] = new byte[2];
			rndGen.nextBytes(rndBytes);
			String rnd = toHexString(rndBytes);
			return time+seq+rnd;
		} else {
			return time+seq;
		}
		
	}

}