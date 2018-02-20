package de.spurtikus.devices.hp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.spurtikus.vxi.connectors.ConnectorConfig;
import de.spurtikus.vxi.connectors.DeviceLink;
import de.spurtikus.vxi.connectors.VXIConnector;
import de.spurtikus.vxi.connectors.VXIConnectorFactory;

public class ErrorTest extends DeviceBaseTest {
	private HP1300b testee = null;

	VXIConnector vxiConnector = null;
	DeviceLink theLid = null;

	@Before
	public void beforeTest() throws Exception {
		final String test_Serial_or_RPC = "Serial"; // "RPC" or "Serial"
		final String TEST_DEVICE_NAME = "hp1301";

		// Load configuration
		ConnectorConfig config = loadConfig(test_Serial_or_RPC);
		System.out.println(config);

		vxiConnector = VXIConnectorFactory.getConnector(config);

		String deviceid = config.getDeviceIdByName(TEST_DEVICE_NAME);
		assertNotNull(deviceid);
		theLid = vxiConnector.initialize(config, deviceid);
		
		testee = new HP1300b(vxiConnector, theLid);
	}

	@Test
	public void test_Errors_UnknownCommand() throws Exception {
		System.out.println("Start...");

		String cmd = "*XXX";
		vxiConnector.send(theLid, cmd);

		List<String> errors = testee.checkErrors();
		errors.forEach(e -> System.out.println(e));
		assertEquals(errors.size(), 2);
		
		System.out.println("...done");
	}

}
