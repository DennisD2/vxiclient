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

public class HP1340Wrapper {
	private static Logger logger = LoggerFactory.getLogger(HP1340Wrapper.class);

	public static final int SERIAL_CONFIG = 1; // TODO -> global constants
												// config
	public static final int RPC_CONFIG = 2; // TODO -> global constants config

	private static Map<String, MainframeInfo> mainframes = new HashMap<String, MainframeInfo>();

	// Singleton
	private static HP1340Wrapper INSTANCE = null;

	private HP1340Wrapper() {
	}

	protected static void initialize(String name, String deviceName)
			throws Exception {
		if (mainframes.containsKey(key(name, deviceName))) {
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
		config = Configuration.findConfigByName(name);
		// get connector
		vxiConnector = VXIConnectorFactory.getConnector(config);
		// get device in that mainframe
		deviceId = config.getDeviceIdByName(deviceName);
		if (deviceId == null) {
			logger.error("Device not found: " + deviceName);
		}
		linkId = vxiConnector.initialize(config, deviceId);
		// initialize device class for the target
		device = new HP1340(vxiConnector, linkId);

		// Create new mainframe info object and add it to list
		MainframeInfo mf = new MainframeInfo(deviceId, config, linkId, device,
				vxiConnector);
		logger.debug("Created MFI entry with key {}",  key(name, deviceName));
		mainframes.put(key(name, deviceName), mf);

		device.initialize();
	}

	private static String key(String name, String deviceName) {
		return name + "." + deviceName;
	}

	/**
	 * Get wrapper instance for mainframe identified by 'name'.
	 * 
	 * @param name
	 *            mainframe name (as defined in property file
	 *            vxiserver.properties).
	 * @return
	 * @throws Exception
	 */
	public static HP1340Wrapper getInstance(String name, String deviceName)
			throws Exception {
		if (INSTANCE == null) {
			INSTANCE = new HP1340Wrapper();
		}
		initialize(name, deviceName);
		return INSTANCE;
	}

	public VXIConnector getConnector(String mainframe, String devname) {
		return mainframes.get(key(mainframe, devname)).getVxiConnector();
	}

	public DeviceLink getLink(String mainframe, String devname) {
		return mainframes.get(key(mainframe, devname)).getLinkId();
	}

	public HP1340 getDevice(String mainframe, String devname) {
		return mainframes.get(key(mainframe, devname)).getDevice();
	}

}
