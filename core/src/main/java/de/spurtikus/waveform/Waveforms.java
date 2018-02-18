package de.spurtikus.waveform;

import de.spurtikus.devices.hp.HP1340;
import de.spurtikus.vxi.connectors.DeviceLink;
import de.spurtikus.vxi.connectors.VXIConnector;
import de.spurtikus.vxi.util.CommunicationUtil;

/**
 * Methods generating common waveforms.
 * 
 * @author dennis
 *
 */
public class Waveforms {
	/**
	 * Ramp wave function.
	 * 
	 * @return double values.
	 */
	public static double[] waveformValues_Ramp() {
		double waveform[] = new double[4096];
		for (int i = 0; i < waveform.length; i++) {
			waveform[i] = 0.00122 * (double) i;
		}
		return waveform;
	}

	/**
	 * Ramp wave function.
	 * 
	 * @return DAC values as DAC values.
	 */
	public static short[] waveformValues_Ramp_DAC() {
		double v;
		short waveform[] = new short[4096];
		for (int i = 0; i < waveform.length; i++) {
			v = 0.00122 * (double) i;
			short d = HP1340.voltsToDACCode(v);
			// System.out.println(v + "-> " + d);
			waveform[i] = d;
		}
		return waveform;
	}
	/**
	 * Ramp wave function.
	 * 
	 * @return DAC values as 16 bit integers (shorts).
	 */
	public static short[] waveformValues_Ramp_Short() {
		double v;
		short waveform[] = new short[4096];
		for (int i = 0; i < waveform.length; i++) {
			v = 0.00122 * (double) i;
			short d = HP1340.voltsToShort(v);
			// System.out.println(v + "-> " + d);
			waveform[i] = d;
		}
		return waveform;
	}

	/**
	 * Damped sine wave function.
	 * 
	 * @return double values.
	 */
	public static double[] waveformValues_DampedSine() {
		double waveform[] = new double[4096];
		double a = 4.0 / 4096.0;
		double w = 2 * Math.PI / 50.0;
		for (int i = 0; i < waveform.length; i++) {
			waveform[i] = Math.exp(-a * i) * Math.sin(w * i);
		}
		return waveform;
	}

	/**
	 * Damped sine wave function.
	 * 
	 * @return DAC values.
	 */
	public static short[] waveformValues_DampedSine_DAC() throws Exception {
		double v;
		short waveform[] = new short[4096];
		double a = 4.0 / 4096.0;
		double w = 2 * Math.PI / 50.0;
		for (int i = 0; i < waveform.length; i++) {
			v = Math.exp(-a * i) * Math.sin(w * i);
			short d = HP1340.voltsToDACCode(v);
			// System.out.println(v + "-> " + d);
			waveform[i] = d;
		}
		return waveform;
	}

	/**
	 * Charge/Discharge curve function.
	 * 
	 * @return double values.
	 */
	public static double[] waveformValues_ChargeDischarge() {
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
	 * Charge/Discharge curve function.
	 * 
	 * @return DAC values.
	 */
	public static short[] waveformValues_ChargeDischarge_DAC() {
		double v;
		short waveform[] = new short[4096];
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
			waveform[i] = HP1340.voltsToDACCode(v);
		}
		return waveform;
	}

	/**
	 * Half rectified sine function.
	 * 
	 * @return double values.
	 */
	public static double[] waveformValues_HalfRectifiedSine() {
		double waveform[] = new double[4096];
		for (int i = 0; i < 2048; i++) {
			waveform[i] = Math.sin(2 * Math.PI * ((double) i / 4096.0));
		}
		for (int i = 2048; i < waveform.length; i++) {
			waveform[i] = 0.0;
		}
		return waveform;
	}

	/**
	 * Half rectified sine function.
	 * 
	 * @return DAC values.
	 */
	public static short[] waveformValues_HalfRectifiedSine_DAC() {
		double v;
		short waveform[] = new short[4096];
		for (int i = 0; i < 2048; i++) {
			v = Math.sin(2 * Math.PI * ((double) i / 4096.0));
			waveform[i] = HP1340.voltsToDACCode(v);
		}
		for (int i = 2048; i < waveform.length; i++) {
			waveform[i] = HP1340.voltsToDACCode(0);
		}
		return waveform;
	}

	/**
	 * Sine waveform with a spike (peak).
	 * 
	 * @param maxValue
	 * @return
	 */
	public static double[] waveformValues_SpikedSine() {
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
	 * Sine waveform with a spike (peak).
	 * 
	 * @param maxValue
	 * @return
	 */
	public static short[] waveformValues_SpikedSine_DAC() {
		double v;
		short waveform[] = new short[4096];
		waveform[0] = HP1340.voltsToDACCode(0);
		for (int i = 1; i < waveform.length; i++) {
			v = 0.5 * Math.sin(2 * Math.PI * ((double) i / 4096.0));
			waveform[i] = HP1340.voltsToDACCode(v);
		}
		int width = 50;
		for (int j = 1; j <= width / 2; j++) {
			v = waveform[j + 1024] + (double) j * 0.04;
			waveform[j + 1024] = HP1340.voltsToDACCode(v);
		}
		for (int j = 1; j <= width / 2; j++) {
			v = waveform[j + 1024 + width / 2] + j + (1.0 - (double) j * 0.04);
			waveform[j + 1024 + width / 2] = HP1340.voltsToDACCode(v);
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
	public static void writeWaveformValues_DampedSine_DAC_ArbBlock(
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
			d = HP1340.voltsToDACCode(v);
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

}
