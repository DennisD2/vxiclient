package de.spurtikus.vxi.connectors.rpc;

import java.util.HashMap;
import java.util.Map;

public class RPCErrors {
	private static Map<Integer, String> errors = new HashMap<>();

	static void initErrorMap() {
		/*
		 * Read failed for reason specified in error code. (From published
		 * VXI-11 protocol, section B.5.2)
		 */
		errors.put(0, "No error");
		errors.put(1, "Syntax error");
		errors.put(3, "Device not accessible");
		errors.put(4, "Invalid link identifier");
		errors.put(5, "Parameter error");
		errors.put(6, "Channel not established");
		errors.put(8, "Operation not supported");
		errors.put(9, "Out of resources");
		errors.put(11, "Device locked by another link");
		errors.put(12, "No lock held by this link");
		errors.put(15, "I/O timeout");
		errors.put(17, "I/O error");
		errors.put(21, "Invalid address");
		errors.put(2, "Abort");
		errors.put(29, "Channel already established");
	}
	
	public static String toErrorString(int error) {
		String errorMessage = "?";
		if (error >= 0 && error < errors.size()) {
			errorMessage = errors.get(error);
		}
		return errorMessage;
	}

}
