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
	}

	@Ignore
	@Test
	public void testStandardWaveForms() throws Exception {
		testee.initialize();

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
	public void testBuiltinWaveForms() throws Exception {
		testee.initialize();

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
		
		testee.setShape(HP1340.BuiltinWaveForm.Sine_Linear_Rising_8_cycles,'A'); // cutie
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
	public void testSweep() throws Exception {
		testee.initialize();

		// set sweep parameters
		testee.setSweep(1e3, 1e5, 1000, 25, 5.0,HP1340.StandardWaveForm.SQUARE);

		testee.start();
	}

	@Ignore
	@Test
	public void testSweepMarker() throws Exception {
		testee.initialize();

		// set sweep parameters
		testee.setSweep(1e3, 1e5, 1000, 25, 5.0, HP1340.StandardWaveForm.SQUARE);

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
	public void testUserDefinedWaveForm() throws Exception {
		testee.initialize();

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

	/**
	 * 
	 * @throws Exception
	 */
	//@Ignore
	@Test
	public void testWriteWithAnswer_XonXoff_and_None()
			throws Exception {
	  // @formatter:off
	
	  testee.initialize();
	  String answer;
	  
	  answer = vxiConnector.send_and_receive(theLid, "*IDN?");
	  System.out.println(answer); 
	  vxiConnector.send(theLid, "*RST");
	  
	  vxiConnector.send(theLid, "SOUR:ROSC:SOUR INT;");
	  vxiConnector.send(theLid, ":SOUR:FREQ:FIX 1E3;");
	  vxiConnector.send(theLid, ":SOUR:FUNC:SHAP USER;");
	  vxiConnector.send(theLid, ":SOUR:VOLT:LEV:IMM:AMPL 5.1V");
	  
	  Timer timer = new Timer(); timer.start(); //
	  //writeWaveformValues_Ramp(vxiConnector, theLid); // 9172ms
	  //writeWaveformValues_DampedSine(vxiConnector, theLid); // 10087ms 
	  //writeWaveformValues_ChargeDischarge(vxiConnector, theLid); // 9481ms
	  //writeWaveformValues_SpikedSine(vxiConnector, theLid);
	  //writeWaveformValues_HalfRectifiedSine(vxiConnector, theLid);
	  writeWaveformValues_DampedSine_DAC(vxiConnector, theLid); // 6342ms
	  //writeWaveformValues_DampedSine_DAC_ArbBlock(vxiConnector, theLid); // 3950ms
	  
	  timer.stopAndPrintln(); 
	  // checkErrors(testee);
	  
	  vxiConnector.send(theLid, "SOUR:FUNC:USER A");
	  vxiConnector.send(theLid, "INIT:IMM");
	  answer = vxiConnector.send_and_receive(theLid, "SOUR:LIST:SEGM:SEL?");
	  System.out.println(answer);
	  answer = vxiConnector.send_and_receive(theLid, "SOUR:LIST:SEGM:VOLT:POIN?");
	  System.out.println(answer);
	  
//	  while (testee.isStarted()) { 
//		  Thread.sleep(7000); 
//		  testee.stop(); 
//	  }
//	  testee.close();

	  // @formatter:on
	}

	private void writeWaveformValues_Ramp(VXIConnector testee, DeviceLink link)
			throws Exception {
		double v;
		NumberFormat formatter = new DecimalFormat("#0.00");
		testee.send(link, "SOUR:LIST:SEGM:SEL A");
		String values = "SOUR:LIST:SEGM:VOLT ";
		for (int i = 0; i < 4096; i++) {
			v = 0.00122 * (double) i;
			values += formatter.format(v).replace(",", ".");
			if (i != 4095) {
				values += ",";
			}
		}
		testee.send(link, values);
	}

	private void writeWaveformValues_DampedSine(VXIConnector testee,
			DeviceLink link) throws Exception {
		double v;
		NumberFormat formatter = new DecimalFormat("#0.00");
		testee.send(link, "SOUR:LIST:SEGM:SEL A");
		String values = "SOUR:LIST:SEGM:VOLT ";
		double a = 4.0 / 4096.0;
		double w = 2 * Math.PI / 50.0;
		for (int i = 0; i < 4096; i++) {
			v = Math.exp(-a * i) * Math.sin(w * i);
			values += formatter.format(v).replace(",", ".");
			if (i != 4095) {
				values += ",";
			}
		}
		// System.out.print(values);
		testee.send(link, values);
	}

	private void writeWaveformValues_DampedSine_DAC(VXIConnector testee,
			DeviceLink link) throws Exception {
		double v;
		testee.send(link, "SOUR:ARB:DAC:SOUR INT");
		testee.send(link, "SOUR:LIST:SEGM:SEL A");
		String values = "SOUR:LIST:SEGM:VOLT:DAC ";
		double a = 4.0 / 4096.0;
		double w = 2 * Math.PI / 50.0;
		for (int i = 0; i < 4096; i++) {
			v = Math.exp(-a * i) * Math.sin(w * i);
			short d = voltsToDACCode(v);
			System.out.println(v + "-> " + d);
			values += d;
			if (i != 4095) {
				values += ",";
			}
		}
		// System.out.print(values);
		values += '\n';
		testee.send(link, values);
	}

	/**
	 * TODO argument creation is wrong
	 * @param testee
	 * @param link
	 * @throws Exception
	 */
	private void writeWaveformValues_DampedSine_DAC_ArbBlock(
			VXIConnector testee, DeviceLink link) throws Exception {
		double v;
		String answer = "TO BE IMPLEMENTED";// ;testee.send_and_receive(link,
											// "SOUR:ARB:DAC:SOUR INT");
		System.out.println(answer);
		testee.send(link, "SOUR:LIST:SEGM:SEL A");
	
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
			d = voltsToDACCode(v);
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
		//testee.send(link, values); <------------ fix here 
		System.out.println(answer);
	}

	// see manual page 109
	@Ignore
	@Test
	public void testDACCodes() {
		short dac = voltsToDACCode(-2.0);
		System.out.println(dac);
		assertEquals(1248, dac);

		dac = voltsToDACCode(0);
		System.out.println(dac);
		assertEquals(2048, dac);

		dac = voltsToDACCode(1);
		System.out.println(dac);
		assertEquals(2448, dac);

		dac = voltsToDACCode(-5.12);
		System.out.println(dac);
		assertEquals(0, dac);

		dac = voltsToDACCode(5.1175);
		System.out.println(dac);
		assertEquals(4095, dac);

	}

	public short voltsToDACCode(double v) {
		short dac = (short) ((v / 0.0025) + 2048);
		if (dac < 0 || dac > 4095) {
			System.out.println("dac value out of range: " + dac);
		}
		return (short) (dac & 0x0fff);
	}

	private void writeWaveformValues_ChargeDischarge(VXIConnector testee,
			DeviceLink link) throws Exception {
		double v;
		NumberFormat formatter = new DecimalFormat("#0.00");
		testee.send(link, "SOUR:LIST:SEGM:SEL A");
		String values = "SOUR:LIST:SEGM:VOLT ";
		double rc = 400.0;
		for (int i = 0; i < 4096; i++) {
			v = 0;
			if (i > 0 && i < 2047) {
				v = 1 - Math.exp(-i / rc);
			}
			if (i >= 2047) {
				v = (1 - Math.exp(-2048 / rc))
						- (1 - Math.exp(-(i - 2047) / rc));
			}
			values += formatter.format(v).replace(",", ".");
			if (i != 4095) {
				values += ",";
			}
		}
		// System.out.print(values);
		testee.send(link, values);
	}

	/**
	 * TODO: this does not work. 0 points are received !?!
	 * @param testee
	 * @param link
	 * @throws Exception
	 */
	private void writeWaveformValues_SpikedSine(VXIConnector testee,
			DeviceLink link) throws Exception {
		double waveform[] = new double[4097];
		NumberFormat formatter = new DecimalFormat("#0.00");
		testee.send(link, "SOUR:LIST:SEGM:SEL A");
		String values = "SOUR:LIST:SEGM:VOLT ";
		waveform[0] = 0.0;
		for (int i = 1; i <= 4096; i++) {
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
		for (int i = 1; i <= 4096; i++) {
			values += formatter.format(waveform[i]).replace(",", ".");
			if (i != 4096) {
				values += ",";
			}
		}
		// System.out.print(values);
		testee.send(link, values);
	}

	private void writeWaveformValues_HalfRectifiedSine(VXIConnector testee,
			DeviceLink link) throws Exception {
		double v;
		NumberFormat formatter = new DecimalFormat("#0.00");
		testee.send(link, "SOUR:LIST:SEGM:SEL A");
		String values = "SOUR:LIST:SEGM:VOLT ";
		for (int i = 0; i < 2048; i++) {
			v = Math.sin(2 * Math.PI * ((double) i / 4096.0));
			values += formatter.format(v).replace(",", ".");
			if (i != 4095) {
				values += ",";
			}

		}
		for (int i = 2048; i < 4096; i++) {
			v = 0.0;
			values += formatter.format(v).replace(",", ".");
			if (i != 4095) {
				values += ",";
			}
		}
		// System.out.print(values);
		testee.send(link, values);
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
