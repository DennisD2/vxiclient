package de.spurtikus.vxi.connectors;

import de.spurtikus.rpcgen.Device_Link;

/**
 * Wrapper for link to device.
 * 
 * @author dennis
 *
 */
public class DeviceLink {
	private Object wrapped;

	public DeviceLink(Device_Link link) {
		// For RPC connectors
		wrapped = link;
	}

	public DeviceLink(ConnectorConfig port) {
		// For pure serial connectors
		wrapped = port;
	}
	
	public DeviceLink(GPIBDeviceLink port) {
		// For GPIB serial serial connectors
		wrapped = port;
	}

	public Object getWrapped() {
		return wrapped;
	}
}
