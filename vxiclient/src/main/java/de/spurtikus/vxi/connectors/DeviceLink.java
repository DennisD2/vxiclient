package de.spurtikus.vxi.connectors;

import de.spurtikus.rpcgen.Device_Link;

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

	public DeviceLink(ConnectorConfig port) {
		wrapped = port;
	}

	public Object getWrapped() {
		return wrapped;
	}
}
