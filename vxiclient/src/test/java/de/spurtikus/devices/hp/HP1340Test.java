package de.spurtikus.devices.hp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import org.hamcrest.core.IsEqual;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import de.spurtikus.vxi.connectors.ConnectorConfig;
import de.spurtikus.vxi.connectors.DeviceLink;
import de.spurtikus.vxi.connectors.VXIConnector;
import de.spurtikus.vxi.connectors.VXIConnectorFactory;
import de.spurtikus.vxi.connectors.serial.GPIBSerialConnector;
import de.spurtikus.vxi.connectors.serial.GPIBSerialConnectorConfig;
import de.spurtikus.vxi.service.Configuration;
import de.spurtikus.waveform.Waveforms;

/**
 * HP1340 AFG (Arbitrary frequency Generator) tests.
 * 
 * @author dennis
 *
 */
public class HP1340Test {
	public static final int SERIAL_CONFIG = 1;
	public static final int RPC_CONFIG = 2;

	final String TEST_DEVICE_NAME = "hp1340";

	Configuration configuration;
	ConnectorConfig config;
	DeviceLink theLid = null;
	private HP1340 testee;

	VXIConnector vxiConnector;

	@Before
	public void before() throws Exception {
		System.out.println("Start...");

		// Load configuration
		Configuration.load();
		// We assume usable config at some index
		config = Configuration.findConfigById(SERIAL_CONFIG);
		assertNotNull(config);
		// We like to test a net GPIBSerial
		assertThat(config.getClass(),
				IsEqual.equalTo(GPIBSerialConnectorConfig.class));
		System.out.println(config);

		vxiConnector = VXIConnectorFactory.getConnector(config);

		String deviceid = config.getDeviceIdByName(TEST_DEVICE_NAME);
		assertNotNull(deviceid);
		theLid = vxiConnector.initialize(config, deviceid);
		testee = new HP1340(vxiConnector, theLid);

		// TODO: do we really need next 6 lines?
		if (((GPIBSerialConnectorConfig) config)
				.getAdapterType() == ((GPIBSerialConnectorConfig) config).ADAPTER_SERIAL_DIRECT) {
			((GPIBSerialConnector) vxiConnector).selectDevice(theLid, "AFG");
		} else {
			((GPIBSerialConnector) vxiConnector).selectDevice(theLid, 9, 10);
		}
		testee.initialize();
		
		String answer = vxiConnector.send_and_receive(theLid, "*IDN?");
		System.out.println(answer);
	}

	// see manual page 109
	@Test
	public void testDACCodes() {
		short dac = HP1340.voltsToDACCode(-2.0);
		System.out.println(dac);
		assertEquals(1248, dac);

		dac = HP1340.voltsToDACCode(0);
		System.out.println(dac);
		assertEquals(2048, dac);

		dac = HP1340.voltsToDACCode(1);
		System.out.println(dac);
		assertEquals(2448, dac);

		dac = HP1340.voltsToDACCode(-5.12);
		System.out.println(dac);
		assertEquals(0, dac);

		dac = HP1340.voltsToDACCode(5.1175);
		System.out.println(dac);
		assertEquals(4095, dac);
	}

	@Ignore
	@Test
	public void standardWaveForms() throws Exception {
		testee.setAmplitude(5.0);
		testee.setFrequency(3E6);

		testee.setShape(HP1340.StandardWaveForm.RAMP);
		testee.start();
		Thread.sleep(5000);
		testee.stop();

		testee.setShape(HP1340.StandardWaveForm.SINE);
		testee.start();
		Thread.sleep(5000);
		testee.stop();

		testee.setShape(HP1340.StandardWaveForm.SQUARE);
		testee.start();
		Thread.sleep(5000);
		testee.stop();

		testee.setShape(HP1340.StandardWaveForm.DC);
		testee.start();
		Thread.sleep(5000);
		testee.stop();

		testee.setShape(HP1340.StandardWaveForm.TRIANGLE);
		testee.start();
	}

	@Ignore
	@Test
	public void builtinWaveForms() throws Exception {
		testee.setAmplitude(5.0);
		testee.setFrequency(3E3);

		testee.setShape(HP1340.BuiltinWaveForm.Harmonic_Chord_3rd_4th_5th, 'A');
		testee.start();
		Thread.sleep(5000);
		testee.stop();

		testee.setShape(HP1340.BuiltinWaveForm.Haversine, 'A');
		testee.start();
		Thread.sleep(5000);
		testee.stop();

		testee.setShape(HP1340.BuiltinWaveForm.Ramp_Falling, 'A');
		testee.start();
		Thread.sleep(5000);
		testee.stop();

		testee.setShape(HP1340.BuiltinWaveForm.Ramp_Falling_First_20, 'A');
		testee.start();
		Thread.sleep(5000);
		testee.stop();

		testee.setShape(HP1340.BuiltinWaveForm.Ramp_Rising, 'A');
		testee.start();
		Thread.sleep(5000);
		testee.stop();

		testee.setShape(HP1340.BuiltinWaveForm.Ramp_Rising_First_20, 'A');
		testee.start();
		Thread.sleep(5000);
		testee.stop();

		testee.setShape(HP1340.BuiltinWaveForm.Sine, 'A');
		testee.start();
		Thread.sleep(5000);
		testee.stop();

		testee.setShape(HP1340.BuiltinWaveForm.Sine_Linear_Rising_8_cycles,
				'A'); // cutie
		testee.start();
		Thread.sleep(5000);
		testee.stop();

		testee.setShape(HP1340.BuiltinWaveForm.Sine_Positive_Half_Cycle, 'A'); // cutie
		testee.start();
		Thread.sleep(5000);
		testee.stop();

		testee.setShape(HP1340.BuiltinWaveForm.Sinx_per_x, 'A');
		testee.start();
		Thread.sleep(5000);
		testee.stop();

		testee.setShape(HP1340.BuiltinWaveForm.Square, 'A');
		testee.start();
		Thread.sleep(5000);
		testee.stop();

		testee.setShape(HP1340.BuiltinWaveForm.Square_First_10, 'A');
		testee.start();
		Thread.sleep(5000);
		testee.stop();

		testee.setShape(HP1340.BuiltinWaveForm.Square_First_4, 'A'); // cutie
		testee.start();
		Thread.sleep(5000);
		testee.stop();

		testee.setShape(HP1340.BuiltinWaveForm.Triangle, 'A');
		testee.start();
		Thread.sleep(5000);
		testee.stop();

		testee.setShape(HP1340.BuiltinWaveForm.White_Noise, 'A'); // cutie
		testee.start();
		Thread.sleep(5000);
		testee.stop();

		testee.setShape(HP1340.BuiltinWaveForm.White_Noise_Modulated, 'A'); // cutie
		testee.start();
		Thread.sleep(5000);
		testee.stop();
	}

	@Ignore
	@Test
	public void sweep() throws Exception {
		// set sweep parameters
		testee.setSweep(1e3, 1e5, 1000, 25, 5.0,
				HP1340.StandardWaveForm.SQUARE);

		testee.start();
	}

	@Ignore
	@Test
	public void sweepMarkers() throws Exception {
		// set sweep parameters
		testee.setSweep(1e3, 1e5, 1000, 25, 5.0,
				HP1340.StandardWaveForm.SQUARE);

		// set marker parameters
		// See page 167
		// FeedType OUTP:ZERO, SEGM, SOUR:ROSC, SOUR:SWE
		// polarityType INV/NORM
		testee.setMarker(HP1340.MarkerFeedType.SOURCE_SWEEP,
				HP1340.PolarityType.NORM);

		testee.start();
	}

	@Ignore
	@Test
	public void userDefinedWaveForm_Simple_Ramp() throws Exception {
		testee.setFrequency(3E3);
		testee.setAmplitude(5.0);

		Double[] waveForm = new Double[4096];
		int i;
		for (i = 0; i < 4096; i++) {
			waveForm[i] = 0.00122 * i;
		}

		testee.setShape(waveForm, i, 'A');

		testee.start();
	}

	@Ignore
	@Test
	public void userDefinedWaveForms() throws Exception {
		double maxValue = 5.0; // maximum allowed y value (to prevent data
								// errors on loading waveforms)

		testee.setFrequency(3E3);
		testee.setAmplitude(maxValue);
	
		testee.setUserDefinedWaveform(Waveforms.waveformValues_Ramp(), maxValue);
		testee.start();
		Thread.sleep(5000);
		testee.stop();

		testee.setUserDefinedWaveform(Waveforms.waveformValues_DampedSine(), maxValue);
		testee.start();
		Thread.sleep(5000);
		testee.stop();

		testee.setUserDefinedWaveform(Waveforms.waveformValues_ChargeDischarge(), maxValue);
		testee.start();
		Thread.sleep(5000);
		testee.stop();

		testee.setUserDefinedWaveform(Waveforms.waveformValues_HalfRectifiedSine(), maxValue);
		testee.start();
		Thread.sleep(5000);
		testee.stop();

		testee.setUserDefinedWaveform(Waveforms.waveformValues_SpikedSine(), maxValue);
		testee.start();
		Thread.sleep(5000);
		testee.stop();
	}

	@Ignore
	@Test
	public void userDefinedWaveForms_DAC() throws Exception {
		double maxValue = 5.0; // maximum allowed y value (to prevent data
								// errors on loading waveforms)

		testee.setFrequency(3E3);
		testee.setAmplitude(maxValue);

		// DAC values tests
		testee.setUserDefinedWaveform(Waveforms.waveformValues_Ramp_DAC());
		testee.start();
		Thread.sleep(5000);
		testee.stop();

		testee.setUserDefinedWaveform(Waveforms.waveformValues_DampedSine_DAC());
		testee.start();
		Thread.sleep(5000);
		testee.stop();

		testee.setUserDefinedWaveform(Waveforms.waveformValues_ChargeDischarge_DAC());
		testee.start();
		Thread.sleep(5000);
		testee.stop();

		testee.setUserDefinedWaveform(Waveforms.waveformValues_HalfRectifiedSine_DAC());
		testee.start();
		Thread.sleep(5000);
		testee.stop();

		testee.setUserDefinedWaveform(Waveforms.waveformValues_SpikedSine_DAC());
		testee.start();
		Thread.sleep(5000);
		testee.stop();
	}

	//@Ignore
	@Test
	public void userDefinedWaveForms_ArbBlock() throws Exception {
		double maxValue = 5.0; // maximum allowed y value (to prevent data
								// errors on loading waveforms)

		testee.setFrequency(3E3);
		testee.setAmplitude(maxValue);

		testee.setUserDefinedWaveformBlk(Waveforms.waveformValues_Ramp_DAC());
		testee.start();
		Thread.sleep(5000);
		testee.stop();
	}

}
