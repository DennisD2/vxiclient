package de.spurtikus.vxi.connectors;

import java.util.ArrayList;
import java.util.List;

import de.spurtikus.vxi.service.DeviceInfo;

public abstract class AbstractConnectorConfig implements ConnectorConfig {
	private String name;
	private int id = 0;
	private boolean enabled = true;
	private List<DeviceInfo> devices = new ArrayList<DeviceInfo>();

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public void setId(int id) {
		this.id = id;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	@Override
	public String toString() {
		return "ID:" + id + (enabled ? "(" : "(not ") + "enabled):";
	}

	@Override
	public List<DeviceInfo> getDevices() {
		return devices;
	}

	@Override
	public void setDevices(List<DeviceInfo> devices) {
		this.devices = devices;
	}

	@Override
	public String getDeviceIdByName(String name) {
		return devices.stream().filter(d -> d.getName().equals(name)).findAny().get().getAddress();
	}
}
