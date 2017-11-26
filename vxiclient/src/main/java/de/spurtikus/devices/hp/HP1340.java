package de.spurtikus.devices.hp;

import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
	static Logger logger = LoggerFactory.getLogger(HP1333.class);

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

	public void Arm() throws Exception {
		if (!isArmed) {
			vxiConnector.send(deviceLink, "INIT:IMM");
			isArmed = true;
		}
	}

	public void DeArm() throws Exception {
		if (isArmed) {
			vxiConnector.send(deviceLink, "ABOR");
			isArmed = false;
		}
	}

	public static byte[] GetDemoWaveForm() {
		int size = 4096;
		int points[] = new int[size];
		for (int i = 0; i < size; i++) {
			points[i] = i;
		}

		byte result[] = new byte[size * 2];
		int j = 0;
		for (int p : points) {
			if (p >= size)
				throw new IllegalArgumentException("Out of range");

			byte msb = (byte) ((p & 0x0F00) >> 8);
			byte lsb = (byte) (p & 0xFF);

			result[j++] = msb; // msbfirst
			result[j++] = lsb;
		}

		return result;
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
	 * @return DAC value in range 0..4096.
	 */
	public int voltsToDACCode(double v) {
		int dac = (int) ((v / 0.0025) + 2048);
		return dac & 0x0fff;
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

	/**
	 * TODO: method below untested old code by Samofabs
	 * 
	 * @return
	 * @throws Exception
	 */
	public String UploadWaveForm() throws Exception {
		// OLD COMMENT: this should eventually work with GPIB
		// Samofabs mailtext:
		// "For example I'm pretty sure that you can't upload arbitrary waveform
		// trough serial port.. you need gpib. There are parts of the code that
		// you might try to reuse if you connect it via GPIB."

		vxiConnector.send(deviceLink, "SOUR:LIST:SEGM:SEL A");

		byte[] demo = GetDemoWaveForm();
		uploadBinaryData("SOUR:LIST:SEGM:VOLT:DAC ", demo);
		return "";
	}

	/*
	 * =========================================================================
	 * = =========== code copied frm samofab NEEDS REWORK
	 */
	public void uploadBinaryData(String command, byte[] data) throws Exception {
		byte[] processedData = GetUploadReadyBytes(data);
		int size = processedData.length;
		int sizeDigits = Integer.toString(size).length();
		String definiteBlockHeader = "#" + Integer.toString(sizeDigits)
				+ Integer.toString(size);

		vxiConnector.send(deviceLink, command + definiteBlockHeader);
		// XXX port.writeWithAnswer(Byte.toString(processedData));
		vxiConnector.send(deviceLink, "\r"); // if using indefinite block, sent
												// !\r
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
