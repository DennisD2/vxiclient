package de.spurtikus.vxi.controller;

import de.spurtikus.devices.hp.*;
import de.spurtikus.vxi.connectors.DeviceLink;
import de.spurtikus.vxi.connectors.VXIConnector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Device factory class. Based on a device boundary type and already existing
 * {VXIConnector} and {DeviceLink} objects, the device is created.
 *
 * @author dennis
 */
public class DeviceFactory {
    private static Logger logger = LoggerFactory.getLogger(DeviceFactory.class);

    public static BaseHPDevice create(
            Class<? extends AbstractController<?>> deviceClass, String deviceType,
            VXIConnector parent, DeviceLink link) throws Exception {
        BaseHPDevice device = null;

        String canonizedDevice = deviceClass.getSimpleName().replaceAll("Controller", "").toLowerCase();
        switch (canonizedDevice) {
            case Constants.URL_MAINFRAME:
                device = new HP1300b(parent, link);
                break;
            case Constants.URL_PACER:
                device = new HP1300Pacer(parent, link);
                break;
            case Constants.URL_MULTIMETER:
                switch (deviceType) {
                    case "multimeter":
                        // HP E1326 and E1411
                        device = new HP1326(parent, link);
                        break;
                    case "multimeter-hp3478a":
                        // HP 3478A
                        device = new HP3478(parent, link);
                        break;
                    default:
                        String msg = "Unknown multimeter type: " + deviceClass;
                        logger.error(msg);
                        throw new Exception(msg);
                }
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
