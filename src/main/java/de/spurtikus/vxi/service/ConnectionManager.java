package de.spurtikus.vxi.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.spurtikus.devices.hp.BaseHPDevice;
import de.spurtikus.devices.hp.DeviceFactory;
import de.spurtikus.vxi.connectors.ConnectorConfig;
import de.spurtikus.vxi.connectors.DeviceLink;
import de.spurtikus.vxi.connectors.VXIConnector;
import de.spurtikus.vxi.connectors.VXIConnectorFactory;

/**
 * Connection Manager for all device connections. Connections are cached and
 * re-used.
 * 
 * @author dennis
 *
 */
public class ConnectionManager {
	private static Logger logger = LoggerFactory
			.getLogger(ConnectionManager.class);

	// Map of all connections to devices (mainframe,device) -> info object
	private static Map<String, DeviceConnectionInfo> connections = new HashMap<String, DeviceConnectionInfo>();

	// Singleton
	private static ConnectionManager INSTANCE = null;

	private ConnectionManager() {
	}

	/**
	 * Initializes a connection to a mainframe and a device. Connections are
	 * cached and re-used.
	 * @param deviceClass 
	 * 
	 * @param mfName
	 *            mainframe name
	 * @param deviceName
	 *            device name
	 * @throws Exception
	 */
	protected static void initialize(Class<? extends AbstractBoundary<?>> deviceClass, String mfName, String deviceName)
			throws Exception {
		if (connections.containsKey(key(deviceClass, mfName, deviceName))) {
			// already in cache.
			return;
		}

		// Load configuration
		Configuration.load();

		ConnectorConfig config;
		VXIConnector vxiConnector;
		String deviceId;
		DeviceLink linkId;
		BaseHPDevice device;

		// Find config for that mainframe
		config = Configuration.findConfigByName(mfName);
		// get connector for that mainframe
		vxiConnector = VXIConnectorFactory.getConnector(config);
		// get device in that mainframe
		deviceId = config.getDeviceIdByName(deviceName);
		if (deviceId == null) {
			logger.error("Device not found: " + deviceName);
		}
		// Create device link
		linkId = vxiConnector.initialize(config, deviceId);
		// initialize device class for the target identified by (connector,link)
		device = DeviceFactory.create(deviceClass, vxiConnector, linkId);

		// Create new connection info object and add it to list
		DeviceConnectionInfo mf = new DeviceConnectionInfo(deviceId, config,
				linkId, device, vxiConnector);
		logger.debug("Created DeviceConnectionInfo entry for key {}",
				key(deviceClass, mfName, deviceName));
		connections.put(key(deviceClass, mfName, deviceName), mf);

		// Initialize device
		device.initialize();
	}

	/**
	 * Creates map key from mainframe name and device class and name.
	 * 
	 * @param deviceClass
	 *            device class
	 * @param mfName
	 *            mainframe name
	 * @param deviceName
	 *            device name
	 * @return usable key
	 */
	private static String key(Class<? extends AbstractBoundary<?>> deviceClass,
			String mfName, String deviceName) {
		StringBuilder sb = new StringBuilder();
		sb.append(mfName);
		sb.append(".");
		sb.append(deviceClass.getSimpleName());
		sb.append(".");
		sb.append(deviceName);
		return sb.toString();
	}

	/**
	 * Gets wrapper instance for mainframe identified by 'name'.
	 * @param deviceClass 
	 * 
	 * @param mfName
	 *            mainframe name (as defined in property file
	 *            vxiserver.properties).
	 * @return
	 * @throws Exception
	 */
	public static ConnectionManager getInstance(Class<? extends AbstractBoundary<?>> deviceClass, String mfName, String deviceName)
			throws Exception {
		if (INSTANCE == null) {
			INSTANCE = new ConnectionManager();
		}
		initialize(deviceClass, mfName, deviceName);
		return INSTANCE;
	}

	public VXIConnector getConnector(Class<? extends AbstractBoundary<?>> deviceClass, String mainframe,
			String devname) {
		return connections.get(key(deviceClass, mainframe, devname))
				.getVxiConnector();
	}

	public DeviceLink getLink(Class<? extends AbstractBoundary<?>> deviceClass,
			String mainframe, String devname) {
		return connections.get(key(deviceClass, mainframe, devname))
				.getLinkId();
	}

	public BaseHPDevice getDevice(Class<? extends AbstractBoundary<?>> deviceClass,
			String mainframe, String devname) {
		return connections.get(key(deviceClass, mainframe, devname))
				.getDevice();
	}

}
