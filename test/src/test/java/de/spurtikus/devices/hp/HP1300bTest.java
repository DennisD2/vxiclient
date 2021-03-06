package de.spurtikus.devices.hp;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.spurtikus.vxi.connectors.ConnectorConfig;
import de.spurtikus.vxi.connectors.DeviceLink;
import de.spurtikus.vxi.connectors.VXIConnector;
import de.spurtikus.vxi.connectors.VXIConnectorFactory;
import de.spurtikus.vxi.mainframes.hp1300b.VXIDevice;

/**
 * Mainframe tests for HP1300 type mainframes.
 * 
 * @author dennis
 *
 */
public class HP1300bTest extends DeviceBaseTest {
	HP1300b mainframe = null;

	ConnectorConfig config;
	VXIConnector vxiConnector;
	DeviceLink theLid;

	@Before
	public void beforeTest() throws Exception {
		final String test_Serial_or_RPC = "Serial"; // "RPC" or "Serial"
		final String TEST_DEVICE_NAME = "hp1301";

		// Load configuration
		config = loadConfig(test_Serial_or_RPC);
		System.out.println(config);

		vxiConnector = VXIConnectorFactory.getConnector(config);

		String deviceid = config.getDeviceIdByName(TEST_DEVICE_NAME);
		assertNotNull(deviceid);
		theLid = vxiConnector.initialize(config, deviceid);

		mainframe = new HP1300b(vxiConnector, theLid);
	}

	@Test
	public void testListDevices() throws Exception {
		System.out.println("Start...");

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

		/*
		 * for (DeviceInfo d: config.getDevices()) { String deviceid = d.
		 * assertNotNull(deviceid); if (deviceid.equals("9,10")) { // this is a
		 * device with no driver installed, this would fail the test
		 * System.err.println("Ignoring device: "+deviceid); continue; } theLid
		 * = vxiConnector.initialize(config, deviceid); String cmd = "*IDN?";
		 * System.out.println("Command: " + cmd); vxiConnector.send(theLid,
		 * cmd);
		 * 
		 * // Receive answer String answer = vxiConnector.receive(theLid);
		 * System.out.println("Answer: " + answer); }
		 */

		System.out.println("...done");
	}

}
