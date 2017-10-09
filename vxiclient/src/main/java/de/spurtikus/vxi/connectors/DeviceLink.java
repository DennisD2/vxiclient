package de.spurtikus.vxi.connectors;

import de.spurtikus.rpcgen.Device_Link;
import de.spurtikus.vxi.connectors.serial.SerialConnector;

/**
 * Wrapper for link to device
 * 
 * @author dennis
 *
 */
public class DeviceLink {
	private Device_Link wrapped;

	public DeviceLink(Device_Link link) {
		wrapped = link;
	}

	public DeviceLink(SerialConnector serialConnector) {
		// TODO: assign to what? Object?
	}

	public Device_Link getWrapped() {
		return wrapped;
	}
}
