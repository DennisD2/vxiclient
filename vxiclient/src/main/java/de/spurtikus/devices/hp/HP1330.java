package de.spurtikus.devices.hp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.spurtikus.vxi.connectors.DeviceLink;
import de.spurtikus.vxi.connectors.VXIConnector;

/**
 * 
 * HP/Agilent/Keysight E1330 Digital I/O control.
 * 
 * Currently implemented functions:
 * 
 *
 * @author dennis
 * 
 */
public class HP1330 extends BaseHPDevice {
	static Logger logger = LoggerFactory.getLogger(HP1330.class);

	public static enum Port {
		DATA0, DATA1, DATA2, DATA3
	}

	public static enum Bit {
		BIT0, BIT1, BIT2, BIT3, BIT4, BIT5, BIT6, BIT7
	}

	/**
	 * Line polarity
	 * 
	 * @author dennis
	 *
	 */
	public static enum Polarity {
		POS, NEG
	}

	/**
	 * Control, Flag Data Lines
	 * 
	 * @author dennis
	 *
	 */
	public static enum Line {
		CONT, FLAG, DATA
	}

	/**
	 * Describes a bit in a port.
	 * 
	 * @author dennis
	 *
	 */
	public static class PortDescription {
		private Port port; // DATA0, DATA1, ...
		private Bit bitPos; // BIT0..BIT7
		private Line line; // CONT, FLAG, DATA

		protected void init(Port port, Bit bitPos, Line line) {
			this.port = port;
			this.bitPos = bitPos;
			this.setLine(line);
		}

		public PortDescription() {
		}

		public PortDescription(Port port, Bit bitPos) {
			init(port, bitPos, Line.DATA);
		}

		public PortDescription(Port port, Bit bitPos, Line line) {
			init(port, bitPos, line);
		}

		public Port getPort() {
			return port;
		}

		public void setPort(Port port) {
			this.port = port;
		}

		public Bit getBitPos() {
			return bitPos;
		}

		public void setBitPos(Bit bitPos) {
			this.bitPos = bitPos;
		}

		public Line getLine() {
			return line;
		}

		public void setLine(Line line) {
			this.line = line;
		}
		
		@Override
		public String toString() {
			return line + "." + port + "." + bitPos;
		}
	}

	/**
	 * CTR. Requires vxiConnector object as parameter.
	 * 
	 * @param parent
	 *            VXI connector
	 * @param link
	 *            device link
	 */
	public HP1330(VXIConnector parent, DeviceLink link) {
		super(parent,link);
	}

	/**
	 * Initializes device. Sets up some useful start configuration for DMM.
	 * @throws Exception 
	 */
	public void initialize() throws Exception {
		vxiConnector.send(deviceLink, "*RST;*OPC?");
		sleep(100);
	}

	protected String createBitPortString(PortDescription bit) {
		return /* bit.line + ":" + */ bit.port + ":" + bit.bitPos;
	}

	protected String createBytePortString(PortDescription bit) {
		return /* bit.line + ":" + */ "" + bit.port;
	}

	/**
	 * Read a bit
	 * 
	 * @param bit
	 *            bit description to read
	 * @return bit value
	 * @throws Exception 
	 */
	public boolean getBit(PortDescription bit) throws Exception {
		String s = vxiConnector.send_and_receive(deviceLink, "MEAS:DIG:" + createBitPortString(bit) + "?");
		logger.debug("DEV: " + s);
		boolean b = false, found = false;
		if (s.startsWith("1")) {
			b = true;
			found = true;
		}
		if (s.startsWith("0")) {
			b = false;
			found = true;
		}
		if (!found) {
			logger.error("Cannot decode boolean value");
		}
		return b;
	}

	/**
	 * Read a byte
	 * 
	 * @param bit
	 *            byte description to read
	 * @return byte value
	 * @throws Exception 
	 */
	public byte getByte(PortDescription bit) throws Exception {
		String s = vxiConnector.send_and_receive(deviceLink, "MEAS:DIG:" + createBytePortString(bit) + "?");
		logger.debug("DEV: " + s);
		byte b = Byte.parseByte(s);
		return b;
	}

	/**
	 * Set a bit
	 * 
	 * @param bit
	 *            bit description to set
	 * @param value
	 *            value to set
	 * @throws Exception 
	 */
	public Boolean setBit(PortDescription bit, boolean value) throws Exception {
		int v = value ? 1 : 0;
		String s = vxiConnector.send_and_receive(deviceLink, 
				"DIG:" + createBitPortString(bit) + " " + v + ";*OPC?");
		logger.debug("DEV: " + s);
		if (s.startsWith("1")) return true;
		if (s.startsWith("0")) return false;
		return null;
	}

	/**
	 * Set a byte
	 * 
	 * @param bit
	 *            byte description to set
	 * @param value
	 *            value to set
	 * @throws Exception 
	 */
	public void setByte(PortDescription bit, byte value) throws Exception {
		String s = vxiConnector.send_and_receive(deviceLink, 
				"DIG:" + createBytePortString(bit) + " " + value + ";*OPC?");
		logger.debug("DEV: " + s);
	}

	public void setPolarity(PortDescription bit, Polarity polarity)
			throws Exception {
		String s = vxiConnector.send_and_receive(deviceLink, 
				"DIG:" + createBytePortString(bit) + ":POL " + polarity);
		logger.debug("DEV: " + s);
	}

	// multiple ports... IN and OUT

	// USING trace memory

}
