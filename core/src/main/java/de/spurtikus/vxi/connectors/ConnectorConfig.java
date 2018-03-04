package de.spurtikus.vxi.connectors;

import java.util.List;

import de.spurtikus.vxi.service.DeviceInfo;

/**
 * Connector Configuration Interface.
 * 
 * @author dennis
 *
 */
public interface ConnectorConfig {

	String getName();

	void setName(String name);

	int getId();

	void setId(int id);

	public boolean isEnabled();

	public void setEnabled(boolean enabled);

	/**
	 * Gets list of devices managed by this connector.
	 * 
	 * @return list of devices managed by this connector.
	 */
	public List<DeviceInfo> getDevices();

	/**
	 * Sets list of devices managed by this connector.
	 * 
	 * @param devices
	 *            list of devices managed by this connector.
	 */
	public void setDevices(List<DeviceInfo> devices);

	/**
	 * Get a device by its name.
	 * 
	 * @param name
	 *            device name.
	 * @return device.
	 */
	public String getDeviceIdByName(String name);

	/**
	 * Get a device type by device name.
	 * 
	 * @param name
	 *            device name.
	 * @return type.
	 */	String getDeviceTypeByName(String name);

}
