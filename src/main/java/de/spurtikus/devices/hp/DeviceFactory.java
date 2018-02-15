package de.spurtikus.devices.hp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.spurtikus.vxi.Constants;
import de.spurtikus.vxi.connectors.DeviceLink;
import de.spurtikus.vxi.connectors.VXIConnector;
import de.spurtikus.vxi.service.AbstractBoundary;
import de.spurtikus.vxi.service.MainframeBoundary;
import de.spurtikus.vxi.service.PacerBoundary;
import de.spurtikus.vxi.service.MultimeterBoundary;
import de.spurtikus.vxi.service.DIOBoundary;
import de.spurtikus.vxi.service.CounterBoundary;
import de.spurtikus.vxi.service.AFGBoundary;
import de.spurtikus.vxi.service.SwitchBoundary;

/**
 * Device factory class. Based on a device boundary type and already existing
 * {VXIConnector} and {DeviceLink} objects, the device is created.
 * 
 * @author dennis
 *
 */
public class DeviceFactory {
	private static Logger logger = LoggerFactory.getLogger(DeviceFactory.class);

	public static BaseHPDevice create(
			Class<? extends AbstractBoundary<?>> deviceClass, VXIConnector parent,
			DeviceLink link) throws Exception {
		BaseHPDevice device = null;
		
		switch (deviceClass.getSimpleName().replaceAll("Boundary", "").toLowerCase()) {
		case Constants.URL_MAINFRAME:
			device = new HP1300b(parent, link);
			break;
		case Constants.URL_PACER:
			device = new HP1300Pacer(parent, link);
			break;
		case Constants.URL_MULTIMETER:
			device = new HP1326(parent, link);
			break;
		case Constants.URL_DIGITALIO:
			device = new DigitalIO(parent, link);
			break;
		case Constants.URL_COUNTER:
			device = new HP1333(parent, link);
			break;
		case Constants.URL_AFG:
			device = new HP1340(parent, link);
			break;
		case Constants.URL_SWITCH:
			device = new HP1351(parent, link);
			break;
		default:
			String msg = "Unknown device type: " + deviceClass;
			logger.error(msg);
			throw new Exception(msg);
		}
		return device;
	}

}
