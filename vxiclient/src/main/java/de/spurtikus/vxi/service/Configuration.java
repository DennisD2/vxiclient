package de.spurtikus.vxi.service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Logger;

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
	static Logger log = Logger.getLogger("Constants");

	/**
	 * webroot property name
	 */
	//public static final String USER_DIR_WEBROOT = "user.dir.webroot";
	/**
	 * http server port
	 */
	//public static final String HTTPSERVER_PORT = "httpserver.port";

	public static final String CONN_SERIAL_TTY_BAUDRATE = "connector.serial.tty.baudrate";

	public static final String CONN_SERIAL_TTY_NAME = "connector.serial.tty.name";

	public static final String CONN_SERIAL_GPIB_DEFAULT_PRIMARY = "connector.serial.gpib.default.primary";

	/**
	 * Config file relative path
	 */
	private static final String CONFIGFILE_LOCATION = "/conf/vxiserver.properties";

	/**
	 * mServer home path (absolute path) see ServerMain on how this is created
	 * and set here
	 */
	private static String homePath = null;

	/**
	 * Properties loaded from CONFIGFILE_LOCATION
	 */
	private static Properties properties = null;

	/**
	 * Singleton var
	 */
	private static Configuration INSTANCE = null;

	public static void setHomePath(String homePath) {
		//Configuration.homePath = homePath;
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
	 */
	public static Configuration getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new Configuration();
			if (homePath != null) {
				initialize();
			} else {
				log.severe("Instance not initialized (no filename was given)");
			}
		}
		return INSTANCE;
	}

	public static void initialize() {
		InputStream is = null;

		// is = ServerMain.class.getClassLoader().getResourceAsStream(homePath +
		// CONFIGFILE_LOCATION);
		try {
			is = new FileInputStream(homePath + CONFIGFILE_LOCATION);
		} catch (FileNotFoundException e1) {
			log.info("Failed loading file: " + homePath + CONFIGFILE_LOCATION);
			e1.printStackTrace();
		}

		properties = new Properties();
		try {
			properties.load(is);
		} catch (IOException e) {
			log.info("Failed loading properties from " + homePath + CONFIGFILE_LOCATION);
		}

		//String webRoot = properties.getProperty(USER_DIR_WEBROOT);
		//log.info("Webserver docroot: " + webRoot);
	}

	public static String getProperty(String key) {
		return properties.getProperty(key);
	}

	/**
	 * Get a property with home path prefixed. Useful for local paths inside
	 * mserver distribution.
	 * 
	 * @param key
	 * @return
	 */
	public static String getBasedProperty(String key) {
		return homePath + properties.getProperty(key);
	}

}