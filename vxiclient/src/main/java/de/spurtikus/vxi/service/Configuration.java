package de.spurtikus.vxi.service;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.spurtikus.vxi.connectors.ConnectorConfig;
import de.spurtikus.vxi.connectors.rpc.RPCConnectorConfig;
import de.spurtikus.vxi.connectors.serial.GPIBSerialConnectorConfig;
import de.spurtikus.vxi.connectors.serial.SerialConnectorConfig;

/**
 * Configuration class for MeasurementServer.
 * 
 * Singleton. first getInstance() initializes the properties. These are read
 * from homePath + CONFIGFILE_LOCATION . Properties then can be retrieved via
 * getProperty().
 * 
 * @author dennis
 *
 */
public class Configuration {
	static private Logger logger = LoggerFactory.getLogger(Configuration.class);

	public static final String PREFIX = "vxi.connector.";

	// public static final String CONN_SERIAL_TTY_BAUDRATE =
	// "connector.serial.tty.baudrate";
	// public static final String CONN_SERIAL_TTY_NAME =
	// "connector.serial.tty.name";
	// public static final String CONN_SERIAL_GPIB_DEFAULT_PRIMARY =
	// "connector.serial.gpib.default.primary";

	/**
	 * Config file relative path in classpath
	 */
	private static final String CONFIGFILE_LOCATION = "vxiserver.properties";

	/**
	 * Properties loaded from CONFIGFILE_LOCATION
	 */
	private static Properties properties = null;

	/**
	 * Singleton var
	 */
	private static Configuration INSTANCE = null;

	public static void setHomePath(String homePath) {
		// Configuration.homePath = homePath;
	}

	/**
	 * Singleton -> private
	 */
	private Configuration() {
	}

	/**
	 * Singleton -> creates privates and initializes instance.
	 * 
	 * @return the singleton
	 * @throws Exception
	 */
	public static Configuration getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new Configuration();
			try {
				initialize();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return INSTANCE;
	}

	public static void initialize() throws Exception {
		InputStream is = null;

		logger.info("Read configuration from file {}", CONFIGFILE_LOCATION);
		is = Configuration.class.getClassLoader()
				.getResourceAsStream(CONFIGFILE_LOCATION);
		if (is == null) {
			logger.info("Configuration not found.");
			throw new Exception("Configuration not found.");
		}
		properties = new Properties();
		try {
			properties.load(is);
		} catch (IOException e) {
			logger.info(
					"Failed loading properties from " + CONFIGFILE_LOCATION);
		}
	}

	public static String getProperty(String key) {
		return properties.getProperty(key);
	}

	public static List<ConnectorConfig> getConnectorConfigs() {
		List<ConnectorConfig> confs = new ArrayList<>();
		// Loop 1: create empty configs with correct type
		for (Object k : properties.keySet()) {
			String key = (String) k;
			if (!key.startsWith(PREFIX)) {
				logger.error("Unknown property key '{}'", key);
				continue;
			}
			String value = properties.getProperty(key);
			// logger.debug("key '{}', value={}", key, value);
			String keypart = key.replaceAll(PREFIX, "");
			// logger.debug(keypart);
			String[] parts = keypart.split("\\.");
			int id = 0;
			try {
				id = Integer.parseInt(parts[0]);
			} catch (Exception e) {
				logger.error("Cannot read id value from '{}'", parts[0]);
				continue;
			}
			if (parts.length != 2) {
				continue;
			}
			if (parts[1].equals("type")) {
				logger.info("type found, id={}, type={}", id, value);
				ConnectorConfig c = getConfigFor(confs, id, value);
				// add to list
				confs.add(c);
			}
		}

		// Loop 2: fill configs
		for (Object k : properties.keySet()) {
			String key = (String) k;
			if (!key.startsWith(PREFIX)) {
				logger.error("Unknown property key '{}'", key);
				continue;
			}
			String value = properties.getProperty(key);
			// logger.debug("key '{}', value={}", key, value);
			String keypart = key.replaceAll(PREFIX, "");
			// logger.info(keypart);
			String[] parts = keypart.split("\\.");
			int id = 0;
			try {
				id = Integer.parseInt(parts[0]);
			} catch (Exception e) {
				logger.error("Cannot read id value from '{}'", parts[0]);
				continue;
			}
			ConnectorConfig cc = getConfigFor(confs, id);
			fillConf(cc, parts, value);
		}
		return confs;
	}

	private static void fillConf(ConnectorConfig cc, String[] parts, String value) {
		logger.debug("keyp RPC '{}', value={}",
				Arrays.stream(parts).reduce((a, b) -> a + "." + b).get(),
				value);
		if (parts.length == 2) {
			try {
				callSetter(cc, parts[1], value);
			} catch (IllegalAccessException | IllegalArgumentException
					| InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			logger.error("TODO Cannot handle'{}'",
					Arrays.stream(parts).reduce((a, b) -> a + "." + b).get());
		}
	}

	private static void callSetter(ConnectorConfig cc, String key, String value)
			throws IllegalAccessException, IllegalArgumentException,
			InvocationTargetException {
		for (Method method : cc.getClass().getMethods()) {
			String mname = method.getName();
			// logger.debug("Method {}", mname);
			if (mname.startsWith("set") && mname.replaceAll("set", "")
					.toLowerCase().equals(key.toLowerCase())) {
				logger.debug("Call setter {} with value {}", mname, value);
				Class<?> pt = method.getParameterTypes()[0];
				//logger.debug("Param {}", pt.getSimpleName());
				switch (pt.getSimpleName()) {
				case "int":
					int intval = Integer.parseInt(value);
					method.invoke(cc, intval);
					break;
				case "String":
					method.invoke(cc, value);
					break;
					default:
						logger.error("Cannot handle property type {}", pt.getSimpleName());
						break;
				}
			}
		}
	}

	private static ConnectorConfig getConfigFor(List<ConnectorConfig> confs,
			int id, String type) {
		for (ConnectorConfig c : confs) {
			if (c.getId() == id) {
				return c;
			}
		}
		ConnectorConfig c = null;
		switch (type) {
		case "serial":
			c = new SerialConnectorConfig();
			break;
		case "serialgpib":
			c = new GPIBSerialConnectorConfig();
			break;
		case "net":
			c = new RPCConnectorConfig();
			break;
		default:
			logger.error("unknown type '{}'", type);
			break;
		}
		c.setId(id);
		return c;
	}

	private static ConnectorConfig getConfigFor(List<ConnectorConfig> confs,
			int id) {
		for (ConnectorConfig c : confs) {
			if (c.getId() == id) {
				return c;
			}
		}
		logger.error("Could not find config with id={}", id);
		return null;
	}
}