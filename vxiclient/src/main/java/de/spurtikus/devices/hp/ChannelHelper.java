package de.spurtikus.devices.hp;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

public class ChannelHelper {
	private static Logger log = Logger.getLogger("ChannelHelper");

	/**
	 * Creates a channel list as understood by HP 1326B.
	 * 
	 * @param channels
	 *            List of channels
	 * @return String containing channels in HP1326B format.
	 */
	public static String toChannelString(List<Integer> channels) {
		if (channels.size() == 0) {
			log.severe("No channels defined");
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
				Double value = stringToDouble(values[i]);
				if (value.equals(HP1326.OVERFLOW_VALUE)) {
					log.severe("Overflow condition: " + value);
				}
				m.put(channels.get(i), value);
			}
		}
		return m;
	}

	/**
	 * Convert a String as returned by HP1326B into a double.
	 * 
	 * @param s
	 *            String to convert
	 * @return Double value
	 */
	public static Double stringToDouble(String s) {
		Double d = 0.0;
		try {
			d = Double.parseDouble(s);
		} catch (NumberFormatException e) {

		}
		return d;
	}

}
