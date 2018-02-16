package de.spurtikus.vxi;

/**
 * Global constants.
 * 
 * @author dennis
 *
 */
public class Constants {
	/** Config file relative path in classpath */
	public static final String CONFIGFILE_LOCATION = "vxiserver.properties";
	/** Prefix for all vxiclient properties */
	public static final String CONFIGVAL_PREFIX = "vxi.connector.";

	/** Serial configuration */
	public static final int SERIAL_CONFIG = 1;
	/** Network configuration */
	public static final int RPC_CONFIG = 2;

	public static final char BS = 0x8;
	public static final char LF = 0xa;
	public static final char CR = 0xd;
	public static final byte VT = 0x0b;
	public static final char XON = 0x11;
	public static final char XOFF = 0x13;
	public static final byte ESC = 0x1b;
	
	//public static final String CONTEXT_ROOT = "/vxi";
	public static final String SERVICE_ROOT = "api/rest/";
	
	public static final String URL_SYSTEM = "system";
	public static final String URL_MAINFRAME = "mainframe";
	public static final String URL_PACER = "pacer";
	public static final String URL_COUNTER = "counter";
	public static final String URL_SWITCH = "switch";
	public static final String URL_AFG = "afg";
	public static final String URL_DIGITALIO = "dio";
	public static final String URL_MULTIMETER = "multimeter";

}
