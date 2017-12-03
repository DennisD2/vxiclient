package de.spurtikus.vxi.connectors.serial;

import java.io.IOException;
import java.io.InputStream;
import java.util.TooManyListenersException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.spurtikus.vxi.connectors.Constants;
import de.spurtikus.vxi.util.CommunicationUtil;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

public class SerialReader implements Runnable {
	Logger logger = LoggerFactory.getLogger(SerialReader.class);

	private SerialPort serial;
	private boolean open;
	private InputStream inputStream;

	private final int INPUT_BUFFER_SIZE = 100000; // 8192;

	/**
	 * Input buffer where subsequent inputs are collected
	 */
	private byte inputBuffer[] = new byte[INPUT_BUFFER_SIZE];
	/**
	 * Pointer to first free location in input buffer
	 */
	private int inputBufferPtr = 0;

	/**
	 * True if an Xoff was received.
	 */
	private boolean Xoff_received = false;

	/**
	 * True if an EOL (Communication.LF) was received.
	 */
	private volatile boolean endOfLineReceived = false;

	/** current bytes per seconds value for running transmission */
	private int bytesPerSecond;
	/**
	 * last bytes per seconds value for current transmission, more stable value
	 */
	private int bytesPerSecondSave = 0;
	/**
	 * total bytes transfered value for running transmission since last START
	 */
	private long totalBytes = 0;

	/**
	 * serialPortEventListener
	 * 
	 * This is the event listener that is added to the serial port in open().
	 * For incoming data the listener is called. The listener itself calls
	 * read() to read the data if the current event equals
	 * SerialPortEvent.DATA_AVAILABLE. All other events are ignored so far.
	 * 
	 */
	class MySerialPortEventListener implements SerialPortEventListener {
		public void serialEvent(SerialPortEvent event) {
			// log.xxx("serialPortEventlistener");
			switch (event.getEventType()) {
			case SerialPortEvent.DATA_AVAILABLE:
				read();
				break;
			case SerialPortEvent.BI:
			case SerialPortEvent.CD:
			case SerialPortEvent.CTS:
			case SerialPortEvent.DSR:
			case SerialPortEvent.FE:
			case SerialPortEvent.OE:
			case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
			case SerialPortEvent.PE:
			case SerialPortEvent.RI:
			default:
				logger.info("RS232 Event " + event.getEventType());
			}
		}
	}

	public SerialReader(SerialPort serialPort) {
		serial = serialPort;
	}

	/**
	 * Thread run()
	 * 
	 * While the port is open, we enter a loop that sleeps for 1 second and then
	 * updates statistics data.
	 */
	@Override
	public void run() {
		logger.info("SerialReader run()");
		// statistic values
		bytesPerSecond = 0;
		bytesPerSecondSave = 0;
		totalBytes = 0;

		setupListener(serial);

		while (open) {
			try {
				// update value (Bytes Per Second)
				bytesPerSecondSave = bytesPerSecond;
				// reset local var
				bytesPerSecond = 0;
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				logger.error("Thread interrupted exception");
			}
		}
	}

	public void stop() {
		bytesPerSecondSave = 0;
		bytesPerSecond = 0;

	}

	public boolean setupListener(SerialPort serialPort) {
		// get input stream for that port (used for reading from serial port)
		try {
			inputStream = serialPort.getInputStream();
		} catch (IOException e) {
			logger.error("cannot access InputStream");
			return false;
		}
		// add event listener for incoming data (read listener)
		try {
			serialPort.addEventListener(new MySerialPortEventListener());
		} catch (TooManyListenersException e) {
			logger.error("TooManyListenersException for port");
			return false;
		}
		serialPort.notifyOnDataAvailable(true);
		return true;
	}

	/**
	 * Reads from port
	 * 
	 * while data is available, read that data in and collect it into
	 * inputBuffer. Update statistics vars.
	 * 
	 */
	void read() {
		int estimated;
		try {
			estimated = inputStream.available();
			if (estimated == 0) {
				return;
			}

			byte[] data = new byte[estimated];
			int numRead = inputStream.read(data, 0, estimated);
			if (numRead == -1) {
				return;
			}
			if (numRead == 0) {
				return;
			}
			// log.xxx("read: " + numRead);
			byte inputBytes[] = new byte[numRead];

			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < numRead; i++) {
				byte c = data[i];
				inputBytes[i] = c;
				if (c >= 32 && c < 127) {
					sb.append((char) c);
				}
				// special chars
				switch (c) {
				case Constants.CR:
					logger.debug("CR received");
					sb.append((char) c);
					break;
				case Constants.LF:
					logger.debug("LF received");
					endOfLineReceived = true;
					sb.append((char) c);
					break;
				case Constants.XON:
					logger.debug("Xon received");
					Xoff_received = false;
					break;
				case Constants.XOFF:
					logger.debug("Xoff received");
					Xoff_received = true;
					break;
				}
			}
			//log.debug(sb.toString());
			//CommunicationUtil.dumpByteArray(inputBytes, numRead);
			// collect bytes read in
			appendBytes(inputBytes, numRead);
		} catch (IOException e) {
			logger.error("error while reading data");
		}
	}

	/**
	 * Append bytes from inputBytes array to global inputBuffer.
	 * 
	 * @param inputBytes
	 *            bytes to collect
	 * @param n
	 *            number of bytes to collect
	 */
	private void appendBytes(byte[] inputBytes, int n) {
		// log.xxx("collect: " + n + " bytes: " +
		// CommunicationUtil.asString(inputBytes));
		for (int i = 0; i < n; i++) {
			inputBuffer[inputBufferPtr + i] = inputBytes[i];
		}
		inputBufferPtr += n;

		// statistics update
		totalBytes += n;
		bytesPerSecond += n;
	}

	public boolean xoffReceived() {
		return Xoff_received;
	}

	public void clearReaderBuffer() {
		inputBufferPtr = 0;
		endOfLineReceived = false;
	}

	public String getBuffer() {
		return CommunicationUtil.asString(inputBuffer, inputBufferPtr);
	}

	public String readAndClearBuffer(String s) {
		String output = CommunicationUtil.asString(inputBuffer, inputBufferPtr).replace(s, "");

		clearReaderBuffer();

		return output;
	}

	public byte[] readAndClearBuffer(byte[] s) {
		// copy data from inputBytes to here
		byte output[] = new byte[inputBufferPtr];
		CommunicationUtil.copyByteArray(inputBuffer, output, inputBufferPtr);

		clearReaderBuffer();

		return output;
	}

	/**
	 * Returns true if the EOL char was received. See read() where that flag is
	 * set. See clearBuffer() where this flag is reset.
	 * 
	 * @return true if the EOL char was received.
	 */
	public boolean eol() {
		// System.out.println("inputBufferPtr: " + inputBufferPtr);
		return endOfLineReceived;
	}

}
