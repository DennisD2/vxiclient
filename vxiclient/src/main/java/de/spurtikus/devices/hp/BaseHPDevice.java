package de.spurtikus.devices.hp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.spurtikus.vxi.connectors.DeviceLink;
import de.spurtikus.vxi.connectors.VXIConnector;

public class BaseHPDevice {
	static Logger logger = LoggerFactory.getLogger(BaseHPDevice.class);
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

	/**
	 * Sleep. Allow device internal interrupts to be serviced.
	 * 
	 * @param ms
	 *            milliseconds to sleep.
	 */
	public void sleep(int ms) {
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			logger.error("sleep() failed.", e);
		}
	}

	/**
	 * Check device for errors.
	 * 
	 * @return list of error strings.
	 * @throws IOException
	 */
	public List<String> checkErrors() throws IOException {
		List<String> errors = new ArrayList<String>();
		boolean hasErrors = true;
		while (hasErrors) {
			String answer = "";
			try {
				answer = vxiConnector.send_and_receive(deviceLink, "SYST:ERR?");
			} catch (Exception e) {
				e.printStackTrace();
			}
			logger.debug(answer);
			if (answer.contains("+0,\"No error\"")) {
				hasErrors = false;
			} else {
				logger.error(answer);
				errors.add(answer);
			}
		}
		return errors;
	}

}
