package de.spurtikus.vxi.connectors.serial;

import de.spurtikus.vxi.connectors.ConnectorConfig;
import de.spurtikus.vxi.connectors.DeviceLink;
import de.spurtikus.vxi.connectors.VXIConnector;

public class GPIBSerialConnector extends SerialConnector {

	public static VXIConnector getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new GPIBSerialConnector();
		}
		return INSTANCE;
	}

	@Override
	public DeviceLink initialize(ConnectorConfig config) throws Exception {
		DeviceLink l = super.initialize(config);

		String s = send_and_receive(l, "++ver");
		logger.debug("DEV: " + s);
		// s = send_and_receive("++auto");
		// logger.debug("DEV: " + s);
		
		// Select mainframe 
		GPIBSerialConnectorConfig c = (GPIBSerialConnectorConfig) theConfig;
		selectDevice(l, c.getControllerPrimaryAddress(), c.getControllerSecondaryAddress());
		return l;
	}

	/**
	 * 
	 * Selects a device by its primary/secondary GPIB address. Used with serial
	 * gpib communication.
	 * 
	 * @param link
	 * @param primary
	 * @param secondary
	 * @throws Exception
	 */
	public void selectDevice(DeviceLink link, int primary, int secondary)
			throws Exception {
		if (theConfig
				.getAdapterType() == SerialConnectorConfig.ADAPTER_SERIAL_GPIB) {
			String s = send_and_receive(link,
					".a " + primary + " " + secondary);
			logger.debug("DEV: " + s);
		}
		if (theConfig
				.getAdapterType() == SerialConnectorConfig.ADAPTER_PROLOGIX) {
			// Prologix wants for secondary adress: gpib-adress+96
			int realAdr = secondary + 96;
			send(link, "++addr " + primary + " " + realAdr);
		}
	}
}
