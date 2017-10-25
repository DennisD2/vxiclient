package de.spurtikus.vxi.connectors;

import java.util.HashMap;
import java.util.Map;

public abstract class AbstractConnectorConfig implements ConnectorConfig {
	private int id = 0;
	private boolean enabled = true;

	private Map<String, String> devices = new HashMap<String, String>();

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
		return "ID:" + id + (enabled?"(":"(not ") + "enabled):";
	}

	@Override
	public Map<String, String> getDevices() {
		return devices;
	}

	@Override
	public void setDevices(Map<String, String> devices) {
		this.devices = devices;
	}
	
	@Override
	public String getDeviceIdByName(String name) {
		return devices.get(name);
	}
}
