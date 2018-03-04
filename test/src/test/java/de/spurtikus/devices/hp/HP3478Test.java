package de.spurtikus.devices.hp;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import de.spurtikus.vxi.connectors.ConnectorConfig;
import de.spurtikus.vxi.connectors.DeviceLink;
import de.spurtikus.vxi.connectors.VXIConnector;
import de.spurtikus.vxi.connectors.VXIConnectorFactory;
import de.spurtikus.vxi.service.Configuration;

/**
 * Multimeter tests.
 * 
 * @author dennis
 *
 */
public class HP3478Test extends DeviceBaseTest {
	HP3478 testee = null;

	@Before
	public void before() throws Exception {
		final String TEST_DEVICE_NAME;

		// Load configuration
		Configuration.load();
		// get configuration for standalone device
		ConnectorConfig config = Configuration.findConfigByName("hp3478standalone");
		System.out.println(config);

		TEST_DEVICE_NAME = "hp3478";

		VXIConnector vxiConnector = VXIConnectorFactory.getConnector(config);

		String deviceid = config.getDeviceIdByName(TEST_DEVICE_NAME);
		assertNotNull(deviceid);
		DeviceLink theLid = vxiConnector.initialize(config, deviceid);
		testee = new HP3478(vxiConnector, theLid);
	}

	@Test
	public void testMeasureSingle() throws Exception {
		System.out.println("Start...");
		testee.initialize();
		testee.initializeMeasurement(HP3478.Function.DC_VOLTAGE,
				HP3478.Range.Auto, HP3478.AutoZero.On,
				HP3478.Digits.DIGITS5, HP3478.Trigger.INTERNAL);

		Double s = testee.measureSingle();
		System.out.println(s);
		System.out.println("... End");
	}

	@Test
	public void testSetDisplay() throws Exception {
		testee.initialize();
		testee.setDisplay("HELLO WORLD");
	}

	@Test
	public void testFrontPanelEnabled() throws Exception {
		testee.initialize();
		boolean b = testee.isFrontPanelSelected();
		assertTrue(b);
	}

	//@Test
	public void testMeasureLoop() throws Exception {
		testee.initialize();
		testee.initializeMeasurement(HP3478.Function.DC_VOLTAGE,
				HP3478.Range.Auto, HP3478.AutoZero.On,
				HP3478.Digits.DIGITS5, HP3478.Trigger.INTERNAL);

		long start = System.currentTimeMillis();
		int max = 100;
		for (int i = 0; i < max; i++) {
			Double v = testee.measureSingle();
			System.out.println(v);
		}
		long time = System.currentTimeMillis() - start;
		System.out.println("Need " + new Double(time) / 1000 + " seconds for "
				+ max + " measurements.");
	}

}
