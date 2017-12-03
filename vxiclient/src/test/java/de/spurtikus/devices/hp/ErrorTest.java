package de.spurtikus.devices.hp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.util.List;

import org.hamcrest.core.IsEqual;
import org.junit.Before;
import org.junit.Test;

import de.spurtikus.vxi.Constants;
import de.spurtikus.vxi.connectors.ConnectorConfig;
import de.spurtikus.vxi.connectors.DeviceLink;
import de.spurtikus.vxi.connectors.VXIConnector;
import de.spurtikus.vxi.connectors.VXIConnectorFactory;
import de.spurtikus.vxi.connectors.serial.GPIBSerialConnectorConfig;
import de.spurtikus.vxi.service.Configuration;

public class ErrorTest {
	static final String TEST_DEVICE_NAME = "hp1301";

	ConnectorConfig config;
	VXIConnector vxiConnector = null;
	DeviceLink theLid = null;
	private HP1300b testee;

	@Before
	public void beforeTest() throws Exception {
		// Load configuration
		Configuration.load();
		// We assume usable config at some index
		config = Configuration.findConfigById(Constants.SERIAL_CONFIG);
		assertNotNull(config);
		// We like to test a net GPIBSerial
		assertThat(config.getClass(),IsEqual.equalTo(GPIBSerialConnectorConfig.class));
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
