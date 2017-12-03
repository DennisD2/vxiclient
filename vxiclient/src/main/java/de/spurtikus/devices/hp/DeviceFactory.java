package de.spurtikus.devices.hp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.spurtikus.vxi.connectors.DeviceLink;
import de.spurtikus.vxi.connectors.VXIConnector;

/**
 * Device factory class. Based on a device type and already existing
 * {VXIConnector} and {DeviceLink} objects, the device is created.
 * 
 * @author dennis
 *
 */
public class DeviceFactory {
	private static Logger logger = LoggerFactory.getLogger(DeviceFactory.class);

	public static BaseHPDevice create(String type, VXIConnector parent,
			DeviceLink link) {
		BaseHPDevice device = null;
		switch (type) {
		case "hp1300b":
			device = new HP1300b(parent, link);
			break;
		case "hp1300Pacer":
			device = new HP1300Pacer(parent, link);
			break;
		case "hp1326":
		case "hp1411":
			device = new HP1326(parent, link);
			break;
		case "hp1330":
			device = new HP1330(parent, link);
			break;
		case "hp1333":
			device = new HP1333(parent, link);
			break;
		case "hp1340":
			device = new HP1340(parent, link);
			break;
		default:
			logger.error("Unknown device type {}", type);
			break;
		}
		return device;
	}

}
