package de.spurtikus.devices.hp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.spurtikus.vxi.connectors.DeviceLink;
import de.spurtikus.vxi.connectors.VXIConnector;

public class HP1351 extends BaseHPDevice {
	static Logger logger = LoggerFactory.getLogger(HP1351.class);

	public HP1351(VXIConnector parent, DeviceLink link) {
		super(parent, link);
	}


}
