package de.spurtikus.devices.hp;

import de.spurtikus.vxi.connectors.DeviceLink;
import de.spurtikus.vxi.connectors.VXIConnector;

public class BaseHPDevice {
	// static Logger logger = LoggerFactory.getLogger("BaseHPDevice");

	/**
	 * The connector object to a vxi mainframe
	 */
	protected VXIConnector vxiConnector;

	/**
	 * The device link to use
	 */
	protected DeviceLink deviceLink;

	/**
	 * CTR for HP devices.
	 * 
	 * @param parent
	 *            VXI connector to use
	 * @param link
	 *            device link to use
	 */
	public BaseHPDevice(VXIConnector parent, DeviceLink link) {
		vxiConnector = parent;
		deviceLink = link;
	}

	protected void sleep(int ms) {
		// sleep 100ms - allow interrupts (inside E1340) to be serviced
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
