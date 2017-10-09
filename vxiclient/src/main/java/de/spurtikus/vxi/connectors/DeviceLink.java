package de.spurtikus.vxi.connectors;

import de.spurtikus.rpcgen.Device_Link;
import de.spurtikus.vxi.connectors.serial.SerialConnector;
import gnu.io.SerialPort;

/**
 * Wrapper for link to device
 * 
 * @author dennis
 *
 */
public class DeviceLink {
	private Object wrapped;

	public DeviceLink(Device_Link link) {
		wrapped = link;
	}

	public DeviceLink(SerialPort port) {
		wrapped = port;
	}

	public Object getWrapped() {
		return wrapped;
	}
}
