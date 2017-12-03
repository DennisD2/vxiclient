package de.spurtikus.vxi.service;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.spurtikus.devices.hp.HP1340;
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
	 * 
	 * @param mfName
	 *            mainframe name
	 * @param deviceName
	 *            device name
	 * @throws Exception
	 */
	protected static void initialize(String mfName, String deviceName)
			throws Exception {
		if (connections.containsKey(key(mfName, deviceName))) {
			// already in cache.
			return;
		}

		// Load configuration
		Configuration.load();

		ConnectorConfig config;
		VXIConnector vxiConnector;
		String deviceId;
		DeviceLink linkId;
		HP1340 device;

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
		// initialize device class for the target identified by (connector,
		// link)
		device = new HP1340(vxiConnector, linkId);

		// Create new connection info object and add it to list
		DeviceConnectionInfo mf = new DeviceConnectionInfo(deviceId, config,
				linkId, device, vxiConnector);
		logger.debug("Created DeviceConnectionInfo entry for key {}",
				key(mfName, deviceName));
		connections.put(key(mfName, deviceName), mf);

		// Initialize device
		device.initialize();
	}

	/**
	 * Creates map key from mainframe name and device name.
	 * 
	 * @param mfName
	 *            mainframe name
	 * @param deviceName
	 *            device name
	 * @return usable key
	 */
	private static String key(String mfName, String deviceName) {
		return mfName + "." + deviceName;
	}

	/**
	 * Gets wrapper instance for mainframe identified by 'name'.
	 * 
	 * @param name
	 *            mainframe name (as defined in property file
	 *            vxiserver.properties).
	 * @return
	 * @throws Exception
	 */
	public static ConnectionManager getInstance(String name, String deviceName)
			throws Exception {
		if (INSTANCE == null) {
			INSTANCE = new ConnectionManager();
		}
		initialize(name, deviceName);
		return INSTANCE;
	}

	public VXIConnector getConnector(String mainframe, String devname) {
		return connections.get(key(mainframe, devname)).getVxiConnector();
	}

	public DeviceLink getLink(String mainframe, String devname) {
		return connections.get(key(mainframe, devname)).getLinkId();
	}

	public HP1340 getDevice(String mainframe, String devname) {
		return connections.get(key(mainframe, devname)).getDevice();
	}

}
