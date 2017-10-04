package de.spurtikus.devices.hp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.spurtikus.vxi.connectors.DeviceLink;
import de.spurtikus.vxi.connectors.VXIConnector;

/**
 * HP/Agilent/Keysight E1326B Digital Multimeter (DMM) control.
 * 
 * Currently implemented measurements / functions:
 * 
 * * single value voltage
 * 
 * * multi channel voltage
 * 
 * Additional functions
 * 
 * * Recording
 * 
 * @author dennis
 *
 */
public class HP1326 extends BaseHPDevice {
	static Logger logger = LoggerFactory.getLogger("RPCConnector");

	public static final double OVERFLOW_VALUE = 9.9E37; // See manual page 67
	/**
	 * HP1326 allowed voltage ranges. Besides real numbers, allowed are also:
	 * AUTO, DEF, MIN, MAX. DEF==AUTO.
	 */
	public final Double[] voltageRangesDC = { 0.113, 0.91, 7.27, 58.1, 300.0 };
	public final Double[] voltageRangesAC = { 0.0795, 0.63, 5.09, 40.7, 300.0 };

	// Double[] apertureRanges = { ...
	/*
	 * 10 ms* 0.0005 100 ms 0.005 2.5 ms 0.125 16.7 ms 1 20 ms 1 267 ms 16 320
	 * ms 16
	 */

	/**
	 * The mainframe object containing this device
	 */
	private VXIConnector mainFrame;

	private DeviceLink deviceLink;

	/**
	 * CTR. Requires mainframe object as parameter.
	 * 
	 * @param parent
	 *            Mainframe containing this device.
	 */
	public HP1326(VXIConnector parent, DeviceLink link) {
		mainFrame = parent;
		deviceLink = link;
	}

	/**
	 * Initializes device. Sets up some useful start configuration for DMM.
	 * 
	 * * Resets device
	 * 
	 * * Interrupts current measurement
	 * 
	 * * sets calibration frequency 50 Hz
	 * 
	 * @throws Exception
	 */
	public void initialize() throws Exception {
		mainFrame.send(deviceLink, "*RST");
		mainFrame.send(deviceLink, "ABOR");
		mainFrame.send(deviceLink, "CAL:LFR 50");
	}

	/**
	 * Initializes device for voltage measurement.
	 * 
	 * @throws Exception
	 */
	public void initializeVoltageMeasurement(Double voltageRange,
			List<Integer> channels) throws Exception {
		/*
		 * 10 ms* 0.0005 100 ms 0.005 2.5 ms 0.125 16.7 ms 1 20 ms 1 267 ms 16
		 * 320 ms 16
		 */
		mainFrame.send(deviceLink, "VOLTage:APERture MAX");
		setVoltageRange(voltageRange, channels);
		mainFrame.send(deviceLink, "CAL:ZERO:AUTO ON");
		mainFrame.send(deviceLink, "VOLTage:RESolution MAX");
		mainFrame.send(deviceLink, "INIT");
	}

	/**
	 * Create valid range parameter from an input value. For now, either there
	 * is a correct mapping or we use just the auto range.
	 * 
	 * @param voltageRange
	 *            Input value, desired range (maximum value)
	 * @return Valid range string
	 */
	protected String createVoltageRangeString(Double voltageRange) {
		boolean voltageRangeFound = false;
		String voltageRangeString;
		for (Double r : voltageRangesDC) {
			if (r.equals(voltageRange)) {
				voltageRangeFound = true;
				break;
			}
		}
		if (voltageRangeFound) {
			voltageRangeString = String.valueOf(voltageRange);
		} else {
			voltageRangeString = "AUTO";
		}
		return voltageRangeString;
	}

	public void setVoltageRange(Double voltageRange, List<Integer> channels)
			throws Exception {
		if (channels == null) {
			logger.error("Channels is null. Not yet implemented.");
			return;
		}
		String voltageRangeString = createVoltageRangeString(voltageRange);
		String channelString = ChannelHelper.toChannelString(channels);
		logger.trace("Selected voltage range: " + voltageRangeString
				+ " to channels " + channelString);

		// TODO: do we need both lines or only second?
		mainFrame.send(deviceLink, "CONF:VOLT:DC " + channelString);
		mainFrame.send(deviceLink, "CONF:VOLT:DC:RANGE " + voltageRangeString
				+ "," + channelString);
	}

	/**
	 * Do a single measurement and return result as Double.
	 * 
	 * @return Measurement value.
	 * @throws Exception
	 */
	public Double measureSingle() throws Exception {
		String s = mainFrame.send_and_receive(deviceLink, "MEAS:VOLT:DC?");
		s = s.replace("V", "");
		logger.trace("DEV: " + s);
		return ChannelHelper.stringToDouble(s);
	}

	/**
	 * Do a measurement on a list of channels (using a MUX card) and return
	 * results as a map.
	 * 
	 * @param channels
	 *            List of channels. Example "100", "101".
	 * 
	 * @return Map with measurements per channel. Map contains elements where
	 *         the key is the channel name and value is the measured value.
	 * @throws Exception
	 */
	public Map<Integer, Double> measureChannels(List<Integer> channels)
			throws Exception {
		String channelDefinition = ChannelHelper.toChannelString(channels);
		String s = mainFrame.send_and_receive(deviceLink,
				"MEAS:VOLT:DC? " + channelDefinition);
		s = s.replace("V", "");
		logger.trace("DEV: " + s);
		HashMap<Integer, Double> m = ChannelHelper.toChannelMap(channels, s);
		return m;
	}

}
