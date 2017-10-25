package de.spurtikus.vxi.connectors;

import java.util.Map;

/**
 * Connector Configuration Interface.
 * 
 * @author dennis
 *
 */
public interface ConnectorConfig {

	int getId();

	void setId(int id);

	public boolean isEnabled();

	public void setEnabled(boolean enabled);

	/**
	 * Gets list of devices managed by this connector.
	 * 
	 * @return list of devices managed by this connector.
	 */
	public Map<String, String> getDevices();

	/**
	 * Sets list of devices managed by this connector.
	 * 
	 * @param devices
	 *            list of devices managed by this connector.
	 */
	public void setDevices(Map<String, String> devices);

	/**
	 * Get a device by its name.
	 * 
	 * @param name
	 *            device name.
	 * @return device.
	 */
	public String getDeviceIdByName(String name);

}
