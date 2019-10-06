package de.spurtikus.vxi.controller;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import de.spurtikus.vxi.connectors.ConnectorConfig;
import de.spurtikus.vxi.connectors.rpc.RPCConnectorConfig;
import de.spurtikus.vxi.connectors.serial.GPIBSerialConnectorConfig;
import de.spurtikus.vxi.connectors.serial.SerialConnectorConfig;
import de.spurtikus.vxi.service.DeviceInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

/**
 * Configuration class.
 * <p>
 * Pure static class. load() initializes the properties. These are read from
 * {Constants.CONFIGFILE_LOCATION}.
 *
 * @author dennis
 */
public class Configuration {
    static private Logger logger = LoggerFactory.getLogger(Configuration.class);

    private static final String TYPE_PROP_KEY = "type";

    /**
     * Properties loaded from CONFIGFILE_LOCATION
     */
    private static Properties properties = null;

    /**
     * List of configs
     */
    private static List<ConnectorConfig> confs = null;

    /**
     * Pure static class
     */
    private Configuration() {
    }

    /**
     * Initializes configuration. Reads in properties.
     *
     * @throws Exception
     */
    public static void load() throws Exception {
        InputStream is = null;

        logger.info("Read configuration from classpath resource {}",
                Constants.CONFIGFILE_LOCATION);
        /*is = Configuration.class.getClassLoader()
                .getResourceAsStream(Constants.CONFIGFILE_LOCATION);*/

        // Code below searches from its root '/BOOT-INF/classes'
        ApplicationContext appContext = new ClassPathXmlApplicationContext();
        Resource resource = appContext.getResource("classpath:resources/" + Constants.CONFIGFILE_LOCATION);
        is = resource.getInputStream();
        if (is == null) {
            logger.info("Configuration not found.");
            throw new Exception("Configuration not found.");
        }
        properties = new Properties();
        try {
            properties.load(is);
        } catch (IOException e) {
            logger.info("Failed loading properties from "
                    + Constants.CONFIGFILE_LOCATION);
            throw new Exception("Failed loading properties from "
                    + Constants.CONFIGFILE_LOCATION);
        }
        // set up config list
        confs = getConfigs();
    }

    /**
     * Get a property by key.
     *
     * @param key property name/key.
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
    public static List<ConnectorConfig> getConfigs() {
        if (confs == null) {
            confs = new ArrayList<>();
            // Loop 1: create empty configs with correct type
            for (Object k : properties.keySet()) {
                String key = (String) k;
                if (!key.startsWith(Constants.CONFIGVAL_PREFIX)) {
                    logger.error("Unknown property key '{}'", key);
                    continue;
                }
                String value = properties.getProperty(key);
                // logger.debug("key '{}', value={}", key, value);
                String keypart = key.replaceAll(Constants.CONFIGVAL_PREFIX, "");
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
                if (parts[1].equals(TYPE_PROP_KEY)) {
                    logger.info("type found, id={}, type={}", id, value);
                    ConnectorConfig c = getConfigFor(confs, id, value);
                    // add to list
                    confs.add(c);
                }
            }

            // Loop 2: fill configs
            for (Object k : properties.keySet()) {
                String key = (String) k;
                if (!key.startsWith(Constants.CONFIGVAL_PREFIX)) {
                    logger.error("Unknown property key '{}'", key);
                    continue;
                }
                String value = properties.getProperty(key);
                // logger.debug("key '{}', value={}", key, value);
                String keypart = key.replaceAll(Constants.CONFIGVAL_PREFIX, "");
                // logger.info(keypart);
                String[] parts = keypart.split("\\.");
                int id = 0;
                try {
                    id = Integer.parseInt(parts[0]);
                } catch (Exception e) {
                    logger.error("Cannot read id value from '{}'", parts[0]);
                    continue;
                }
                ConnectorConfig cc = findConfigById(id);
                fillConf(cc, parts, value);
            }
        }
        return confs;
    }

    /**
     * Fills configuration value in ConnectorConfig object.
     *
     * @param conf  configuration object
     * @param parts parts of key (e.g. '1.baudrate')
     * @param value value for key
     */
    protected static void fillConf(ConnectorConfig conf, String[] parts,
                                   String value) {
        // logger.debug("key RPC '{}', value={}",
        // Arrays.stream(parts).reduce((a, b) -> a + "." + b).get(),
        // value);
        if (parts.length == 2) {
            try {
                callSetter(conf, parts[1], value);
            } catch (IllegalAccessException | IllegalArgumentException
                    | InvocationTargetException e) {
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
     * @param conf  configuration object
     * @param key   key attribute name to set
     * @param value value for attribute
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    protected static void callSetter(ConnectorConfig conf, String key,
                                     String value) throws IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        boolean called = false;
        for (Method method : conf.getClass().getMethods()) {
            String mname = method.getName();
            // logger.debug("Method {}", mname);
            if (mname.startsWith("set") && mname.replaceAll("set", "")
                    .toLowerCase().equals(key.toLowerCase())) {
                logger.debug("Call config (id={}) setter {} with value {}",
                        conf.getId(), mname, value);
                Class<?> pt = method.getParameterTypes()[0];
                // logger.debug("Param {}", pt.getSimpleName());
                switch (pt.getSimpleName()) {
                    case "boolean":
                        boolean boolval = Boolean.parseBoolean(value);
                        method.invoke(conf, boolval);
                        called = true;
                        break;
                    case "int":
                        int intval = Integer.parseInt(value);
                        method.invoke(conf, intval);
                        called = true;
                        break;
                    case "String":
                        method.invoke(conf, value);
                        called = true;
                        break;
                    case "List":
                        List<DeviceInfo> mval = JSonStringToList(value, conf.getName());
                        method.invoke(conf, mval);
                        called = true;
                        break;
                    default:
                        logger.error("Cannot handle property type {}",
                                pt.getSimpleName());
                        break;
                }
            }
        }
        if (!called && !key.equals(TYPE_PROP_KEY)) {
            logger.error("No setter for key {}", key);
        }
    }

    protected static List<DeviceInfo> JSonStringToList(String value, String mainframe) {
        java.lang.reflect.Type listType = new TypeToken<List<DeviceInfo>>() {
        }.getType();
        Gson gson = new Gson();
        List<DeviceInfo> devs = gson.fromJson(value, listType);
        if (devs != null) {
            // Initialize 'mainframe' attribute
            devs.stream().forEach(dev -> dev.setMainframe(mainframe));
        }
        return devs;
    }

    /**
     * Gets object (new or existing) for a given connector configuration id.
     *
     * @param confs list of configurations found.
     * @param id    id to get object for.
     * @param type  type of connector (used for CTR call).
     * @return object found or new object.
     */
    protected static ConnectorConfig getConfigFor(List<ConnectorConfig> confs,
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
     * Finds a configuration by its name.
     *
     * @param id id to find object for.
     * @return object found or null.
     */
    public static ConnectorConfig findConfigByName(String name) {
        ConnectorConfig conf = confs.stream()
                .filter(c -> c.getName().equals(name)).findAny().get();
        if (conf == null) {
            logger.error("Could not find config with name={}", name);
        }
        return conf;
    }

    /**
     * Finds a configuration by its id.
     *
     * @param id id to find object for.
     * @return object found or null.
     */
    public static ConnectorConfig findConfigById(int id) {
        ConnectorConfig conf = confs.stream().filter(c -> c.getId() == id)
                .findAny().get();
        if (conf == null) {
            logger.error("Could not find config with id={}", id);
        }
        return conf;
    }

    /**
     * Gets all enabled configs.
     *
     * @return enabled configs.
     */
    public static List<ConnectorConfig> getEnabledConfigs() {
        return confs.stream().filter(c -> c.isEnabled())
                .collect(Collectors.toList());
    }

    /**
     * Gets device id (like "iscpi,37" or "9,0" or "21") for a device name (like
     * "hp1330"). Name to device id mapping is user defined in file
     * vxiserver.properties.
     *
     * @param connector config name connector config name to use.
     * @param name      device name.
     * @return device id.
     */
    public static String getDeviceIdByName(String configName, String name) {
        ConnectorConfig theConf = confs.stream()
                .filter(c -> c.getName().equals(configName)).findAny().get();
        return theConf.getDeviceIdByName(name);
    }

    /**
     * Gets device id (like "iscpi,37" or "9,0") for a device name (like
     * "hp1330"). Name to device id mapping is user defined in file
     * vxiserver.properties.
     *
     * @param connectorId connector id to use.
     * @param name        device name.
     * @return device id.
     */
    public static String getDeviceIdByName(int connectorId, String name) {
        ConnectorConfig theConf = confs.stream()
                .filter(c -> c.getId() == connectorId).findAny().get();
        return theConf.getDeviceIdByName(name);
    }
}