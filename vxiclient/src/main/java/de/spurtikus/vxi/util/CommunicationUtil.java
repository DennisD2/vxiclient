package de.spurtikus.vxi.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.spurtikus.vxi.connectors.Constants;

public class CommunicationUtil {
	static Logger logger = LoggerFactory.getLogger(CommunicationUtil.class);

	private static final int MAX_PER_LINE = 24;
	private static final int MAX_PER_GROUP = 8;

	public static void dumpString(String s) {
		StringBuilder sbascii = new StringBuilder();
		StringBuilder sbhex = new StringBuilder();

		for (int i = 0; i < s.length(); i++) {
			char c = (char) s.charAt(i);

			if (c > 32 && c < 127) {
				sbascii.append(c);
			} else {
				sbascii.append('.');
			}
			sbhex.append(charToHexString(c));

			if ((i + 1) % MAX_PER_GROUP == 0) {
				sbhex.append("   ");
				sbascii.append(' ');
			} else {
				sbhex.append(' ');
			}

			if ((i + 1) % MAX_PER_LINE == 0) {
				logger.debug(sbhex.toString() + " | "
						+ sbascii.toString());
				sbhex = new StringBuilder();
				sbascii = new StringBuilder();
			}
		}
		logger.debug(sbhex.toString() + " | " + sbascii.toString());
	}

	public static void dumpByteArray(byte data[], int length) {
		StringBuilder sbascii = new StringBuilder();
		StringBuilder sbhex = new StringBuilder();

		for (int i = 0; i < length; i++) {
			char c = (char) data[i];

			if (c > 32 && c < 127) {
				sbascii.append(c);
			} else {
				sbascii.append('.');
			}
			sbhex.append(charToHexString(c));

			if ((i + 1) % MAX_PER_GROUP == 0) {
				sbhex.append("   ");
				sbascii.append(' ');
			} else {
				sbhex.append(' ');
			}

			if ((i + 1) % MAX_PER_LINE == 0) {
				logger.debug(sbhex.toString() + " | "
						+ sbascii.toString());
				sbhex = new StringBuilder();
				sbascii = new StringBuilder();
			}
		}
		logger.debug(sbhex.toString() + " | " + sbascii.toString());
	}

	//public static final char LF = 0xa;
	//public static final char CR = 0xd;
	//public static final char BS = 0x8;
	//public static final char ESC = 0x1b;

	public static StringBuilder charToHexString(char c) {
		switch (c) {
		case Constants.LF:
			return new StringBuilder("LF");
		case Constants.CR:
			return new StringBuilder("CR");
		case Constants.BS:
			return new StringBuilder("BS");
		case Constants.ESC:
			return new StringBuilder("ES");

		}
		return byteToHexString((byte) c);
	}
	
	public static StringBuilder byteToHexString(byte c) {

		char loNibble = nibbleToHexNibble(c & 0x0f);
		char hiNibble = nibbleToHexNibble((c >> 4) & 0x0f);
		return new StringBuilder().append(hiNibble).append(loNibble);
	}


	private static char nibbleToHexNibble(int c) {
		if (c < 10) {
			return (char) (c + 0x30);
		}
		char r;
		switch (c) {
		case 10:
			r = 'a';
			break;
		case 11:
			r = 'b';
			break;
		case 12:
			r = 'c';
			break;
		case 13:
			r = 'd';
			break;
		case 14:
			r = 'e';
			break;
		case 15:
			r = 'f';
			break;
		default:
			r = '?';
		}
		return r;
	}
	
	/**
	 * Converts chars in a byte array to a String.
	 * 
	 * Works only well if all bytes in the source are allowed in a String.
	 * 
	 * @param source
	 *            byte array to use
	 * @param n
	 *            number of bytes to convert
	 * @return String containing the bytes from byte array as chars.
	 */
public static String asString(byte[] source, int n) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < n; i++) {
			sb.append((char) source[i]);
		}
		return sb.toString();
}

	public static void copyByteArray(byte[] from, byte[] to) {
		for (int i = 0; i < from.length; i++) {
			to[i] = from[i];
		}
	}
	
	public static void copyByteArray(byte[] from, byte[] to, int size) {
		for (int i = 0; i < size; i++) {
			to[i] = from[i];
		}
	}
	
	public static byte[] asByteArray(String s) {
		byte b[] = new byte[s.length()];
		for (int i = 0; i < s.length(); i++) {
			b[i] = (byte) s.charAt(i);
		}
		return b;
	}

	public static String asStringNullAware(byte[] b) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < b.length && b[i] != 0x00; i++) {
			// logger.debug((char)b[i]);
			sb.append((char) b[i]);
		}
		return sb.toString();
	}

	public static String asStringNullAware(byte[] b, int len) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < len && i < b.length && b[i] != 0x00; i++) {
			// logger.debug((char)b[i]);
			sb.append((char) b[i]);
		}
		return sb.toString();
	}

}
