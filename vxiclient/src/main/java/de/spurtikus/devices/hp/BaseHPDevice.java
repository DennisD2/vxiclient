package de.spurtikus.devices.hp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BaseHPDevice {
	static Logger logger = LoggerFactory.getLogger("RPCConnector");
	
	protected void sleep(int ms) {
		// sleep 100ms - allow interrupts (inside E1340) to be serviced
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public byte[] asByteArray(String s) {
		byte[] data = new byte[s.length()];
		for (int i = 0; i < s.length(); i++) {
			data[i] = (byte) s.charAt(i);
		}
		return data;
	}

	public String byteArrayAsString(byte[] b) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < b.length && b[i] != 0x00; i++) {
			// log.fine((char)b[i]);
			sb.append((char) b[i]);
		}
		return sb.toString();
	}

	public String byteArrayAsString(byte[] b, int len) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < len && i < b.length && b[i] != 0x00; i++) {
			logger.trace(""+(char)b[i]);
			sb.append((char) b[i]);
		}
		return sb.toString();
	}

}
