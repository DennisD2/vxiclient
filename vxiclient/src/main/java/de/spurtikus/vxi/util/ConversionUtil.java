package de.spurtikus.vxi.util;

import java.io.UnsupportedEncodingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConversionUtil {
	static Logger logger = LoggerFactory.getLogger(ConversionUtil.class);

	// pure static
	private ConversionUtil() {
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
			logger.error("Error converting string '{}' to double value", s);
		}
		return d;
	}

	/**
	 * Converts byte array to a string.
	 * 
	 * @param data
	 *            input byte array
	 * @return string
	 * @throws UnsupportedEncodingException
	 */
	public static String toString(byte[] data)
			throws UnsupportedEncodingException {
		return new String(data, "UTF-8");
	}

	/**
	 * Converts a string to a byte array.
	 * 
	 * @param string
	 *            input string.
	 * @return byte array.
	 */
	public static byte[] toBytes(String string) {
		return string.getBytes();
	}

	/**
	 * @deprecated
	 * @param s
	 * @return
	 */
	public byte[] asByteArray(String s) {
		byte[] data = new byte[s.length()];
		for (int i = 0; i < s.length(); i++) {
			data[i] = (byte) s.charAt(i);
		}
		return data;
	}

	/**
	 * @deprecated
	 * @param s
	 * @return
	 */
	public String byteArrayAsString(byte[] b) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < b.length && b[i] != 0x00; i++) {
			logger.trace("" + (char) b[i]);
			sb.append((char) b[i]);
		}
		return sb.toString();
	}

	/**
	 * @deprecated
	 * @param s
	 * @return
	 */
	public String byteArrayAsString(byte[] b, int len) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < len && i < b.length && b[i] != 0x00; i++) {
			logger.trace("" + (char) b[i]);
			sb.append((char) b[i]);
		}
		return sb.toString();
	}

}
