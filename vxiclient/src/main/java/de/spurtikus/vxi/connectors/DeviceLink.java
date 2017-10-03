package de.spurtikus.vxi.connectors;

import de.spurtikus.rpcgen.Device_Link;

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

	public Device_Link getWrapped() {
		return wrapped;
	}
}
