package de.spurtikus.devices.hp;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import de.spurtikus.devices.hp.DigitalIO.PortDescription;
import de.spurtikus.vxi.connectors.ConnectorConfig;
import de.spurtikus.vxi.connectors.DeviceLink;
import de.spurtikus.vxi.connectors.VXIConnector;
import de.spurtikus.vxi.connectors.VXIConnectorFactory;

import static org.junit.Assert.*;

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
		final String test_Serial_or_RPC = "RPC"; // "RPC" or "Serial"
		final String TEST_DEVICE_NAME = "hp1330";

		// Load configuration
		ConnectorConfig config = loadConfig(test_Serial_or_RPC);
		System.out.println(config);

		VXIConnector vxiConnector = VXIConnectorFactory.getConnector(config);

		String deviceid = config.getDeviceIdByName(TEST_DEVICE_NAME);
		assertNotNull(deviceid);
		DeviceLink theLid = vxiConnector.initialize(config, deviceid);
		testee = new DigitalIO(vxiConnector, theLid);

		testee.initialize();
	}

	// @Ignore
	@Test
	public void testGetAndSet_true() throws Exception {
		PortDescription set = new PortDescription(DigitalIO.Port.DATA0,
				DigitalIO.Bit.BIT1);
		testee.setBit(set, true);
		boolean b = testee.getBit(set);
		System.out.println("Bit value: " + b);
		assertTrue(b);
	}

	@Test
	public void testGetAndSet_false() throws Exception {
		PortDescription set = new PortDescription(DigitalIO.Port.DATA0,
				DigitalIO.Bit.BIT1);
		testee.setBit(set, false);
		boolean b = testee.getBit(set);
		System.out.println("Bit value: " + b);
		assertFalse(b);
	}

	@Test
	public void testGetAndSet_byte() throws Exception {
		PortDescription set = new PortDescription(DigitalIO.Port.DATA1);

		testee.setByte(set, 0);
		int b = testee.getByte(set);
		System.out.println("Byte value: " + b);
		//assertFalse(b);
	}

	@Test
	public void testGetAndSet_Polarity_NEG() throws Exception {
		PortDescription set = new PortDescription(DigitalIO.Port.DATA0,
				DigitalIO.Bit.BIT1);
		testee.setPolarity(set, DigitalIO.Polarity.NEG);
		String b = testee.getPolarity(set);
		System.out.println("Polarity value: " + b);
		assertEquals( "NEG", b);
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
