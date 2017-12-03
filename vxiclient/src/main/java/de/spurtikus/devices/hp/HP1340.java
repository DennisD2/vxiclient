package de.spurtikus.devices.hp;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.spurtikus.Timer;
import de.spurtikus.vxi.Constants;
import de.spurtikus.vxi.connectors.DeviceLink;
import de.spurtikus.vxi.connectors.VXIConnector;

/**
 * 
 * HP/Agilent/Keysight E1340 Arbitrary Function Generator (AFG) control.
 * 
 * Currently implemented functions:
 * 
 * 
 * *LRN?
 * 
 * RST;
 * 
 * *EMC +0;
 * 
 * :CAL:STAT:AC 1;
 * 
 * :ARB:DAC:SOUR INT;
 * 
 * :VOLT:AMPL:UNIT:VOLT V;
 * 
 * :ROSC:FREQ :EXT +4.294967296E+007;
 * 
 * :ROSC:SOUR INT;
 * 
 * GATE:STAT 0;
 * 
 * :FREQ:FIX +1.000000000E+004;
 * 
 * FSK +1.000000000E+004,+1.500000000E+007;
 * 
 * :RAMP:POL NORM;
 * 
 * :VOLT:AMPL +0.00000000E+000;
 * 
 * OFFS +0 .00000000E+000;
 * 
 * :OUTP:LOAD +5.00000000E+001;
 * 
 * :ARM:COUN +9.90000000E+037;
 * 
 * LAY2:COUN +1.00000000E+000;
 * 
 * SLOP POS;
 * 
 * SOUR IMM;
 * 
 * :MARK:FEED "SEGM";
 * 
 * POL NORM;
 * 
 * :FUNC:USER NON E
 *
 * @author dennis
 * 
 */
public class HP1340 extends BaseHPDevice {
	static Logger logger = LoggerFactory.getLogger(HP1340.class);

	/**
	 * Minimum amplitude [V]
	 */
	private static final double MIN_AMPLITUDE = -5.12;
	/**
	 * Max. amplitude [V]
	 */
	private static final double MAX_AMPLITUDE = 5.1175;

	private static final double MIN_SWEEP_FREQUENCY = 0.0;
	private static final double MAX_SWEEP_FREQUENCY_SINE = 15.0; // Mhz
	private static final double MAX_SWEEP_FREQUENCY_ARBITRARY = 15.0; // Mhz
	private static final double MAX_SWEEP_FREQUENCY_OTHER = 1.0; // Mhz

	public boolean isArmed;

	/**
	 * True if AFG is started.
	 */
	private boolean started;

	/**
	 * Waveform frequency
	 */
	private Double frequency;

	/**
	 * Waveform offset
	 */
	private Double offset;

	/**
	 * Waveform amplitude
	 */
	private Double amplitude;

	/**
	 * Waveform shape
	 */
	private StandardWaveForm shape;

	/**
	 * Standard waveforms
	 * 
	 * @author dennis
	 */
	public enum StandardWaveForm {
		// @formatter:off
		UNDEFINED(0, "UNDEFINED"), 
		DC(1, "DC"), 
		SINE(2, "SIN"), 
		TRIANGLE(3,	"TRI"), 
		SQUARE(4, "SQU"), 
		RAMP(5, "RAMP"), 
		USERA(6, "A"), 
		USERB(7, "B"), 
		USERC(8, "C"), 
		USERD(9, "D");
		// @formatter:on

		private int key = 0;

		private String value;

		private StandardWaveForm(int key, String value) {
			if (key < 1 || key > 9) {
				// log.xxx("Illegal WaveForm value");
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
	 * Builtin (EEPROM) waveforms
	 * 
	 * @author dennis
	 */
	public enum BuiltinWaveForm {
		// @formatter:off
		Undefined(0, "UNDEFINED"), 
		Sine(1, "EEPRom1"), 
		Triangle(2, "EEPRom2"), 
		Sinx_per_x(3, "EEPRom3"), 
		Haversine(4, "EEPRom4"), 
		Square(5, "EEPRom5"), 
		Square_First_10(6, "EEPRom6"), 
		Square_First_4(7, "EEPRom7"), 
		Ramp_Falling(8, "EEPRom8"), 
		Ramp_Falling_First_20(9, "EEPRom9"), 
		Ramp_Rising(10, "EEPRom10"), 
		Ramp_Rising_First_20(11, "EEPRom11"), 
		White_Noise(12, "EEPRom12"), 
		White_Noise_Modulated(13, "EEPRom13"), 
		Harmonic_Chord_3rd_4th_5th(14, "EEPRom14"), 
		Sine_Linear_Rising_8_cycles(15, "EEPRom15"), 
		Sine_Positive_Half_Cycle(16, "EEPRom16");
		// @formatter:on

		private int key = 0;

		private String value;

		private BuiltinWaveForm(int key, String value) {
			if (key < 1 || key > 16) {
				// log.xxx("Illegal WaveForm value");
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
	 * Marker feed
	 * 
	 * @author dennis
	 */
	public enum MarkerFeedType {
		// @formatter:off
		UNDEFINED(0, "UNDEFINED"), 
		OUTPUT_ZERO(1, "OUTP:ZERO"),
		SEGMENT(1, "SEGM"),
		SOURCE_ROSC(2, "SOUR:ROSC"),
		SOURCE_SWEEP(3, "SOUR:SWE");
		// @formatter:on

		private int key = 0;

		private String value;

		private MarkerFeedType(int key, String value) {
			if (key < 1 || key > 16) {
				// log.xxx("Illegal WaveForm value");
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
	 * Waveform polarity
	 * 
	 * @author dennis
	 */
	public enum PolarityType {
		// @formatter:off
		UNDEFINED(0, "UNDEFINED"), 
		NORM(1, "NORM"),
		INV(2, "INV");
		// @formatter:on

		private int key = 0;

		private String value;

		private PolarityType(int key, String value) {
			if (key < 1 || key > 16) {
				// log.xxx("Illegal WaveForm value");
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
	 * CTR. Requires mainframe object as parameter.
	 * 
	 * @param parent
	 *            Mainframe containing this device.
	 */
	public HP1340(VXIConnector parent, DeviceLink link) {
		super(parent, link);
	}

	/**
	 * Initializes device. Sets up some useful start configuration for DMM.
	 * 
	 * @throws Exception
	 */
	public void initialize() throws Exception {
		vxiConnector.send(deviceLink, "*RST;*CLS;*OPC?");
		vxiConnector.send(deviceLink, "ABOR");
		vxiConnector.send(deviceLink, "SOUR:LIST:SEL NONE");
		vxiConnector.send(deviceLink, "CAL:LFR 50");
		started = false;
		sleep(100);
	}

	public boolean isRunning() {
		return started;
	}

	public void start() throws Exception {
		if (!started) {
			vxiConnector.send(deviceLink, "INIT:IMM");
			started = true;
		} else {
			logger.info("AFG already started.");
		}
	}

	public void stop() throws Exception {
		if (started) {
			vxiConnector.send(deviceLink, "ABOR");
			started = false;
		} else {
			logger.info("AFG not running.");
		}
	}

	public void setFrequency(Double frequency) throws Exception {
		this.frequency = frequency;
		String cmd = "SOUR:FREQ:FIX " + Double.toString(frequency) + ";";
		vxiConnector.send(deviceLink, cmd);
	}

	public void setOffset(Double offset) throws Exception {
		this.offset = offset;
		String cmd = "SOUR:VOLT:OFFS " + Double.toString(offset) + "V";
		vxiConnector.send(deviceLink, cmd);
	}

	public void setAmplitude(Double amplitude) throws Exception {
		this.amplitude = amplitude;
		String cmd = "SOUR:VOLT:LEV:IMM:AMPL " + Double.toString(amplitude)
				+ "V;";
		vxiConnector.send(deviceLink, cmd);
	}

	/**
	 * Set shape of StandardWaveForm type.
	 * 
	 * @param shape
	 *            StandardWaveForm type
	 * @throws Exception
	 */
	public void setShape(StandardWaveForm shape) throws Exception {
		this.shape = shape;

		switch (shape) {
		case DC:
		case SINE:
		case SQUARE:
		case TRIANGLE:
		case RAMP:
			setShapeInternal("SHAP", shape.getValue());
			return;
		default:
			setShapeInternal("SHAP", "USER");
			break;
		}

		// user defined.
		switch (shape) {
		case USERA:
		case USERB:
		case USERC:
		case USERD:
			setShapeInternal("USER", shape.getValue());
			break;
		default:
			logger.error("Unknown user shape id " + shape.key);
		}
	}

	/**
	 * Internal method to set shape.
	 * 
	 * @param waveFormClass
	 *            class, this is "SHAP" or "USER"
	 * @param waveFormType
	 * @throws Exception
	 */
	private void setShapeInternal(String waveFormClass, String waveFormType)
			throws Exception {
		setShapeInternal(waveFormClass, waveFormType, false);
	}

	private void setShapeInternal(String waveFormClass, String waveFormType,
			boolean inGroup) throws Exception {
		String grouper = inGroup ? ";" : "";
		String cmd = "SOUR:FUNC:" + waveFormClass + " " + waveFormType
				+ grouper;
		vxiConnector.send(deviceLink, cmd);
	}

	/**
	 * Set shape, builtin (non-standard) type.
	 * 
	 * @param waveForm
	 *            waveform of type BuiltinWaveForm.
	 * @param dest
	 *            user location A,B,C or D.
	 * @throws Exception
	 */
	public void setShape(BuiltinWaveForm waveForm, char dest) throws Exception {
		if (dest != 'A' && dest != 'B' && dest != 'C' && dest != 'D') {
			logger.error(
					"Invalid destination: " + dest + "(Allowed: A,B,C and D)");
			return;
		}
		setShapeInternal("SHAP", "USER");
		String source = waveForm.getValue();
		int length = 4096;
		String cmd = "SOUR:ARB:DOWN " + source + "," + dest + "," + length;
		vxiConnector.send(deviceLink, cmd);
		cmd = "SOUR:FUNC:USER " + dest;
		vxiConnector.send(deviceLink, cmd);
	}

	public void arm() throws Exception {
		if (!isArmed) {
			vxiConnector.send(deviceLink, "INIT:IMM");
			isArmed = true;
		}
	}

	public void deArm() throws Exception {
		if (isArmed) {
			vxiConnector.send(deviceLink, "ABOR");
			isArmed = false;
		}
	}

	public void setShape(Double[] waveForm, int length, char dest)
			throws Exception {
		setShapeInternal("SHAP", "USER", true);

		String cmd = "SOUR:LIST:SEGM:SEL " + dest;
		vxiConnector.send(deviceLink, cmd);

		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < length; i++) {
			sb.append(String.format(Locale.ENGLISH, "%.5f", waveForm[i]));
			if (i != length - 1) {
				sb.append(',');
			}
		}
		cmd = "SOUR:LIST:SEGM:VOLT " + sb.toString()/* .substring(0, 59) */;
		vxiConnector.send(deviceLink, cmd);

		setShapeInternal("USER", StandardWaveForm.USERA.getValue());

		cmd = "SOUR:LIST:SEGM:VOLT:POINTS?";
		vxiConnector.send(deviceLink, cmd);
	}

	/**
	 * Converts a double volts value to related DAC code. See page 109.
	 * 
	 * @param v
	 *            volt value to convert.
	 * @return DAC value in range 0..4096. Return value is a short value.
	 */
	public static short voltsToDACCode(double v) {
		short dac = (short) ((v / 0.0025) + 2048);
		if (dac < 0 || dac > 4095) {
			logger.error("dac value out of allowed range: " + dac);
		}
		return (short) (dac & 0x0fff);
	}

	/**
	 * Converts a double volts value to 16 bit integer.
	 * 
	 * @param v
	 *            volt value to convert.
	 * @return 16 bit integer
	 */
	public static short voltsToShort(double v) {
		short val = (short) ((v / 0.0025));
		if (val < -2047 || val > 2047) {
			logger.error("dac value out of allowed range: " + val);
		}
		return (short) val;
	}

	public void setUserDefinedWaveform(String valueString, boolean dacValues)
			throws Exception {
		String answer;

		String prefix;
		if (dacValues) {
			prefix = "SOUR:LIST:SEGM:VOLT:DAC ";
		} else {
			prefix = "SOUR:LIST:SEGM:VOLT ";
		}
		Timer timer = new Timer();

		prefix_userDefinedWF();

		timer.start();
		String values = prefix + valueString;
		values += '\n';
		vxiConnector.send(deviceLink, values);
		timer.stopAndPrintln();

		vxiConnector.send(deviceLink, "SOUR:FUNC:USER A");
		vxiConnector.send(deviceLink, "INIT:IMM");
		answer = vxiConnector.send_and_receive(deviceLink,
				"SOUR:LIST:SEGM:SEL?");
		System.out.println(answer);
		answer = vxiConnector.send_and_receive(deviceLink,
				"SOUR:LIST:SEGM:VOLT:POIN?");
		System.out.println(answer);
	}

	/**
	 * Set user defined waveform. Waveform is contained in a 4096 point double
	 * array. Array values must be between -maxValue and +maxValue. Otherwise
	 * the waveform will be ignored by HP1340.
	 * 
	 * @param waveform
	 *            4096 point double array with waveform data.
	 * @param maxValue
	 *            maximum allowed data value.
	 * @throws Exception
	 */
	public void setUserDefinedWaveform(double[] waveform, double maxValue)
			throws Exception {
		String values = "";
		NumberFormat formatter = new DecimalFormat("#0.00");

		prefix_userDefinedWF();
		for (int i = 0; i < waveform.length; i++) {
			// ensure that value does not overrun max/min allowed value
			// if overun occurs, the waveform data will not be loaded by HP1340
			if (waveform[i] < -maxValue) {
				waveform[i] = -maxValue;
			}
			if (waveform[i] > maxValue) {
				waveform[i] = maxValue;
			}
			values += formatter.format(waveform[i]).replace(",", ".");
			if (i != waveform.length - 1) {
				values += ",";
			}
		}
		postfix_UserDefinedWF("", values);
	}

	/**
	 * Set user defined waveform. Waveform is contained in a 4096 point short
	 * array as DAC values.
	 * 
	 * @param waveform
	 *            4096 point DAC array with waveform data.
	 * @param maxValue
	 *            maximum allowed data value.
	 * @throws Exception
	 */
	public void setUserDefinedWaveform(short[] waveform) throws Exception {
		String values = "";

		prefix_userDefinedWF();
		for (int i = 0; i < waveform.length; i++) {
			values += waveform[i];
			if (i != waveform.length - 1) {
				values += ",";
			}
		}
		postfix_UserDefinedWF(":DAC", values);
	}

	/**
	 * Set user defined waveform. Waveform is contained in a 4096 point short
	 * array as DAC values.
	 * 
	 * This method uses the compact 'Definite Length Arbitrary Block Data' data
	 * format to transfer the DAC values. Each DAC value is an IEEE488.2 16 Bit
	 * Integer. MSB first.
	 * 
	 * TODO: Integer values are allowed in range -2047 .. +2047 which is -0x7ff
	 * .. +0x7ff. Currently, all values having the highest bit in the lower byte
	 * set (1), cannot be transmitted. I am helping me by setting that bit to
	 * zero. The error may come from the byte-to-String conversion in Java,
	 * where data type 'byte' is defined as a signed integer with 7 bits, bit 8
	 * then is the sign bit.
	 * 
	 * This function does work only with prologix controller, because the Prologix requirement
	 * for sending 8 bit data is met (by escaping some chars: CR,LF,ESC,VT).
	 * 
	 * @param waveform
	 *            4096 point DAC array with waveform data.
	 * @param maxValue
	 *            maximum allowed data value.
	 * @throws Exception
	 */
	public void setUserDefinedWaveformBlk(short[] waveform) throws Exception {
		// Definite Length Arbitrary Block Data
		String values = "#" + "4" + 2 * waveform.length;
		byte b;
		byte t[] = new byte[1];
		boolean escape;

		prefix_userDefinedWF();
		
		for (int i = 0; i < waveform.length; i++) {
			short theValue = waveform[i];
			
			// MSB
			// max value possible as byte 1 is 0x0f
			// Some chars needed to be escaped (Prologix special)
			escape=false;
			b = (byte) ((theValue >> 8) & 0xf);
			if (b == Constants.CR) {	escape = true; } 
			if (!escape && (b == Constants.VT)) { escape = true; } 
			if (!escape && (b == Constants.LF)) { escape = true; } 
			if (escape) {
				t[0] = Constants.ESC; // add escape char
				values += new String(t);
			}
			t[0] = b;
			values += new String(t);

			// LSB
			// max value possible as byte 2 is 0x7f: bit 8 cannot be set
			// Maybe: byte=8 bit signed Int, resulting in a FAIL lateron?!?
			escape = false; 
			b = (byte) (theValue & 0xff);
			if ((b & 0x80) != 0) { b = (byte) (b & 0x7f); } // <-- does not work with escape!!!
			if (b == Constants.CR) { escape = true; } 
			if (!escape && (b == Constants.VT)) { escape = true; }  
			if (!escape && (b == Constants.LF)) { escape = true; }  
			if (!escape && (b == Constants.ESC)) { escape = true; } 
			if (escape) {
				t[0] = Constants.ESC; // add escape char
				values += new String(t);
			}
			t[0] = b;
			values += new String(t);
		}
		postfix_UserDefinedWF(":DAC", values);
	}

	/**
	 * Helper for setUserDefinedWaveform()
	 * 
	 * @throws Exception
	 */
	protected void prefix_userDefinedWF() throws Exception {
		vxiConnector.send(deviceLink, "SOUR:ROSC:SOUR INT;");
		vxiConnector.send(deviceLink, ":SOUR:FREQ:FIX 1E3;");
		vxiConnector.send(deviceLink, ":SOUR:FUNC:SHAP USER;");
		vxiConnector.send(deviceLink, ":SOUR:VOLT:LEV:IMM:AMPL 5.1V");

		vxiConnector.send(deviceLink, "SOUR:ARB:DAC:SOUR INT");
		vxiConnector.send(deviceLink, "SOUR:LIST:SEGM:SEL A");
	}

	/**
	 * Helper for setUserDefinedWaveform()
	 * 
	 * @throws Exception
	 */
	protected void postfix_UserDefinedWF(String prefix, String values)
			throws Exception {
		String answer;
		Timer timer = new Timer();
		timer.start();
		// System.out.print(values);
		int toTransfer = values.length();
		values = "SOUR:LIST:SEGM:VOLT" + prefix + " " + values + '\n';
		vxiConnector.send(deviceLink, values);
		timer.stopAndPrintln();
		checkErrors();

		vxiConnector.send(deviceLink, "SOUR:FUNC:USER A");
		vxiConnector.send(deviceLink, "INIT:IMM");

		// Check that transfer was complete
		answer = vxiConnector.send_and_receive(deviceLink,
				"SOUR:LIST:SEGM:VOLT:POIN?");
		logger.debug("Device answer: " + answer); // should be #values
		int transfered = Integer
				.parseInt(answer.replace("\\+", "").replace("\n", ""));
		if (toTransfer != transfered) {
			logger.error(
					"Transfered number of points differ. Transfered {} of {} points.",
					transfered, toTransfer);
		}
		// Check target segment - just for fun
		answer = vxiConnector.send_and_receive(deviceLink,
				"SOUR:LIST:SEGM:SEL?");
		System.out.println(answer); // should be 'A'
	}

	/**
	 * Set sweep parameters. See page 90/91 of user's manual.
	 * 
	 * @param startFrequency
	 *            start frequency for sweep.
	 * @param stopFrequency
	 *            stop frequency for sweep
	 * @param numPoints
	 *            number of points (different frequencies) in sweep
	 * @param time
	 *            sweep repetition time
	 * @param amplitude
	 *            maximum amplitude for sweep.
	 * @param waveform
	 *            waveform for sweep.
	 * @throws Exception
	 */
	public void setSweep(double startFrequency, double stopFrequency,
			int numPoints, int time, double amplitude,
			StandardWaveForm waveform) throws Exception {
		this.amplitude = amplitude;
		String shape = waveform.getValue();
		String cmd = "SOUR:FREQ:MODE SWE;";
		vxiConnector.send(deviceLink, cmd);
		cmd = ":SOUR:FREQ:STAR " + Double.toString(startFrequency) + ";";
		vxiConnector.send(deviceLink, cmd);
		cmd = ":SOUR:FREQ:STOP " + Double.toString(stopFrequency) + ";";
		vxiConnector.send(deviceLink, cmd);
		cmd = ":SOUR:SWE:POIN " + numPoints + ";";
		vxiConnector.send(deviceLink, cmd);
		cmd = ":SOUR:SWE:TIME " + time + ";";
		vxiConnector.send(deviceLink, cmd);
		cmd = ":SOUR:FUNC:SHAP " + shape + ";";
		vxiConnector.send(deviceLink, cmd);

		cmd = "SOUR:VOLT:LEV:IMM:AMPL " + Double.toString(amplitude) + "V";
		vxiConnector.send(deviceLink, cmd);

		// sleep 100ms - allow interrupts (inside E1340) to be serviced
		sleep(100);
	}

	public void setMarker(MarkerFeedType feed, PolarityType polarity)
			throws Exception {
		// See page 167
		// FeedType OUTP:ZERO, SEGM, SOUR:ROSC, SOUR:SWE
		// polarityType INV/NORM
		String cmd = "SOUR:MARK:FEED \"" + feed.getValue() + "\"";
		vxiConnector.send(deviceLink, cmd);
		cmd = "SOUR:MARK:POL " + polarity.getValue();
		vxiConnector.send(deviceLink, cmd);
	}
	
}
