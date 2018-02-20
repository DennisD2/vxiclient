package de.spurtikus.devices.hp;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import de.spurtikus.devices.hp.DigitalIO.PortDescription;
import de.spurtikus.vxi.connectors.ConnectorConfig;
import de.spurtikus.vxi.connectors.DeviceLink;
import de.spurtikus.vxi.connectors.VXIConnector;
import de.spurtikus.vxi.connectors.VXIConnectorFactory;

/**
 * Digital IO tests.
 * 
 * @author dennis
 *
 */
public class HP1330Test extends DeviceBaseTest {
	DigitalIO testee = null;

	@Before
	public void before() throws Exception {
		final String test_Serial_or_RPC = "Serial"; // "RPC" or "Serial"
		final String TEST_DEVICE_NAME = "hp1330";

		// Load configuration
		ConnectorConfig config = loadConfig(test_Serial_or_RPC);
		System.out.println(config);

		VXIConnector vxiConnector = VXIConnectorFactory.getConnector(config);

		String deviceid = config.getDeviceIdByName(TEST_DEVICE_NAME);
		assertNotNull(deviceid);
		DeviceLink theLid = vxiConnector.initialize(config, deviceid);
		testee = new DigitalIO(vxiConnector, theLid);
	}

	// @Ignore
	@Test
	public void testGetAndSet() throws Exception {
		testee.initialize();

		PortDescription get = new PortDescription(DigitalIO.Port.DATA0,
				DigitalIO.Bit.BIT0);
		boolean b = testee.getBit(get);
		System.out.println("Bit value: " + b);

		PortDescription set = new PortDescription(DigitalIO.Port.DATA0,
				DigitalIO.Bit.BIT1);
		testee.setBit(set, true);
		b = testee.getBit(set);
		System.out.println("Bit value: " + b);

		testee.sleep(10);

		testee.setBit(set, false);
		b = testee.getBit(set);
		System.out.println("Bit value: " + b);
	}

	@Ignore
	@Test
	public void testSetterLoop() throws Exception {
		testee.initialize();

		PortDescription set = new PortDescription(DigitalIO.Port.DATA0,
				DigitalIO.Bit.BIT1);
		while (true) {
			// System.out.println("1");
			testee.setBit(set, true);
			// testee.sleep(10);
			testee.setBit(set, false);
			// System.out.println("0");
			// testee.sleep(10);
		}
	}

}
