package de.spurtikus.vxi.connectors.serial;

/**
 * GPIB over Serial Connector configuration. Adds Primary/Secondary GPIB address
 * for controller in charge.
 * 
 * @author dennis
 *
 */
public class GPIBSerialConnectorConfig extends SerialConnectorConfig {
	/** Controller primary address */
	int controllerPrimaryAddress = 9;
	/** Controller secondary address */
	int controllerSecondaryAddress = 0;

	public void setControllerPrimaryAddress(int controllerPrimaryAddress) {
		this.controllerPrimaryAddress = controllerPrimaryAddress;
	}

	public int getControllerPrimaryAddress() {
		return controllerPrimaryAddress;
	}

	public void setControllerSecondaryAddress(int controllerSecondaryAddress) {
		this.controllerSecondaryAddress = controllerSecondaryAddress;
	}

	public int getControllerSecondaryAddress() {
		return controllerSecondaryAddress;
	}

}
