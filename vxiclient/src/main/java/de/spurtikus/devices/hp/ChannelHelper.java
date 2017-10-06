package de.spurtikus.devices.hp;

import java.util.HashMap;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.spurtikus.vxi.util.ConversionUtil;

public class ChannelHelper {
	private static Logger logger = LoggerFactory.getLogger(ChannelHelper.class);

	/**
	 * Creates a channel list as understood by HP 1326B.
	 * 
	 * @param channels
	 *            List of channels
	 * @return String containing channels in HP1326B format.
	 */
	public static String toChannelString(List<Integer> channels) {
		if (channels.size() == 0) {
			logger.warn("No channels defined");
			return null;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("(@");
		for (int ch : channels) {
			sb.append(ch);
			sb.append(',');
		}
		// remove last comma
		sb.deleteCharAt(sb.length() - 1);
		sb.append(")");
		return sb.toString();
	}
	
	/**
	 * Convert a list of values inside a string into map of doubles where the
	 * key is the channel name.
	 * 
	 * @param channels
	 *            List of channels
	 * @param s
	 *            String containing all measurements.
	 * @return Map of doubles where the key is the channel name.
	 */
	public static HashMap<Integer, Double> toChannelMap(List<Integer> channels, String s) {
		HashMap<Integer, Double> m = new HashMap<Integer, Double>();
		// get rid of starting garbage
		s = s.replace("\n\r\n\r", "");
		// read only until first end of line
		String v[] = s.split("\n\r");
		String[] values = v[0].split(",");
		for (int i = 0; i < values.length; i++) {
			// add ( key = channel name at position i, value = value to double
			// at position i )
			if (values[i] != null && !values[i].isEmpty()) {
				Double value = ConversionUtil.stringToDouble(values[i]);
				if (value.equals(HP1326.OVERFLOW_VALUE)) {
					logger.warn("Overflow condition: " + value);
				}
				m.put(channels.get(i), value);
			}
		}
		return m;
	}

}
