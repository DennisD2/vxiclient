package de.spurtikus.vxi.client;

import org.junit.Test;

import de.spurtikus.vxi.connectors.DeviceLink;
import de.spurtikus.vxi.connectors.VXIConnector;
import de.spurtikus.vxi.connectors.VXIConnectorFactory;
import de.spurtikus.vxi.connectors.rpc.RPCConnectorConfig;

public class BaseTest {

	String host = "192.168.178.78";

	static final int CLIENT_ID = 12345;

	static final String TEST_DEVICE_ID = "iscpi,8";

	@Test
	public void VXIConnectorTest() throws Exception {
		System.out.println("Start...");

		VXIConnector vxiConnector = VXIConnectorFactory.getConnector();

		RPCConnectorConfig config = new RPCConnectorConfig(host, CLIENT_ID, TEST_DEVICE_ID);
		DeviceLink theLid = vxiConnector.initialize(config);

		// Send command
		// vclient.send(client, theLid, "MEAS:VOLT:AC? 1, 0.001");
		vxiConnector.send(theLid, "*IDN?");

		// Receive answer
		String answer = vxiConnector.receive(theLid);
		System.out.println("Answer: " + answer);

		System.out.println("...done");
	}

}
