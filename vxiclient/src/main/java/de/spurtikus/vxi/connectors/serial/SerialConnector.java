package de.spurtikus.vxi.connectors.serial;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.spurtikus.vxi.connectors.ConnectorConfig;
import de.spurtikus.vxi.connectors.DeviceLink;
import de.spurtikus.vxi.connectors.VXIConnector;
import de.spurtikus.vxi.util.CommunicationUtil;
import de.spurtikus.vxi.util.ConversionUtil;
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

/**
 * Communication with a serial port.
 * 
 * 
 * Flow control:
 * 
 * * NONE: Class Communication does no flow control itself in this case. The
 * underlying driver is also configured to operate w/o flow control. This mode
 * may loose data during transfer, but is ok for small amounts of data.
 * 
 * * Driver XON/XOFF: The underlying serial port is configured and operated by
 * the driver. Class Communication does no flow control itself in this case.
 * This seems to work with rxtx library well on some kbyte of data, but not well
 * on larger transmissions.
 * 
 * * Programmatic XON/XOFF: the underlying serial port is configured w/o flow
 * control. The class Communication does XON/XOFF handling itself.
 * 
 * * Hardware CTS/RTS: ?
 * 
 * @author dennis
 *
 */
public class SerialConnector implements VXIConnector {
	Logger logger = LoggerFactory.getLogger(SerialConnector.class);

	/** Singleton */
	static SerialConnector INSTANCE = null;

	/** Config to use */
	private SerialConnectorConfig theConfig;

	public static final char LF = 0xa;
	public static final char CR = 0xd;

	public static final char XON = 0x11;
	public static final char XOFF = 0x13;

	private CommPortIdentifier serialPortId;
	private SerialPort serialPort;
	private OutputStream outputStream;
	protected Boolean open = false;

	/** true if port is started. */
	protected volatile Boolean started = false;

	private SerialReader serialReader;
	private Thread serialReaderThread;

	private static final int WAIT_TIME_AFTER_SEND = 1;

	private SerialConnector() {
		// Singleton

		// Initialize error array
		// RPCErrors.initErrorMap();
	}

	public static VXIConnector getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new SerialConnector();
		}
		return INSTANCE;
	}

	@Override
	public DeviceLink initialize(ConnectorConfig config) throws Exception {
		if (started) {
			logger.info("port is already started");
		}
		theConfig = (SerialConnectorConfig) config;

		open();
		start();

		DeviceLink link = new DeviceLink(serialPort);
		if (theConfig
				.getAdapterType() == SerialConnectorConfig.ADAPTER_PROLOGIX) {
			String s;
			s = send_and_receive(link, "++ver");
			logger.debug("DEV: " + s);
			// s = port.writeWithAnswer("++auto");
			// log.xxx("DEV: " + s);
			selectDevice(link, 9, 0);
		}
		return link;
	}

	/**
	 * Write string to port
	 * 
	 * String is written to port.
	 * 
	 * @param c
	 *            char to be written
	 * @throws IOException
	 */
	@Override
	public void send(DeviceLink link, String message) throws Exception {
		if (message == null) {
			return;
		}
		logger.debug("send(\"" + message + "\")");
		write(ConversionUtil.toBytes(message + "\n\r"));
	}

	public void write(byte[] s) throws IOException {
		if (s == null)
			return;
		// logger.debug("write(\"" + new String(s) + "\")");
		if (outputStream == null) {
			logger.error("Output stream not initialized when writing data");
			return;
		}
		for (int i = 0; i < s.length; i++) {
			// wait until a xoff phase ends
			while (serialReader.xoffReceived()) {
				sleep(WAIT_TIME_AFTER_SEND);
			}
			outputStream.write((int) s[i]);
		}
	}

	@Override
	public String receive(DeviceLink link) throws Exception {
		// wait for complete answer (EOL)
		while (!serialReader.eol()) {
			sleep(WAIT_TIME_AFTER_SEND);
		}
		// return answer collected in serialReader
		return serialReader.readAndClearBuffer("");
	}

	@Override
	public String send_and_receive(DeviceLink link, String message)
			throws Exception {
		logger.debug("send_and_receive(\"" + message + "\")");
		serialReader.clearReaderBuffer();
		send(link, message + "\n\r");

		String answer = receive(link);
		return answer.replace(message, "");
	}

	/**
	 * Starts communication
	 * 
	 * Start the thread. See run() what is happening in thread.
	 */
	public void start() {
		if (started) {
			logger.info("port is already started");
			return;
		}
		// start this thread, see run()
		serialReaderThread = new Thread(serialReader);
		serialReaderThread.start();
		started = true;
		logger.debug("port started");
	}

	public void stop() {
		if (!started) {
			logger.info("port is not started");
			return;
		}
		logger.debug("stop port");
		// serialReaderThread.stop();
		started = false;
	}

	/**
	 * Opens a port. Adds event listeners.
	 * 
	 * @return true on success, false otherwise.
	 */
	public boolean open() {
		Boolean foundPort = false;

		if (open) {
			logger.info("port is already open");
			return false;
		}
		logger.debug("open port");

		// search for this port in list of available ports
		Enumeration<?> enumComm = CommPortIdentifier.getPortIdentifiers();
		while (enumComm.hasMoreElements()) {
			serialPortId = (CommPortIdentifier) enumComm.nextElement();
			if (theConfig.getPort().contentEquals(serialPortId.getName())) {
				foundPort = true;
				break;
			}
		}
		if (!foundPort) {
			logger.error("port " + theConfig.getPort() + " not found");
			return false;
		}

		// open port
		try {
			serialPort = (SerialPort) serialPortId.open("de.spurtikus.comm",
					500);
		} catch (PortInUseException e) {
			logger.error("port in use.");
			return false;
		}

		// get output stream for that port (used for writing to serial port)
		try {
			outputStream = serialPort.getOutputStream();
		} catch (IOException e) {
			logger.error("cannot access OutputStream");
			return false;
		}

		// set port configuration
		try {
			serialPort.setSerialPortParams(theConfig.getBaudRate(),
					theConfig.getDataBits(), theConfig.getStopBits(),
					theConfig.getParity());
		} catch (UnsupportedCommOperationException e) {
			logger.error("cannot set port configuration");
			return false;
		}

		if (/* inputStream == null || */outputStream == null) {
			logger.error("Failed in getting input or output stream");
		}

		// serialPort.setOutputBufferSize(80000);
		// serialPort.setInputBufferSize(80000);
		try {
			switch (theConfig.getProtocol()) {
			case CTSRTS:
				serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN
						| SerialPort.FLOWCONTROL_RTSCTS_OUT);
				serialPort.setRTS(true);
				logger.info("Flow control PC side: RTS/CTS");
				break;
			case XONXOFF:
				serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_XONXOFF_IN
						| SerialPort.FLOWCONTROL_XONXOFF_OUT);
				logger.info("Flow control PC side: Xon/Xoff");
				break;
			default:
				logger.info("Flow control PC side: None");
				// do nothing
			}
		} catch (UnsupportedCommOperationException e) {
			logger.error("cannot set flow control");
			return false;
		}

		// setup serial reader
		serialReader = new SerialReader(serialPort);
		// serialReader.setupListener(serialPort);

		// all good
		logger.debug("Port opened.");
		open = true;
		return true;
	}

	/**
	 * Closes port
	 * 
	 * closes port, set CPS back to zero.
	 * 
	 */
	public void close() {
		if (open) {
			// log.xxx("closing port.");
			// serialPort.close();
			open = false;
		} else {
			logger.info("port is already closed.");
		}
		logger.debug("Port closed.");
	}

	/**
	 * * @deprecated
	 * 
	 * @param s
	 * @return
	 * @throws IOException
	 */
	public byte[] writeWithAnswer(byte[] s) throws IOException {
		logger.debug(
				"byte writeWithAnswer(\"" + ConversionUtil.toString(s) + "\")");
		serialReader.clearReaderBuffer();
		byte[] to = new byte[s.length + 1];
		CommunicationUtil.copyByteArray(s, to);
		// to[s.length] = '\n';
		to[s.length] = '\r';
		write(to);
		// wait for complete answer (EOL)
		while (!serialReader.eol()) {
			sleep(WAIT_TIME_AFTER_SEND);
		}
		// return answer collected in serialReader
		return serialReader.readAndClearBuffer(s);
	}

	/**
	 * Returns available serial ports.
	 * 
	 * @return hash map with all available ports
	 */
	public List<CommPortIdentifier> getPorts() {
		List<CommPortIdentifier> ports = new ArrayList<CommPortIdentifier>();
		Enumeration<?> thePorts = CommPortIdentifier.getPortIdentifiers();
		while (thePorts.hasMoreElements()) {
			CommPortIdentifier com = (CommPortIdentifier) thePorts
					.nextElement();
			switch (com.getPortType()) {
			case CommPortIdentifier.PORT_SERIAL:
				try {
					CommPort thePort = com.open("CommUtil", 50);
					thePort.close();
					ports.add(com);
				} catch (PortInUseException e) {
					logger.error("Port, " + com.getName() + ", is in use.");
				} catch (Exception e) {
					logger.error("Failed to open port " + com.getName());
					e.printStackTrace();
				}
			}
		}
		return ports;
	}

	public Boolean isStarted() {
		return started;
	}

	public Boolean isOpen() {
		return open;
	}

	public void UploadBinaryData(String string, byte[] demo) {
		throw new UnsupportedOperationException("NOT IMPLEMENTED");
	}

	/**
	 * Sleep ms milliseconds.
	 * 
	 * @param ms
	 *            milliseconds value to sleep
	 * @deprecated see other impl.
	 */
	protected void sleep(int ms) {
		// sleep - allow interrupts to be serviced
		try {
			Thread.sleep(ms);
		} catch (InterruptedException e) {
			logger.error("Can't sleep");
			e.printStackTrace();
		}
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
			send(link, "++addr " + primary + " " + realAdr );
		}
	}

}
