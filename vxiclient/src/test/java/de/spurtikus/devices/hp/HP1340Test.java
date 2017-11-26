package de.spurtikus.devices.hp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import org.hamcrest.core.IsEqual;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import de.spurtikus.Timer;
import de.spurtikus.vxi.connectors.ConnectorConfig;
import de.spurtikus.vxi.connectors.DeviceLink;
import de.spurtikus.vxi.connectors.VXIConnector;
import de.spurtikus.vxi.connectors.VXIConnectorFactory;
import de.spurtikus.vxi.connectors.serial.GPIBSerialConnector;
import de.spurtikus.vxi.connectors.serial.GPIBSerialConnectorConfig;
import de.spurtikus.vxi.service.Configuration;
import de.spurtikus.vxi.util.CommunicationUtil;

/**
 * TODO: see NOT YET IMPLEMENTED for non working parts.
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
	public void userDefinedWaveForm_Ramp() throws Exception {
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

	@Test
	public void userDefinedWaveForms()
			throws Exception {
	  String answer;
	  
	  double maxValue = 5.0; // maximum allowed y value (to prevent data errors on loading waveforms)
			  
	  testee.setFrequency(3E3);
	  testee.setAmplitude(maxValue);
	  answer = vxiConnector.send_and_receive(theLid, "*IDN?");
	  System.out.println(answer); 
	  vxiConnector.send(theLid, "*RST");
	  
	  testee.setUserDefinedWaveform(waveformValues_Ramp(), maxValue);
	  testee.start();
	  Thread.sleep(5000);
	  testee.stop();
	
	  testee.setUserDefinedWaveform(waveformValues_DampedSine(), maxValue);
	  testee.start();
	  Thread.sleep(5000);
	  testee.stop();

	  testee.setUserDefinedWaveform(waveformValues_ChargeDischarge(), maxValue);
	  testee.start();
	  Thread.sleep(5000);
	  testee.stop();

	  testee.setUserDefinedWaveform(waveformValues_HalfRectifiedSine(), maxValue);
	  testee.start();
	  Thread.sleep(5000);
	  testee.stop();

	  testee.setUserDefinedWaveform(waveformValues_SpikedSine(), maxValue);
	  testee.start();
	  Thread.sleep(5000);
	  testee.stop();

	  // DAC values tests
	  testee.setUserDefinedWaveform(waveformValues_DampedSine_DAC());
	  testee.start();
	  Thread.sleep(5000);
	  testee.stop();

	  //writeWaveformValues_DampedSine_DAC_ArbBlock(vxiConnector, theLid); // 3950ms

	}

	/**
	 * Ramp wave function.
	 * 
	 * @return
	 */
	private double[] waveformValues_Ramp() {
		double waveform[] = new double[4096];
		for (int i = 0; i < waveform.length; i++) {
			waveform[i] = 0.00122 * (double) i;
		}
		return waveform;
	}

	/**
	 * Damped sine wave function.
	 * 
	 * @return
	 */
	private double[] waveformValues_DampedSine() {
		double waveform[] = new double[4096];
		double a = 4.0 / 4096.0;
		double w = 2 * Math.PI / 50.0;
		for (int i = 0; i < waveform.length; i++) {
			waveform[i] = Math.exp(-a * i) * Math.sin(w * i);
		}
		return waveform;
	}

	private short[] waveformValues_DampedSine_DAC() throws Exception {
		double v;
		short waveform[] = new short[4096];
		double a = 4.0 / 4096.0;
		double w = 2 * Math.PI / 50.0;
		for (int i = 0; i < 4096; i++) {
			v = Math.exp(-a * i) * Math.sin(w * i);
			short d = HP1340.voltsToDACCode(v);
			System.out.println(v + "-> " + d);
			waveform[i]= d;
		}
		return waveform;
	}

	/**
	 * Charge/Discharge curve function.
	 * 
	 * @return
	 */
	private double[] waveformValues_ChargeDischarge() {
		double v;
		double waveform[] = new double[4096];
		double rc = 400.0;
		for (int i = 0; i < waveform.length; i++) {
			v = 0;
			if (i > 0 && i < 2047) {
				v = 1 - Math.exp(-i / rc);
			}
			if (i >= 2047) {
				v = (1 - Math.exp(-2048 / rc))
						- (1 - Math.exp(-(i - 2047) / rc));
			}
			waveform[i] = v;
		}
		return waveform;
	}

	/**
	 * Half rectified sine function.
	 * 
	 * @return
	 */
	private double[] waveformValues_HalfRectifiedSine() {
		double v;
		double waveform[] = new double[4096];
		for (int i = 0; i < 2048; i++) {
			waveform[i] = Math.sin(2 * Math.PI * ((double) i / 4096.0));
		}
		for (int i = 2048; i < waveform.length; i++) {
			v = 0.0;
			waveform[i] = 0.0;
		}
		return waveform;
	}

	/**
	 * Sine waveform with a spike (peak).
	 * 
	 * @param maxValue
	 * @return
	 */
	private double[] waveformValues_SpikedSine() {
		double waveform[] = new double[4096];
		waveform[0] = 0.0;
		for (int i = 1; i < waveform.length; i++) {
			waveform[i] = 0.5 * Math.sin(2 * Math.PI * ((double) i / 4096.0));
		}
		int width = 50;
		for (int j = 1; j <= width / 2; j++) {
			waveform[j + 1024] = waveform[j + 1024] + (double) j * 0.04;
		}
		for (int j = 1; j <= width / 2; j++) {
			waveform[j + 1024 + width / 2] = waveform[j + 1024 + width / 2] + j
					+ (1.0 - (double) j * 0.04);
		}
		return waveform;
	}

	/**
	 * TODO argument creation is wrong
	 * 
	 * @param vxiConn
	 * @param link
	 * @throws Exception
	 */
	private void writeWaveformValues_DampedSine_DAC_ArbBlock(
			VXIConnector vxiConn, DeviceLink link) throws Exception {
		double v;
		String answer = "TO BE IMPLEMENTED";// ;testee.send_and_receive(link,
											// "SOUR:ARB:DAC:SOUR INT");
		System.out.println(answer);
		vxiConn.send(link, "SOUR:LIST:SEGM:SEL A");

		String valuesPrefix = "SOUR:LIST:SEGM:VOLT:DAC #48192";
		int valuesLength = valuesPrefix.length() + 8192 + 1;
		byte values[] = new byte[valuesLength];
		int i;
		for (i = 0; i < valuesPrefix.length(); i++) {
			values[i] = (byte) valuesPrefix.charAt(i);
		}

		double a = 4.0 / 4096.0;
		double w = 2 * Math.PI / 50.0;
		short d;
		for (int j = 0; j < 4096; j++) {
			v = Math.exp(-a * j) * Math.sin(w * j);

			boolean fakeWave = true;
			if (fakeWave) {
				v = 0.0;
				if (j / 3000 > 0) {
					v = 1.0;
				}
			}
			d = testee.voltsToDACCode(v);
			if (d > 4095)
				d = 4095;
			if (d < 0)
				d = 0;
			System.out.println(v + "-> " + d);
			values[i++] = (byte) (d >> 8); // MSB first
			values[i++] = (byte) (d & 0xff);
		}
		CommunicationUtil.dumpByteArray(values, valuesLength);

		for (int j = valuesPrefix.length(); j < valuesPrefix.length()
				+ 8192; j += 2) {
			System.out.println(CommunicationUtil.byteToHexString(values[j])
					+ " " + CommunicationUtil.byteToHexString(values[j + 1]));

			switch (j / 800) {
			case 0:
				d = 0;
				break;
			case 1:
				d = 1248;
				break;
			case 2:
				d = 2048;
				break;
			case 3:
				d = 2448;
				break;
			case 4:
				d = 3248;
				break;
			default:
				d = 4095;
				break;
			}
		}
		// System.out.print(values);
		values[i] = '\n';
		// testee.send(link, values); <------------ fix here
		System.out.println(answer);
	}

	private void checkErrors(VXIConnector testee, DeviceLink link)
			throws IOException {
		boolean hasErrors = true;
		while (hasErrors) {
			byte[] answer = null; // testee.send_and_receive(link, "SYST:ERR?");
			String s = "TO BE IMPLEMENTED"; // CommunicationUtil.asByteArray(answer,
											// 60);
			System.out.println(s);
			if (s.contains("+0,\"No error\"")) {
				hasErrors = false;
			}
		}
	}

}
