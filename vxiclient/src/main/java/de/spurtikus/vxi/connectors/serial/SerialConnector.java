package de.spurtikus.vxi.connectors.serial;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.spurtikus.vxi.connectors.AbstractConnector;
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
public class SerialConnector extends AbstractConnector implements VXIConnector {
	Logger logger = LoggerFactory.getLogger(SerialConnector.class);

	/** Singleton */
	static protected SerialConnector INSTANCE = null;

	/** Config to use */
	protected SerialConnectorConfig theConfig;

	private SerialPort serialPort;
	private OutputStream outputStream;
	private SerialReader serialReader;
	private Thread serialReaderThread;

	boolean initialized;

	private static final int WAIT_TIME_AFTER_SEND = 1;

	// Singleton
	protected SerialConnector() {}
	public static VXIConnector getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new SerialConnector();
		}
		return INSTANCE;
	}

	@Override
	public DeviceLink initialize(ConnectorConfig config, String deviceId) throws Exception {
		theConfig = (SerialConnectorConfig) config;

		if (!initialized) {
			// initialize physical connection and reader thread
			initializePhysicalPort();
			startReaderThread();
			initialized=true;
		}
		DeviceLink link = new DeviceLink(config);
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
		return answer.replace(message, "").replace("\n", "");
	}

	/**
	 * Starts communication
	 * 
	 * Start the reader thread. See run() what is happening in thread.
	 */
	public void startReaderThread() {
		// start this thread, see run()
		serialReaderThread = new Thread(serialReader);
		serialReaderThread.start();
		logger.debug("port started");
	}

	/**
	 * Opens a port, configures it and connects input stream, output stream and
	 * an event listener to it.
	 * 
	 * @throws Exception
	 */
	public void initializePhysicalPort() throws Exception {
		logger.debug("Open port");

		CommPortIdentifier serialPortId = getSerialPort(theConfig.getPort());
		if (serialPortId == null) {
			// We have problems with this port.
			throw new Exception("Unable to open or use port.");
		}

		// Open physical port
		serialPort = openPhysicalPort(serialPortId);
		// Connect input and output streams/input listener to physical port
		connectStreams(serialPort, serialPortId.getName());
		// Set physical port characteristics
		setPortMode(serialPortId);

		// All good
		logger.debug("Port opened.");
	}

	/**
	 * Gets CommPortIdentifier known to the RXTX Serial subsystem.
	 * 
	 * @param port
	 *            port to get identifier for, e.g. "/dev/tty0"
	 * @return true if port is known.
	 */
	protected CommPortIdentifier getSerialPort(String port) {
		Enumeration<?> enumComm = CommPortIdentifier.getPortIdentifiers();
		// search for this port in list of available ports
		while (enumComm.hasMoreElements()) {
			CommPortIdentifier serialPortId = (CommPortIdentifier) enumComm
					.nextElement();
			if (port.contentEquals(serialPortId.getName())) {
				return serialPortId;
			}
		}
		logger.error("port " + theConfig.getPort()
				+ " not found. Either it does not exist or is occupied by another process.");
		return null;
	}

	/**
	 * Opens physical port.
	 * 
	 * @param portId
	 * @return port
	 * @throws Exception
	 */
	protected SerialPort openPhysicalPort(CommPortIdentifier portId)
			throws Exception {
		SerialPort port = null;
		try {
			port = (SerialPort) portId.open("de.spurtikus.comm", 500);
		} catch (PortInUseException e) {
			logger.error("Port {} already in use.", portId.getName());
			throw new Exception("Port already in use.", e);
		}
		return port;
	}

	/**
	 * Connect streams and reader thread to physical port.
	 * 
	 * @param port physical port to use
	 * @param portId port id of physical port
	 * @throws Exception
	 */
	protected void connectStreams(SerialPort port, String portId)
			throws Exception {
		// Get output stream for the port (used for writing to serial port)
		try {
			outputStream = port.getOutputStream();
		} catch (IOException e) {
			logger.error("Cannot create output stream for port {}.", portId);
			throw new Exception("Cannot create output stream for port.", e);
		}
		if (outputStream == null) {
			logger.error("Failed in getting output stream (is null).");
		}

		// Setup serial reader
		serialReader = new SerialReader(port);
	}

	/**
	 * Sets physical port characteristics.
	 * 
	 * @param portId port id of physical port
	 * @throws Exception
	 */
	protected void setPortMode(CommPortIdentifier portId) throws Exception {
		// Set port configuration
		try {
			serialPort.setSerialPortParams(theConfig.getBaudRate(),
					theConfig.getDataBits(), theConfig.getStopBits(),
					theConfig.getParity());
		} catch (UnsupportedCommOperationException e) {
			logger.error("Cannot set port configuration for port {}.",
					portId.getName());
			throw new Exception("Cannot set port configuration for port.", e);
		}
		// serialPort.setOutputBufferSize(80000);
		// serialPort.setInputBufferSize(80000);

		// Set flow control
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
			logger.error("Cannot set flow control for port {}.",
					portId.getName());
			throw new Exception("Cannot set flow control for port.", e);
		}
	}

	/**
	 * Closes port
	 * 
	 * closes port
	 * 
	 */
	public void close() {
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


}
