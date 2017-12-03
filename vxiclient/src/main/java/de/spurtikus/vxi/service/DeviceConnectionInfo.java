package de.spurtikus.vxi.service;

import de.spurtikus.devices.hp.BaseHPDevice;
import de.spurtikus.devices.hp.HP1340;
import de.spurtikus.vxi.connectors.ConnectorConfig;
import de.spurtikus.vxi.connectors.DeviceLink;
import de.spurtikus.vxi.connectors.VXIConnector;

/**
 * Info about a connection.
 * 
 * @author dennis
 *
 */
public class DeviceConnectionInfo {
	private String deviceId;
	private ConnectorConfig config;
	private DeviceLink linkId;
	private BaseHPDevice device;
	private VXIConnector vxiConnector;

	public DeviceConnectionInfo(String deviceId, ConnectorConfig config,
			DeviceLink linkId, BaseHPDevice device, VXIConnector vxiConnector) {
		this.deviceId = deviceId;
		this.config = config;
		this.linkId = linkId;
		this.device = device;
		this.vxiConnector = vxiConnector;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public ConnectorConfig getConfig() {
		return config;
	}

	public DeviceLink getLinkId() {
		return linkId;
	}

	public BaseHPDevice getDevice() {
		return device;
	}

	public VXIConnector getVxiConnector() {
		return vxiConnector;
	}


}
