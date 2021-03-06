package de.spurtikus.vxi.connectors;

import static org.junit.Assert.assertNotNull;

import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;

import de.spurtikus.vxi.Constants;
import de.spurtikus.vxi.connectors.rpc.RPCConnectorConfig;
import de.spurtikus.vxi.service.Configuration;

public class RPCConnectorBaseTest {

	//static final String TEST_DEVICE_NAME = "hp1330";
	//static final String TEST_DEVICE_NAME = "hp1333";
	static final String TEST_DEVICE_NAME = "hp1411";

	RPCConnectorConfig config  = null;
	VXIConnector vxiConnector = null;
	String deviceId = null;

	@Before
	public void before() throws Exception {
		// Get connection config for Adapter with Id==1 (serial connector)
		Configuration.load();
		Stream<ConnectorConfig> ccc = Configuration.getEnabledConfigs().stream().filter(c->c.getId()==Constants.RPC_CONFIG);
		config  = (RPCConnectorConfig) ccc.findAny().get();
		assertNotNull(config);

		// Get connector for that configuration
		vxiConnector = VXIConnectorFactory.getConnector(config);
	}
	
	@Test
	public void serialConnector_getDeviceIdByName() throws Exception {
		System.out.println("Start...");

		String deviceId = config.getDeviceIdByName(TEST_DEVICE_NAME);
		assertNotNull(deviceId);
		System.out.println(TEST_DEVICE_NAME + " --> " + deviceId);
		System.out.println("...done");
	}
	
	@Test
	public void vxiConnector_SimpleTest() throws Exception {
		System.out.println("Start...");
		
		// Get deviceId for the device to use in test
		deviceId = Configuration.getDeviceIdByName(config.getId(), TEST_DEVICE_NAME);

		DeviceLink theLid = vxiConnector.initialize(config, deviceId);
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
