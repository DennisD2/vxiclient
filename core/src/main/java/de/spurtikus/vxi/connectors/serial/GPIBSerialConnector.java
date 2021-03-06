package de.spurtikus.vxi.connectors.serial;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.spurtikus.vxi.connectors.ConnectorConfig;
import de.spurtikus.vxi.connectors.DeviceLink;
import de.spurtikus.vxi.connectors.GPIBDeviceLink;
import de.spurtikus.vxi.connectors.VXIConnector;

public class GPIBSerialConnector extends SerialConnector {
	Logger logger = LoggerFactory.getLogger(GPIBSerialConnector.class);

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

	/** GPIB connector initialization state */
	boolean gpibInitialized = false;

	/** Currently selected GPIB primary address */
	private static int selectedSecondary;
	/** Currently selected GPIB secondary address */
	private static int selectedPrimary;

	protected GPIBSerialConnectorConfig getConfig() {
		return (GPIBSerialConnectorConfig) theConfig;
	}

	@Override
	public DeviceLink initialize(ConnectorConfig config, String deviceId)
			throws Exception {
		/** reuse device link if possible */
		if (deviceLinks.containsKey(deviceId)) {
			logger.debug("Reuse device link for: " + deviceId);
			return deviceLinks.get(deviceId);
		}
		logger.debug("Creating new device link for: " + deviceId);

		super.initialize(config, deviceId);
		/** Create new device link for GPIB */
		GPIBSerialConnectorConfig c = getConfig();
		GPIBDeviceLink glink = createDeviceLinkFor(deviceId);
		// Add config reference to device link
		glink.config = c;
		DeviceLink l = new DeviceLink(glink);

		if (!initialized) {
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

			initialized = true;
		}

		// Select device if not yet selected.
		selectDevice(l, glink);

		// Put new link to deviceLink map
		deviceLinks.put(deviceId, l);
		return l;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void send(DeviceLink link, String message) throws Exception {
		GPIBDeviceLink glink = (GPIBDeviceLink) link.getWrapped();
		selectDevice(link, glink);
		super.send(link, message);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String receive(DeviceLink link) throws Exception {
		GPIBDeviceLink glink = (GPIBDeviceLink) link.getWrapped();
		selectDevice(link, glink);
		return super.receive(link);
	}

	protected GPIBDeviceLink createDeviceLinkFor(String deviceId) {
		GPIBDeviceLink l = new GPIBDeviceLink();
		l.secondary = GPIBDeviceLink.UNDEFINED;
		l.primary = getPrimaryAddressFrom(deviceId);
		if (deviceId.contains(",")) {
			l.secondary = getSecondaryAddressFrom(deviceId);
		}
		return l;
	}

	private int getPrimaryAddressFrom(String deviceId) {
		String[] p = deviceId.split(",");
		int r = Integer.parseInt(p[0]);
		return r;
	}

	private int getSecondaryAddressFrom(String deviceId) {
		String[] p = deviceId.split(",");
		int r = Integer.parseInt(p[1]);
		return r;
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
	 * Selects a device by its primary/secondary GPIB address. Does nothing if
	 * device is already selected.
	 * 
	 * @param link
	 * @param primary
	 * @param secondary
	 * @throws Exception
	 */
	public void selectDevice(DeviceLink link, GPIBDeviceLink glink)
			throws Exception {
		int primary = glink.primary;
		int secondary = glink.secondary;
		if (isSelected(primary, secondary)) {
			// logger.debug("Device already selected");
			return;
		}
		if (secondary != GPIBDeviceLink.UNDEFINED) {
			logger.debug("Select device: {},{}", primary, secondary);
		} else {
			logger.debug("Select device: {}", primary);
		}

		if (theConfig
				.getAdapterType() == SerialConnectorConfig.ADAPTER_SERIAL_GPIB) {
			StringBuilder sb = new StringBuilder();
			sb.append(".a ");
			sb.append(primary);
			if (secondary != GPIBDeviceLink.UNDEFINED) {
				sb.append(" ");
				sb.append(secondary);
			}
			String s = send_and_receive(link, sb.toString());
			logger.debug("DEV: " + s);
		}
		if (theConfig
				.getAdapterType() == SerialConnectorConfig.ADAPTER_PROLOGIX) {
			StringBuilder sb = new StringBuilder();
			sb.append("++addr ");
			sb.append(primary);
			if (secondary != GPIBDeviceLink.UNDEFINED) {
				// Prologix wants for secondary adress: gpib-adress+96
				int realAdr = secondary + 96;
				sb.append(" ");
				sb.append(realAdr);
			}
			super.send(link, sb.toString());
		}
		selectedPrimary = primary;
		selectedSecondary = secondary;
	}

	/**
	 * Checks if device is already slected
	 * 
	 * @param primary
	 * @param secondary
	 * @return
	 */
	private boolean isSelected(int primary, int secondary) {
		return selectedSecondary == secondary && selectedPrimary == primary;
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

	// TODO: methode needed????
	/**
	 * Selects a device by its name. Used with direct serial communication.
	 * UNTESTED.
	 * 
	 * @param device
	 *            Device name
	 * @throws Exception
	 */
	/*
	 * @Deprecated public void selectDevice(DeviceLink deviceLink, String
	 * device) throws Exception { if (theConfig.getAdapterType() ==
	 * ADAPTER_SERIAL_DIRECT) { String s = send_and_receive(deviceLink,
	 * CRTL_D_STRING); logger.debug("DEV: " + s); s =
	 * send_and_receive(deviceLink, SELECT_INSTRUMENT + " " + device);
	 * logger.debug("DEV: " + s); } throw new Exception("???"); }
	 */
}
