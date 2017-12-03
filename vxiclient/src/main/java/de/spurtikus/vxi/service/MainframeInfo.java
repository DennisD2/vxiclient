package de.spurtikus.vxi.service;

import de.spurtikus.devices.hp.HP1340;
import de.spurtikus.vxi.connectors.ConnectorConfig;
import de.spurtikus.vxi.connectors.DeviceLink;
import de.spurtikus.vxi.connectors.VXIConnector;

/**
 * Info about a mainframe.
 * 
 * @author dennis
 *
 */
public class MainframeInfo {
	private String deviceId;
	private ConnectorConfig config;
	private DeviceLink linkId;
	private HP1340 device;
	private VXIConnector vxiConnector;

	public MainframeInfo(String deviceId, ConnectorConfig config,
			DeviceLink linkId, HP1340 device, VXIConnector vxiConnector) {
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

	public HP1340 getDevice() {
		return device;
	}

	public VXIConnector getVxiConnector() {
		return vxiConnector;
	}


}
