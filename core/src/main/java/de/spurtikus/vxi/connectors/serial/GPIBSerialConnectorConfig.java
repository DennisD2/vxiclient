package de.spurtikus.vxi.connectors.serial;

/**
 * GPIB over Serial Connector configuration. Adds Primary/Secondary GPIB address
 * for controller in charge.
 * 
 * @author dennis
 *
 */
public class GPIBSerialConnectorConfig extends SerialConnectorConfig {
	int primary=9;
	int secondary=0;

	public int getPrimaryAddress(String deviceId) {
		return primary;
	}

	public int getSecondaryAddress(String deviceId) {
		return secondary;
	}

}
