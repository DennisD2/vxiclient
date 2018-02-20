package de.spurtikus.devices.hp;

import static org.junit.Assert.assertNotNull;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import de.spurtikus.devices.hp.HP1333.Attenuation;
import de.spurtikus.devices.hp.HP1333.CounterConfiguration;
import de.spurtikus.devices.hp.HP1333.Coupling;
import de.spurtikus.devices.hp.HP1333.Impedance;
import de.spurtikus.devices.hp.HP1333.Slope;
import de.spurtikus.vxi.connectors.ConnectorConfig;
import de.spurtikus.vxi.connectors.DeviceLink;
import de.spurtikus.vxi.connectors.VXIConnector;
import de.spurtikus.vxi.connectors.VXIConnectorFactory;

/**
 * Counter tests.
 * 
 * @author dennis
 *
 */
public class HP1333Test extends DeviceBaseTest {
	private HP1333 testee = null;

	@Before
	public void before() throws Exception {
		final String test_Serial_or_RPC = "Serial"; // "RPC" or "Serial"
		final String TEST_DEVICE_NAME = "hp1333";

		System.out.println("Start...");

		// Load configuration
		ConnectorConfig config = loadConfig(test_Serial_or_RPC);
		System.out.println(config);

		VXIConnector vxiConnector = VXIConnectorFactory.getConnector(config);

		String deviceid = config.getDeviceIdByName(TEST_DEVICE_NAME);
		assertNotNull(deviceid);
		DeviceLink theLid = vxiConnector.initialize(config, deviceid);
		testee = new HP1333(vxiConnector, theLid);

		testee.initialize();

		String answer = vxiConnector.send_and_receive(theLid, "*IDN?");
		System.out.println(answer);
	}

	@Test
	public void frequencyMeasurement() throws Exception {
		testee.initialize();
		int channel = 1;

		CounterConfiguration configuration = CounterConfiguration.FREQ;
		testee.configureSense(channel, configuration);

		double aperture = 0.128;
		testee.setAperture(channel, aperture);

		Attenuation attenuation = Attenuation.MIN;
		testee.setAttenuation(attenuation);

		Coupling coupling = Coupling.DC;
		testee.setCoupling(coupling);

		Impedance impedance = Impedance.MIN;
		testee.setImpedance(impedance);

		testee.setLowPassFilter(false);

		double answer;
		answer = testee.measure(channel);
		// answer = testee.measure(2);
		// answer = testee.measure(3);

		System.out.println(answer);
	}

	@Ignore
	@Test
	public void totalizerMeasurement() throws Exception {
		testee.initialize();
		int channel = 1;

		int milliseconds = 100;

		CounterConfiguration configuration = CounterConfiguration.TOT;
		testee.configureConf(channel, configuration);

		double level = 1.0;
		testee.setEventLevel(channel, level);

		testee.setLowPassFilter(true);

		Coupling coupling = Coupling.DC;
		testee.setCoupling(coupling);

		testee.start(channel);
		// Count all events during 1000ms
		Thread.sleep(milliseconds);

		double answer;
		answer = testee.fetch(channel);

		System.out.println(answer);
	}

	@Ignore
	@Test
	public void ratioMeasurement() throws Exception {
		testee.initialize();
		int channel = 1;
		int otherChannel = 2;

		CounterConfiguration configuration = CounterConfiguration.RAT;
		testee.configureSense(channel, configuration);

		double level = 1.0;
		testee.setEventLevel(channel, level);
		testee.setEventLevel(otherChannel, level);

		Coupling coupling = Coupling.DC;
		testee.setCoupling(coupling);

		double answer;
		answer = testee.measureRatio(channel);

		System.out.println(answer);
	}

	@Ignore
	@Test
	public void periodAverageMeasurement() throws Exception {
		// page 31
	}

	@Ignore
	@Test
	public void pulseWidthMeasurement() throws Exception {
		// page 32
	}

	@Ignore
	@Test
	public void timeIntervalMeasurement() throws Exception {
		// page 34
	}

	@Ignore
	@Test
	public void eventLevelSlope() throws Exception {
		int channel = 1;

		Slope slope = Slope.POS;
		testee.setEventSlope(channel, slope);

		// System.out.println(answer);
	}

}
