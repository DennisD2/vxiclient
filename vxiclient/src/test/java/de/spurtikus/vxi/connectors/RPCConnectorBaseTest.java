package de.spurtikus.vxi.connectors;

import org.junit.Test;

import de.spurtikus.vxi.connectors.DeviceLink;
import de.spurtikus.vxi.connectors.VXIConnector;
import de.spurtikus.vxi.connectors.VXIConnectorFactory;
import de.spurtikus.vxi.connectors.rpc.RPCConnectorConfig;

public class RPCConnectorBaseTest {

	// String host = "192.168.178.78";
	String host = "vxi1";

	static final int CLIENT_ID = 12345;

	static final String TEST_DEVICE_ID = "iscpi,37";
	// static final String TEST_DEVICE_ID = "iscpi,8";

	@Test
	public void vxiConnectorTest() throws Exception {
		System.out.println("Start...");

		RPCConnectorConfig config = new RPCConnectorConfig(host, CLIENT_ID,
				TEST_DEVICE_ID);

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
