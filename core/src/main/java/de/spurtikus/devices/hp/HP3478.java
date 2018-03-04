package de.spurtikus.devices.hp;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.spurtikus.vxi.connectors.DeviceLink;
import de.spurtikus.vxi.connectors.VXIConnector;
import de.spurtikus.vxi.util.ConversionUtil;

/**
 * HP/Agilent/Keysight 3478 standalone Digital Multimeter (DMM) control.
 * 
 * Currently implemented measurements / functions:
 * 
 * 
 * @author dennis
 *
 */
public class HP3478 extends BaseHPDevice {
	static Logger logger = LoggerFactory.getLogger(HP3478.class);

	/**
	 * Meter functions
	 * 
	 * @author dennis
	 */
	public enum Function {
		// @formatter:off
		UNDEFINED(0, "UNDEFINED"), 
		DC_VOLTAGE(1, "1"), 
		AC_VOLTAGE(2, "2"), 
		TWO_WIRE_OHMS(3,	"3"), 
		FOUR_WIRE_OHMS(4, "4"), 
		DC_CURRENT(5, "5"), 
		AC_CURRENT(6, "6"), 
		EXTENDED_OHMS(7, "7");
		// @formatter:on

		private int key = 0;

		private String value;

		private Function(int key, String value) {
			if (key < 1 || key > 7) {
				// log.xxx("Illegal Function value");
				key = 0;
			}
			this.key = key;
			this.value = value;
		}

		public int getKey() {
			return key;
		}

		public String getValue() {
			return value;
		}
	};

	/**
	 * Meter ranges
	 *
	 * @author dennis
	 */
	public enum Range {
		// @formatter:off
		UNDEFINED(0, "UNDEFINED"), 
		Auto(1, "A"), 
		MV30(2, "-2"), 
		MV300(3, "-1"), 
		V3(4, "0"), 
		V30(5, "1"), 
		V300(6, "2"), 
		KOHMS3(7, "3"), 
		KOHMS30(8, "4"), 
		KOHMS300(9, "5"), 
		MOHMS3(10, "6"), 
		MOHMS30(11, "7");
		// @formatter:on

		private int key = 0;

		private String value;

		private Range(int key, String value) {
			if (key < 1 || key > 11) {
				// log.xxx("Illegal Range value");
				key = 0;
			}
			this.key = key;
			this.value = value;
		}

		public int getKey() {
			return key;
		}

		public String getValue() {
			return value;
		}
	};

	/**
	 * Autozero values
	 *
	 * @author dennis
	 */
	public enum AutoZero {
		// @formatter:off
		UNDEFINED(0, "UNDEFINED"), 
		Off(1, "0"), 
		On(2, "1");
		// @formatter:on

		private int key = 0;

		private String value;

		private AutoZero(int key, String value) {
			if (key < 1 || key > 2) {
				// log.xxx("Illegal Range value");
				key = 0;
			}
			this.key = key;
			this.value = value;
		}

		public int getKey() {
			return key;
		}

		public String getValue() {
			return value;
		}
	};

	/**
	 * Number of digits
	 *
	 * @author dennis
	 */
	public enum Digits {
		// @formatter:off
		UNDEFINED(0, "UNDEFINED"), 
		DIGITS3(1, "3"), 
		DIGITS4(2, "4"),
		DIGITS5(2, "5");
		// @formatter:on

		private int key = 0;

		private String value;

		private Digits(int key, String value) {
			if (key < 1 || key > 2) {
				// log.xxx("Illegal Function value");
				key = 0;
			}
			this.key = key;
			this.value = value;
		}

		public int getKey() {
			return key;
		}

		public String getValue() {
			return value;
		}
	};

	/**
	 * Meter trigger modes
	 *
	 * @author dennis
	 */
	public enum Trigger {
		// @formatter:off
		UNDEFINED(0, "UNDEFINED"), 
		INTERNAL(1, "1"), 
		EXTERNAL(2, "2"), 
		SINGLE(3, "3"), 
		HOLD(4, "4"), 
		FAST(5, "5");
		// @formatter:on

		private int key = 0;

		private String value;

		private Trigger(int key, String value) {
			if (key < 1 || key > 7) {
				// log.xxx("Illegal Function value");
				key = 0;
			}
			this.key = key;
			this.value = value;
		}

		public int getKey() {
			return key;
		}

		public String getValue() {
			return value;
		}
	};

	/**
	 * CTR. Requires vxiConnector object as parameter.
	 * 
	 * @param parent
	 *            VXI connector
	 * @param link
	 *            device link
	 */
	public HP3478(VXIConnector parent, DeviceLink link) {
		super(parent, link);
	}

	/**
	 * Initializes device. Sets up some useful start configuration for DMM.
	 * 
	 * @throws Exception
	 */
	public void initialize() throws Exception {
		/*
		 * vxiConnector.send(deviceLink, "*RST"); vxiConnector.send(deviceLink,
		 * "ABOR"); vxiConnector.send(deviceLink, "CAL:LFR 50");
		 */
	}

	/**
	 * Initializes device for measurement.
	 * 
	 * @throws Exception
	 */
	public void initializeMeasurement(Function f, Range r, AutoZero z,
			Digits d, Trigger t) throws Exception {
		StringBuilder sb = new StringBuilder();
		// Function
		sb.append("F" + f.value);
		// Range
		sb.append("R" + r.value);
		// Auto zero
		sb.append("Z" + z.value);
		// Number of digits
		sb.append("N" + d.value);
		// Trigger mode
		sb.append("T" + t.value);
		
		logger.debug("Initialization string: " + sb.toString());
		vxiConnector.send(deviceLink, sb.toString());
	}

	/**
	 * Do a single measurement and return result as Double.
	 * 
	 * @return Measurement value.
	 * @throws Exception
	 */
	public Double measureSingle() throws Exception {
		String s = vxiConnector.send_and_receive(deviceLink, "T3");
		logger.trace("DEV: " + s);
		return ConversionUtil.stringToDouble(s);
	}

	/**
	 * CHeck if front panel inputs are selected.
	 * 
	 * @return True if front panel inputs are selected. false if back panel
	 *         inputs are selected.
	 * @throws Exception
	 */
	public boolean isFrontPanelSelected() throws Exception {
		String s = vxiConnector.send_and_receive(deviceLink, "S");
		logger.debug("DEV: " + s);
		return s.startsWith("1");
	}

	public void setDisplay(String s) throws Exception {
		logger.debug("set display text: " + s);
		// See p.60
		// Allowed for D: 1,2,3
		vxiConnector.send(deviceLink, "D2" + s);
	}

	public void homeCommand() {
		// H
		// 1..7
	}
	
	String getStatus() throws Exception {
		// See p.61
		String s = vxiConnector.send_and_receive(deviceLink, "B");
		// Byte 0,1,2,3
		return s;
	}
}
