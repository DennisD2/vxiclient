package de.spurtikus.devices.hp;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.spurtikus.vxi.connectors.DeviceLink;
import de.spurtikus.vxi.connectors.VXIConnector;

/**
 * 
 * HP/Agilent/Keysight E1333 Frequency Counter.
 * 
 * Currently implemented functions:
 * 
 * *RST
 * 
 * INP:COUP DC
 * 
 * INP:IMP MIN
 * 
 * *idn?
 * 
 * HEWLETT-PACKARD,E1333A,0,A.04.01
 * 
 * SENS1:FUNC:FREQ
 * 
 * SENS1:FREQ:APER 32.768
 * 
 * READ1?
 * 
 * +0.0000000000E+000
 * 
 *
 * @author dennis
 * 
 */
public class HP1333 extends BaseHPDevice {
	static Logger logger = LoggerFactory.getLogger(HP1333.class);

	// Totalizer, Frequency, Ratio, Period Average, Pulse Width, Time Interval
	public enum CounterConfiguration {
		TOT, FREQ, RAT, PER, NWID, TINT
	}

	// INPUT
	public enum Coupling {
		AC, DC
	}

	// INPUT
	public enum Attenuation {
		MIN, MAX, DEF
	}

	// INPUT
	public enum Impedance {
		MIN, MAX, DEF
	}

	// INPUT event slope if input signal
	public enum Slope {
		POS, NEG
	}

	private Double apertures[] = { 65.536, 32.768, 16.384, 8.192, 4.096, 2.048,
			1.024, .512, .256, .128, .064, .032, .016, .008, .004, .002 };
	/**
	 * True if Counter is started.
	 */
	private boolean counter1Started;
	private boolean counter2Started;
	private boolean counter3Started;

	/**
	 * CTR. Requires vxiConnector object as parameter.
	 * 
	 * @param parent
	 *            vxiConnector containing this device.
	 */
	public HP1333(VXIConnector parent, DeviceLink link) {
		super(parent, link);
	}

	/**
	 * Checks that channel value is in allowed range.
	 * 
	 * @param channel
	 *            channel value to check
	 * @param includeChannel3
	 *            set to true if channel 3 is also an allowed channel.
	 * @return
	 */
	private boolean validateChannel(int channel, boolean includeChannel3) {
		return channel == 1 || channel == 2
				|| (includeChannel3 && (channel == 3));
	}

	/**
	 * Initializes device. Sets up some useful start configuration for DMM.
	 * 
	 * @throws Exception
	 */
	public void initialize() throws Exception {
		vxiConnector.send(deviceLink, "*RST;*OPC?");
		sleep(100);
	}

	/**
	 * Configures channel mode.
	 * 
	 * TODO: SENS or CONF ?
	 * 
	 * @param channel
	 *            channel to control
	 * @param configuration
	 *            mode
	 * @throws Exception
	 */
	public void configureSense(int channel, CounterConfiguration configuration)
			throws Exception {
		if (!validateChannel(channel, false)) {
			throw new IOException("Invalid channel");
		}
		// SENS or CONF?
		vxiConnector.send(deviceLink,
				"SENS" + channel + ":FUNC:" + configuration.toString());
	}
	
	/**
	 * Configures channel mode.
	 * 
	 * TODO: SENS or CONF ?
	 * 
	 * @param channel
	 *            channel to control
	 * @param configuration
	 *            mode
	 * @throws Exception
	 */
	public void configureConf(int channel, CounterConfiguration configuration)
			throws Exception {
		if (!validateChannel(channel, false)) {
			throw new IOException("Invalid channel");
		}
		// SENS or CONF?
		vxiConnector.send(deviceLink,
				"SENS" + channel + ":FUNC:" + configuration.toString());
	}


	/**
	 * Controls low pass filter. Affects channel 1&2.
	 * 
	 * @param filter
	 *            filter value. If true, the low pass filter is used.
	 * @throws Exception
	 */
	public void setLowPassFilter(boolean filter) throws Exception {
		String f = filter ? "ON" : "OFF";
		vxiConnector.send(deviceLink, "INP:FILT " + f);
	}

	/**
	 * Controls coupling. Affects channel 1&2.
	 * 
	 * @param coupling
	 *            AC or DC coupling.
	 * @throws Exception
	 */
	public void setCoupling(Coupling coupling) throws Exception {
		vxiConnector.send(deviceLink, "INP:COUP " + coupling.toString());
	}

	/**
	 * Controls 20dB attenuation. Affects channel 1&2.
	 * 
	 * @param attenuation
	 *            attenuation value. MIN for no attenuation, MAX for 20dB
	 *            attenuation.
	 * @throws Exception
	 */
	public void setAttenuation(Attenuation attenuation) throws Exception {
		// TODO in manual p 23 , attenuation can also be a number ?!
		vxiConnector.send(deviceLink, "INP:ATT " + attenuation.toString());
	}

	/**
	 * Controls impedance. 50 Ohms or 1MOhm. Affects channel 1&2.
	 * 
	 * @param impedance
	 *            impedance to use. MIN for 50 ohms, MAX for 1 MOhms.
	 * @throws Exception
	 */
	public void setImpedance(Impedance impedance) throws Exception {
		// TODO in manual p 23 , impedance can also be a number ?!
		vxiConnector.send(deviceLink, "INP:IMP " + impedance.toString());
	}

	/**
	 * Controls aperture. Float value between 0.002 seconds (2ms) and 32.768
	 * seconds.
	 * 
	 * @param channel
	 *            channel to set
	 * @param aperture
	 *            aperture to use. Double value.
	 * @throws Exception
	 */
	public void setAperture(int channel, double aperture) throws Exception {
		//
		vxiConnector.send(deviceLink,
				"SENS" + channel + ":FREQ:APER " + aperture);
	}

	/**
	 * Set event level for a channel.
	 * 
	 * @param channel
	 *            channel to set
	 * @param level
	 *            level to use. Double value.
	 * @throws Exception
	 */
	public void setEventLevel(int channel, double level) throws Exception {
		if (!validateChannel(channel, false)) {
			throw new IOException("Invalid channel");
		}
		String sense = "";
		if (channel != 1) {
			sense = "SENS" + channel + ":";
		}
		vxiConnector.send(deviceLink, sense + "EVEN:LEV " + level);
	}

	/**
	 * Sets event slope. Positive or negative slope can be selected.
	 * 
	 * @param channel
	 *            channel to set
	 * @param slope
	 *            slope to use
	 * @throws Exception
	 */
	public void setEventSlope(int channel, Slope slope) throws Exception {
		if (!validateChannel(channel, false)) {
			throw new IOException("Invalid channel");
		}
		String sense = "";
		if (channel != 1) {
			sense = "SENS" + channel + ":";
		}
		vxiConnector.send(deviceLink, sense + "EVEN:SLOP " + slope);
	}

	// See user page 25 weiter mit INIT
	public void start(int channel) throws Exception {
		if (!validateChannel(channel, true)) {
			throw new IOException("Invalid channel");
		}
		boolean started = false;
		switch (channel) {
		case 1:
			if (counter1Started) {
				started = true;
			}
			break;
		case 2:
			if (counter2Started) {
				started = true;
			}
			break;
		case 3:
			if (counter3Started) {
				started = true;
			}
			break;
		default:
			logger.error("No such counter channel " + channel);
			return;
		}
		if (started) {
			logger.info("Counter channel " + channel + " already started.");
			return;
		}
		vxiConnector.send(deviceLink, "INIT" + channel);
	}

	public double fetch(int channel) throws Exception {
		if (!validateChannel(channel, true)) {
			throw new IOException("Invalid channel");
		}
		String s = vxiConnector.send_and_receive(deviceLink,
				"FETC" + channel + "?");
		double v = Double.parseDouble(s);
		return v;
	}

	/**
	 * Execute a measurement. Use default counter configuration set before. See
	 * 
	 * @param channel
	 *            channel to measure
	 * @return measured value.
	 * @throws Exception
	 */
	public double measure(int channel) throws Exception {
		if (!validateChannel(channel, true)) {
			throw new IOException("Invalid channel");
		}
		// TODO: handle XXX
		String s = vxiConnector.send_and_receive(deviceLink,
				"READ" + channel + "?");
		double v = Double.parseDouble(s);
		return v;
	}

	/**
	 * Execute a measurement with a given configuration.
	 * 
	 * @param channel
	 *            channel to measure
	 * @param configuration
	 *            configuration to use
	 * @return measured value.
	 * @throws Exception
	 */

	public double measure(int channel, CounterConfiguration configuration)
			throws Exception {
		if (!validateChannel(channel, true)) {
			throw new IOException("Invalid channel");
		}
		// TODO: for the "PER" case, see page
		// user manual 31. there are additional args for // period and
		// resolution.
		String s = vxiConnector.send_and_receive(deviceLink,
				"MEAS" + channel + ":" + configuration + "?");
		double v = Double.parseDouble(s);
		return v;
	}

}
