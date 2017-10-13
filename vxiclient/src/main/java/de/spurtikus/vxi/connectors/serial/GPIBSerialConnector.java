package de.spurtikus.vxi.connectors.serial;

import java.io.IOException;

import de.spurtikus.vxi.connectors.ConnectorConfig;
import de.spurtikus.vxi.connectors.DeviceLink;
import de.spurtikus.vxi.connectors.VXIConnector;

public class GPIBSerialConnector extends SerialConnector {

	public static final int ADAPTER_SERIAL_DIRECT = 0;
	public static final int ADAPTER_SERIAL_GPIB = 1;
	public static final int ADAPTER_PROLOGIX = 2;

	static final String CRTL_D_STRING = "\u0004";
	static final String SELECT_INSTRUMENT = "si";
	public static final String SYSTEM_INSTRUMENT = "SYSTEM";

	public static VXIConnector getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new GPIBSerialConnector();
		}
		return INSTANCE;
	}

	protected GPIBSerialConnectorConfig getConfig() {
		return (GPIBSerialConnectorConfig) theConfig;
	}

	@Override
	public DeviceLink initialize(ConnectorConfig config) throws Exception {
		DeviceLink l = super.initialize(config);

		GPIBSerialConnectorConfig c = getConfig();

		initializeCommunication(l);

		if (c.getAdapterType() == ADAPTER_SERIAL_DIRECT) {
			initializeSerialProtocol();
		}
		if (c.getAdapterType() == ADAPTER_SERIAL_GPIB) {
			String s = send_and_receive(l, ".y");
			logger.debug("DEV: " + s);
		}
		if (c.getAdapterType() == ADAPTER_PROLOGIX) {
			String s;
			s = send_and_receive(l, "++ver");
			logger.debug("DEV: " + s);
			// s = vxiConnector.send_and_receive(deviceLink, "++auto");
			// log.xxx("DEV: " + s);
		}

		String s = send_and_receive(l, "++ver");
		logger.debug("DEV: " + s);
		// s = send_and_receive("++auto");
		// logger.debug("DEV: " + s);

		// Select mainframe
		selectDevice(l, c.getControllerPrimaryAddress(),
				c.getControllerSecondaryAddress());
		return l;
	}

	/**
	 * Initialization of communication interface in HP1300B.
	 * 
	 * @throws Exception
	 */
	public void initializeCommunication(DeviceLink deviceLink)
			throws Exception {
		GPIBSerialConnectorConfig c = getConfig();
		if (c.getAdapterType() == ADAPTER_SERIAL_DIRECT) {
			String s = send_and_receive(deviceLink, CRTL_D_STRING);
			logger.debug("DEV: " + s);
			// set (i.e.: clear) terminal type
			s = send_and_receive(deviceLink, "st UNKNOWN");
			logger.debug("DEV: " + s);
		}
	}

	/**
	 * Initialization RS232 protocol.
	 * 
	 * @throws IOException
	 */
	public void initializeSerialProtocol() throws IOException {
		/*
		 * String s = null; int rs232BufferSize = 0;
		 * selectDevice(SYSTEM_INSTRUMENT);
		 * 
		 * switch (port.getProtocol()) { case CTSRTS: // s =
		 * vxiConnector.send_and_receive(deviceLink,
		 * "SYST:COMM:SER:PACE:PROT NONE"); s =
		 * vxiConnector.send_and_receive(deviceLink,
		 * "SYST:COMM:SER:CONT:RTS IBFULL"); logger.debug("DEV: " + s);
		 * 
		 * // get size of rs232 input buffer in mainframe s =
		 * vxiConnector.send_and_receive(deviceLink,
		 * "SYST:COMM:SER:PACE:THR:STOP? MAX"); // log.xxx("DEV: " + s);
		 * rs232BufferSize = answerStringToInteger(s);
		 * logger.debug("Buffer size: " + rs232BufferSize);
		 * 
		 * // set size of rs232 input buffer rs232BufferSize -= 20; s =
		 * vxiConnector.send_and_receive(deviceLink,
		 * "SYST:COMM:SER:PACE:THR:STOP " + rs232BufferSize);
		 * logger.debug("DEV: " + s);
		 * 
		 * // flush input buffer; 0 (as on page 5-12) seems impossible, so we //
		 * use // MIN s = vxiConnector.send_and_receive(deviceLink,
		 * "SYST:COMM:SER:PACE:THR:STAR MIN"); logger.debug("DEV: " + s);
		 * 
		 * s = vxiConnector.send_and_receive(deviceLink,
		 * "SYST:COMM:SER:CONT:RTS IBFULL"); logger.debug("DEV: " + s);
		 * 
		 * logger.debug("Flow control mainframe side: RTS/CTS"); break; case
		 * XONXOFF: default: // See HP1300B manual, page 5-12 to deepen whats
		 * going on.
		 * 
		 * // get size of rs232 input buffer in mainframe s =
		 * vxiConnector.send_and_receive(deviceLink,
		 * "SYST:COMM:SER:PACE:THR:STOP? MAX"); // log.xxx("DEV: " + s);
		 * rs232BufferSize = answerStringToInteger(s);
		 * logger.debug("Buffer size: " + rs232BufferSize);
		 * 
		 * // set size of rs232 input buffer rs232BufferSize -= 20; s =
		 * vxiConnector.send_and_receive(deviceLink,
		 * "SYST:COMM:SER:PACE:THR:STOP " + rs232BufferSize);
		 * logger.debug("DEV: " + s);
		 * 
		 * // flush input buffer; 0 (as on page 5-12) seems impossible, so we //
		 * use // MIN s = vxiConnector.send_and_receive(deviceLink,
		 * "SYST:COMM:SER:PACE:THR:STAR MIN"); logger.debug("DEV: " + s); // s =
		 * vxiConnector.send_and_receive(deviceLink,
		 * "SYST:COMM:SER:PACE:THR:STAR?"); // log.xxx("DEV: " + s); // enable
		 * xon/xoff; command syntax here is different from that on // page //
		 * 5-12 s = vxiConnector.send_and_receive(deviceLink,
		 * "SYST:COMM:SER:PACE:PROT XON"); logger.debug("DEV: " + s);
		 * logger.debug("Flow control mainframe side: Xon/XOff"); break; }
		 */ }

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
	
	/**
	 * Selects a device by its name. Used with direct serial communication.
	 * UNTESTED.
	 * 
	 * @param device
	 *            Device name
	 * @throws Exception
	 */
	public void selectDevice(DeviceLink deviceLink, String device) throws Exception {
		if (theConfig.getAdapterType() == ADAPTER_SERIAL_DIRECT) {
			String s = send_and_receive(deviceLink, CRTL_D_STRING);
			logger.debug("DEV: " + s);
			s = send_and_receive(deviceLink, SELECT_INSTRUMENT + " " + device);
			logger.debug("DEV: " + s);
		}
	}
	
}
