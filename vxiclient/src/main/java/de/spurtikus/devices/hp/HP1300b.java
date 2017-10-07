package de.spurtikus.devices.hp;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.spurtikus.vxi.connectors.DeviceLink;
import de.spurtikus.vxi.connectors.VXIConnector;
import de.spurtikus.vxi.util.CommunicationUtil;

/**
 * HP/Agilent/Keysight E1300B/E1301B/75000 VXI mainframe control.
 * 
 * @author dennis
 * 
 *         TODO XXX
 * 
 *         Many lines were commented out. These lines usually will go to a
 *         future VXIConnector implementing serial protocol. For now the HP1300
 *         is here, but it CANNOT work. It is useless and obnly left here for future work.
 *
 */
public class HP1300b extends BaseHPDevice {
	static Logger logger = LoggerFactory.getLogger(HP1326.class);

	public static final int ADAPTER_SERIAL_DIRECT = 0;
	public static final int ADAPTER_SERIAL_GPIB = 1;
	public static final int ADAPTER_PROLOGIX = 2;

	/**
	 * Adapter type (direct, gpib)
	 */
	private int adapterType = ADAPTER_SERIAL_DIRECT;
	/**
	 * Communication port to use.
	 */
	// static Communication port = new Communication();

	static final String CRTL_D_STRING = "\u0004";
	static final String SELECT_INSTRUMENT = "si";
	public static final String SYSTEM_INSTRUMENT = "SYSTEM";

	public HP1300b(VXIConnector parent, DeviceLink link) {
		super(parent, link);
	}
	/**
	 * CTR. Configures communication port.
	 * 
	 * @param device
	 *            Serial device name. On Linux, this is e.g. "/dev/ttyS0".
	 * @param baud
	 *            Baudrate. Default of HP75000 is 9600.
	 * @param dataBits
	 *            Databits value. Default of HP75000 is 8.
	 * @param parity
	 *            Parity value. Default of HP75000 is 0 (no parity).
	 * @param stopBits
	 *            Stop bits value. Default of HP75000 is 1.
	 */
	// public HP1300b(int adapterType, String device, int baud, int dataBits,
	// int parity, int stopBits) {
	// this.adapterType = adapterType;
	// //port = new Communication();
	// //port.configure(device, baud, dataBits, parity, stopBits,
	// Communication.Protocol.XONXOFF);
	// }

	// public HP1300b(int adapterType/*, Communication port*/) {
	// this.adapterType = adapterType;
	// //HP1300b.port = port;
	// //HP1300b.port.configure(port.getDevice(), port.getBaudrate(),
	// port.getDataBits(), port.getParity(),
	// // port.getStopBits(), port.getProtocol());
	// }

	/**
	 * Initialization of mainframe. COmmunication and Protocol is initialized.
	 * 
	 * @throws Exception
	 */
	public void initialize() throws Exception {
		initializeCommunication();
		if (adapterType == ADAPTER_SERIAL_DIRECT) {
			initializeSerialProtocol();
		}
		if (adapterType == ADAPTER_SERIAL_GPIB) {
			String s = vxiConnector.send_and_receive(deviceLink, ".y");
			logger.debug("DEV: " + s);
		}
		if (adapterType == ADAPTER_PROLOGIX) {
			String s;
			s = vxiConnector.send_and_receive(deviceLink, "++ver");
			logger.debug("DEV: " + s);
			// s = vxiConnector.send_and_receive(deviceLink, "++auto");
			// log.xxx("DEV: " + s);
		}
	}

	/**
	 * Initialization of communication interface in HP1300B.
	 * 
	 * @throws Exception
	 */
	public void initializeCommunication() throws Exception {
		if (adapterType == ADAPTER_SERIAL_DIRECT) {
			String s = vxiConnector.send_and_receive(deviceLink, CRTL_D_STRING);
			logger.debug("DEV: " + s);
			// set (i.e.: clear) terminal type
			s = vxiConnector.send_and_receive(deviceLink, "st UNKNOWN");
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

	protected int answerStringToInteger(String s) {
		CommunicationUtil.dumpString(s);
		if (s == null || s.isEmpty()) {
			return 0;
		}
		return Integer.parseInt(s.replace("\n", "").replace("\r", ""));
	}

	/**
	 * Selects a device by its name. Used with direct serial communication.
	 * 
	 * @param device
	 *            Device name
	 * @throws Exception
	 */
	public void selectDevice(String device) throws Exception {
		if (adapterType == ADAPTER_SERIAL_DIRECT) {
			String s = vxiConnector.send_and_receive(deviceLink, CRTL_D_STRING);
			logger.debug("DEV: " + s);
			s = vxiConnector.send_and_receive(deviceLink,
					SELECT_INSTRUMENT + " " + device);
			logger.debug("DEV: " + s);
		}
	}

	/**
	 * Selects a device by its primary/secondary GPIB address. Used with serial
	 * gpib communication.
	 * 
	 * @param device
	 *            Device name
	 * @throws Exception
	 */
	public void selectDevice(int primary, int secondary) throws Exception {
		if (adapterType == ADAPTER_SERIAL_GPIB) {
			String s = vxiConnector.send_and_receive(deviceLink,
					".a " + primary + " " + secondary);
			logger.debug("DEV: " + s);
		}
		if (adapterType == ADAPTER_PROLOGIX) {
			// Prologix wants for secondary adress: gpib-adress+96
			int realAdr = secondary + 96;
			// port.writeNoAnswer("++addr " + primary + " " + realAdr);
		}
	}

	// public List<vxiConnector> listDevices(boolean fakeResult) throws
	// IOException {
	// selectDevice(9,0);
	// String s;
	// if (fakeResult) {
	// s =
	// "+1,-1,+4095,+1300,-1,+0,HYB,NONE,#H00000000,#H00000000,READY,,,,SYSTEM
	// INSTALLED AT SECONDARY ADDR 0;+24,+0,+4095,+65344,-1,+0,REG,A16
	// ,#H00000000,#H00000000,READY,,,,VOLTMTR INSTALLED AT SECONDARY ADDR
	// 3;+25,+0,+4095,+65280,-1,+0,REG,A16
	// ,#H00000000,#H00000000,READY,,,,VOLTMTR INSTALLED AT SECONDARY ADDR
	// 3;+26,+0,+4095,+65280,-1,+0,REG,A16
	// ,#H00000000,#H00000000,READY,,,,VOLTMTR INSTALLED AT SECONDARY ADDR
	// 3;+64,+0,+4095,+65300,-1,+0,REG,A16
	// ,#H00000000,#H00000000,READY,,,,SWITCH INSTALLED AT SECONDARY ADDR
	// 8;+72,+0,+4095,+65296,-1,+0,REG,A16
	// ,#H00000000,#H00000000,READY,,,,SWITCH INSTALLED AT SECONDARY ADDR
	// 9;+80,+0,+4095,+65440,-1,+0,REG,A16 ,#H00000000,#H00000000,READY,,,,ARB
	// INSTALLED AT SECONDARY ADDR 10;+144,+0,+4095,+65361,-1,+0,REG,A16
	// ,#H00000000,#H00000000,READY,,,,DIG_I/O INSTALLED AT SECONDARY ADDR 18";
	// } else {
	// s = vxiConnector.send_and_receive(deviceLink, "VXI:CONF:DLIS?");
	// }
	// //System.out.println(s);
	// List<vxiConnector> devices = new ArrayList<vxiConnector>();
	// String lines[] = s.split(";");
	// for (String l : lines) {
	// // System.out.println(l);
	// vxiConnector dev = vxiConnector.materializeFrom(l);
	// log.info("Device found: " + dev.getName());
	// devices.add(dev);
	// }
	// return devices;
	// }

	/**
	 * Opens mainframe. Communication port is opened.
	 * 
	 * @return
	 */
	// public boolean open() {
	// boolean o = port.open();
	// try {
	// if (adapterType == ADAPTER_SERIAL_DIRECT) {
	// initializeSerialProtocol();
	// }
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	// return o;
	// }

	/**
	 * Closes mainframe. Communication port is closed (and thus released).
	 * 
	 * @return
	 */
	// public void close() {
	// port.close();
	// }

	/**
	 * Checks if mainframe is open. Opening a mainframe two times is an error.
	 * 
	 * @return true if open.
	 */
	// public boolean isOpen() {
	// return port.isOpen();
	// }

	/**
	 * "Starts" mainframe. This means a thread is opened which listens to serial
	 * port.
	 * 
	 */
	// public void start() {
	// port.start();
	// }

	/**
	 * Checks if mainframe is open. Starting a mainframe two times is an error.
	 * 
	 * @return true if started.
	 * 
	 */
	// public boolean isStarted() {
	// return port.isStarted();
	// }

	/**
	 * Writes a string to mainframe communication port. Honors behaviour of
	 * different adapter types regarding returning a string or nothing at all.
	 * 
	 * @param cmd
	 *            command to send.
	 * @throws Exception
	 */
	protected void writeToPort(String cmd) throws Exception {
		if (getAdapterType() == HP1300b.ADAPTER_SERIAL_GPIB
				|| getAdapterType() == HP1300b.ADAPTER_SERIAL_DIRECT) {
			String s = writeWithAnswer(cmd);
			logger.debug("DEV: " + s);
		}
		if (getAdapterType() == HP1300b.ADAPTER_PROLOGIX) {
			writeNoAnswer(cmd);
		}
	}

	/**
	 * Writes a command string to the mainframe using the communication port. Do
	 * not wait for answer.
	 * 
	 * @param s
	 *            Command string
	 * @throws Exception
	 */
	public void writeNoAnswer(String s) throws Exception {
		vxiConnector.send(deviceLink, s);
	}

	/**
	 * Writes a command string to the mainframe using the communication port and
	 * wait for an answer.
	 * 
	 * @param s
	 *            Command string
	 * @return Answer from mainframe.
	 * @throws Exception
	 */
	public String writeWithAnswer(String s) throws Exception {
		return vxiConnector.send_and_receive(deviceLink, s);
	}

	public int getAdapterType() {
		return adapterType;
	}

	public void setAdapterType(int adapterType) {
		this.adapterType = adapterType;
	}

	/*
	 * =========================================================================
	 * = =========== code copied frm samofab NEEDS REWORK
	 */
	public void UploadBinaryData(String command, byte[] data) throws Exception {
		byte[] processedData = GetUploadReadyBytes(data);
		int size = processedData.length;
		int sizeDigits = Integer.toString(size).length();
		String definiteBlockHeader = "#" + Integer.toString(sizeDigits)
				+ Integer.toString(size);

		vxiConnector.send_and_receive(deviceLink,
				command + definiteBlockHeader);
		// XXX vxiConnector.send_and_receive(deviceLink,
		// Byte.toString(processedData));
		vxiConnector.send_and_receive(deviceLink, "\r"); // if using indefinite
															// block, sent !\r
	}

	private static byte[] GetUploadReadyBytes(byte[] input) {
		byte[] result = new byte[input.length * 2];
		int i = 0;
		for (byte c : input) {
			char msb = (char) ((c & 0xF0) >> 4);
			char lsb = (char) (c & 0x0F);
			// var checkMsb = _check[msb] << 4;
			// var checkLsb = _check[lsb] << 4;
			// result[i++] = (byte)(msb | checkMsb | 0x80); // msbfirst
			// result[i++] = (byte)(lsb | checkLsb | 0x80);
		}
		return result;
	}

}
