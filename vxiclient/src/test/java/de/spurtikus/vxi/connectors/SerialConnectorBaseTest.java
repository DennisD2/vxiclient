package de.spurtikus.vxi.connectors;

import org.junit.Test;

import de.spurtikus.vxi.connectors.serial.SerialConnectorConfig;

public class SerialConnectorBaseTest {

	//static final String TEST_DEVICE_ID = "iscpi,37";
	//static final String TEST_DEVICE_ID = "iscpi,8";

	@Test
	public void serialConnectorBaseTest() throws Exception {
		System.out.println("Start...");

		SerialConnectorConfig config = new SerialConnectorConfig();

		VXIConnector vxiConnector = VXIConnectorFactory.getConnector(config);


		DeviceLink theLid = vxiConnector.initialize(config);
		// Send command
		// String cmd = "MEAS:VOLT:AC? 1, 0.001";
		String cmd = "*IDN?";
		System.out.println("Command: " + cmd);
		vxiConnector.send(theLid, cmd);

		// Receive answer
		String answer = vxiConnector.receive(theLid);
		System.out.println("Answer: " + answer);

		System.out.println("...done");
	}

}
