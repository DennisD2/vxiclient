package de.spurtikus.vxi.connectors;

import static org.junit.Assert.assertNotNull;

import java.util.stream.Stream;

import org.junit.Before;
import org.junit.Test;

import de.spurtikus.vxi.connectors.serial.GPIBSerialConnectorConfig;
import de.spurtikus.vxi.service.Configuration;

public class GPIBSerialConnectorBaseTest {

	static final String TEST_DEVICE_NAME = "hp1301";

	GPIBSerialConnectorConfig config  = null;
	VXIConnector vxiConnector = null;
	String deviceId = null;
	
	@Before
	public void before() throws Exception {
		// Get connection config for Adapter with Id==1 (serial connector)
		Configuration.load();
		Stream<ConnectorConfig> ccc = Configuration.getEnabledConfigs().stream().filter(c->c.getId()==1);
		config  = (GPIBSerialConnectorConfig) ccc.findAny().get();

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
	public void serialConnector_SimpleTest() throws Exception {
		System.out.println("Start...");
		
		// Get deviceId for the device to use in test
		deviceId = Configuration.getDeviceIdByName(config.getId(), TEST_DEVICE_NAME);
		
		DeviceLink theLid = vxiConnector.initialize(config, deviceId);
		String cmd = "*IDN?";
		System.out.println("Command: " + cmd);
		vxiConnector.send(theLid, cmd);

		// Receive answer
		String answer = vxiConnector.receive(theLid);
		System.out.println("Answer: " + answer);

		System.out.println("...done");
	}
	

}
