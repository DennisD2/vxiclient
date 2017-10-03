package de.spurtikus.vxi.utilConversionUtil;

import java.io.UnsupportedEncodingException;

public class ConversionUtil {

	/**
	 * Converts byte array to a string.
	 * 
	 * @param data
	 *            input byte array
	 * @return string
	 * @throws UnsupportedEncodingException
	 */
	public static String toString(byte[] data) throws UnsupportedEncodingException {
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
}
