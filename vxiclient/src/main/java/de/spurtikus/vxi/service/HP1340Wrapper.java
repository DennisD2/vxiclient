package de.spurtikus.vxi.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.spurtikus.devices.hp.HP1340;
import de.spurtikus.vxi.connectors.ConnectorConfig;
import de.spurtikus.vxi.connectors.DeviceLink;
import de.spurtikus.vxi.connectors.VXIConnector;
import de.spurtikus.vxi.connectors.VXIConnectorFactory;

public class HP1340Wrapper {
	private static Logger logger = LoggerFactory.getLogger(HP1340Wrapper.class);

	public static final int SERIAL_CONFIG = 1; // TODO -> global constants config
	public static final int RPC_CONFIG = 2; // TODO -> global constants config
	public static final String TEST_DEVICE_NAME = "hp1340";
	
	private static String deviceId;
	private static ConnectorConfig config;
	private static DeviceLink linkId;
	private static HP1340 device;
	private static VXIConnector vxiConnector;
	
	// Singleton
	private static HP1340Wrapper INSTANCE = null;
	private HP1340Wrapper() {}

	protected static void initialize() throws Exception {
		// Load configuration
		Configuration.load();
		// We assume usable config at some index
		config = Configuration.findConfigById(SERIAL_CONFIG);

		vxiConnector = VXIConnectorFactory.getConnector(config);

		deviceId = config.getDeviceIdByName(TEST_DEVICE_NAME);
		if (deviceId == null) {
			logger.error("Device not found: " + TEST_DEVICE_NAME);
		}
		linkId = vxiConnector.initialize(config, deviceId);
		device = new HP1340(vxiConnector, linkId);

		device.initialize();
	}

	public static HP1340Wrapper getInstance() throws Exception {
		if (INSTANCE==null) {
			INSTANCE = new HP1340Wrapper();
			initialize();
		}
		return INSTANCE;
	}

	public VXIConnector getConnector() {
		return vxiConnector;
	}

	public DeviceLink getLink() {
		return linkId;
	}

	public HP1340 getDevice() {
		return device;
	}

}
