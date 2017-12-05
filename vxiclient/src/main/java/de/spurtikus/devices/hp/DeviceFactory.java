package de.spurtikus.devices.hp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.spurtikus.vxi.connectors.DeviceLink;
import de.spurtikus.vxi.connectors.VXIConnector;
import de.spurtikus.vxi.service.AbstractBoundary;
import de.spurtikus.vxi.service.HP1300Boundary;
import de.spurtikus.vxi.service.HP1300PacerBoundary;
import de.spurtikus.vxi.service.HP1326Boundary;
import de.spurtikus.vxi.service.HP1340Boundary;

/**
 * Device factory class. Based on a device type and already existing
 * {VXIConnector} and {DeviceLink} objects, the device is created.
 * 
 * @author dennis
 *
 */
public class DeviceFactory {
	private static Logger logger = LoggerFactory.getLogger(DeviceFactory.class);

	public static BaseHPDevice create(Class<? extends AbstractBoundary> deviceClass, VXIConnector parent,
			DeviceLink link) throws Exception {
		BaseHPDevice device = null;
		switch (deviceClass.getSimpleName()) {
		case HP1300Boundary.className:
			device = new HP1300b(parent, link);
			break;
		case HP1300PacerBoundary.className:
			device = new HP1300Pacer(parent, link);
			break;
		case HP1326Boundary.className:
			device = new HP1326(parent, link);
			break;
		case "hp1330":
			device = new HP1330(parent, link);
			break;
		case "hp1333":
			device = new HP1333(parent, link);
			break;
		case HP1340Boundary.className:
			device = new HP1340(parent, link);
			break;
		default:
			String msg = "Unknown device type: " + deviceClass;
			logger.error(msg);
			throw new Exception(msg);
		}
		return device;
	}

}
