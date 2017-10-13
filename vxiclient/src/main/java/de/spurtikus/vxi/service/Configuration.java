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
 * Configuration class.
 * 
 * Singleton. first getInstance() initializes the properties. These are read
 * from CONFIGFILE_LOCATION .
 * 
 * @author dennis
 *
 */
public class Configuration {
	static private Logger logger = LoggerFactory.getLogger(Configuration.class);

	public static final String PREFIX = "vxi.connector.";

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

	/**
	 * Initializes configuration. Reads in properties.
	 * 
	 * @throws Exception
	 */
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

	/**
	 * Get a property by key.
	 * 
	 * @param key
	 *            property name/key.
	 * @return property value.
	 */
	public static String getProperty(String key) {
		return properties.getProperty(key);
	}

	/**
	 * Gets list of ConnectorConfigurations.
	 * 
	 * @return list of ConnectorConfigurations.
	 */
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
			ConnectorConfig cc = findConfigById(confs, id);
			fillConf(cc, parts, value);
		}
		return confs;
	}

	/**
	 * Fills configuration value in ConnectorConfig object.
	 * 
	 * @param conf
	 *            configuration object
	 * @param parts
	 *            parts of key (e.g. '1.baudrate')
	 * @param value
	 *            value for key
	 */
	private static void fillConf(ConnectorConfig conf, String[] parts,
			String value) {
		logger.debug("keyp RPC '{}', value={}",
				Arrays.stream(parts).reduce((a, b) -> a + "." + b).get(),
				value);
		if (parts.length == 2) {
			try {
				callSetter(conf, parts[1], value);
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

	/**
	 * Sets 'value' for 'key' in configuration object. Uses introspection to
	 * find setter method.
	 * 
	 * @param conf
	 *            configuration object
	 * @param key
	 *            key attribute name to set
	 * @param value
	 *            value for attribute
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 * @throws InvocationTargetException
	 */
	private static void callSetter(ConnectorConfig conf, String key,
			String value) throws IllegalAccessException,
			IllegalArgumentException, InvocationTargetException {
		for (Method method : conf.getClass().getMethods()) {
			String mname = method.getName();
			// logger.debug("Method {}", mname);
			if (mname.startsWith("set") && mname.replaceAll("set", "")
					.toLowerCase().equals(key.toLowerCase())) {
				logger.debug("Call setter {} with value {}", mname, value);
				Class<?> pt = method.getParameterTypes()[0];
				// logger.debug("Param {}", pt.getSimpleName());
				switch (pt.getSimpleName()) {
				case "int":
					int intval = Integer.parseInt(value);
					method.invoke(conf, intval);
					break;
				case "String":
					method.invoke(conf, value);
					break;
				default:
					logger.error("Cannot handle property type {}",
							pt.getSimpleName());
					break;
				}
			}
		}
	}

	/**
	 * Gets object (new or existing) for a given connector configuration id.
	 * 
	 * @param confs
	 *            list of configurations found.
	 * @param id
	 *            id to get object for.
	 * @param type
	 *            type of connector (used for CTR call).
	 * @return object found or new object.
	 */
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

	/**
	 * 
	 * @param confs
	 *            list of configurations found.
	 * @param id
	 *            id to find object for.
	 * @return object found or null.
	 */
	private static ConnectorConfig findConfigById(List<ConnectorConfig> confs,
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