package de.spurtikus.devices.hp;

import static org.junit.Assert.*;

import java.util.List;

import org.hamcrest.core.IsEqual;
import org.junit.Before;
import org.junit.Test;

import de.spurtikus.vxi.connectors.ConnectorConfig;
import de.spurtikus.vxi.connectors.DeviceLink;
import de.spurtikus.vxi.connectors.VXIConnector;
import de.spurtikus.vxi.connectors.VXIConnectorFactory;
import de.spurtikus.vxi.connectors.serial.GPIBSerialConnectorConfig;
import de.spurtikus.vxi.mainframes.hp1300b.VXIDevice;
import de.spurtikus.vxi.service.Configuration;

public class HP1300bTest {

	public static final int SERIAL_CONFIG = 1;
	public static final int RPC_CONFIG = 2;
	
	static final String TEST_DEVICE_NAME = "hp1301";

	Configuration configuration ;
	ConnectorConfig config;
	VXIConnector vxiConnector = null;
	DeviceLink theLid = null;

	@Before
	public void beforeTest() throws Exception {
		// Get configuration
		configuration = Configuration.getInstance();
		// We assume usable config at some index
		config = Configuration.findConfigById(SERIAL_CONFIG);
		// We like to test a net GPIBSerial
		assertThat(config.getClass(),IsEqual.equalTo(GPIBSerialConnectorConfig.class));
		System.out.println(config);

		vxiConnector = VXIConnectorFactory.getConnector(config);

		String deviceid = config.getDeviceIdByName(TEST_DEVICE_NAME);
		assertNotNull(deviceid);
		theLid = vxiConnector.initialize(config, deviceid);
	}

	@Test
	public void testListDevices() throws Exception {
		System.out.println("Start...");

		HP1300b mainframe = new HP1300b(vxiConnector, theLid);
		List<VXIDevice> devs = mainframe.listDevices(false);

		for (VXIDevice dev : devs) {
			if (dev.getAddress() % 8 != 0 && dev.getAddress() != 1) {
				// sub-devices of other devices
				System.out.print("  |-");
			} else {
				System.out.print("|-");
			}
			System.out.print(dev.toString());
			System.out.println();
		}
		System.out.println("...done");
	}
	
	@Test
	public void test_ListDevice() throws Exception {
		System.out.println("Start...");

		String deviceid = config.getDeviceIdByName("hp1333");
		assertNotNull(deviceid);
		theLid = vxiConnector.initialize(config, deviceid);
		String cmd = "*IDN?";
		System.out.println("Command: " + cmd);
		vxiConnector.send(theLid, cmd);

		// Receive answer
		String answer = vxiConnector.receive(theLid);
		System.out.println("Answer: " + answer);

		System.out.println("...done");
	}

	@Test
	public void test_ListDevices() throws Exception {
		System.out.println("Start...");

		for (String d: config.getDevices().keySet()) {
			String deviceid = config.getDeviceIdByName(d);
			assertNotNull(deviceid);
			if (deviceid.equals("9,10")) {
				// this is a device with no driver installed, this would fail the test
				System.err.println("Ignoring device: "+deviceid);
				continue;
			}
			theLid = vxiConnector.initialize(config, deviceid);
			String cmd = "*IDN?";
			System.out.println("Command: " + cmd);
			vxiConnector.send(theLid, cmd);
	
			// Receive answer
			String answer = vxiConnector.receive(theLid);
			System.out.println("Answer: " + answer);
		}
	
		System.out.println("...done");
	}

}
