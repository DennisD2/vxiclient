package de.spurtikus.devices.hp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.spurtikus.vxi.connectors.DeviceLink;
import de.spurtikus.vxi.connectors.VXIConnector;

/**
 * TODO: XXX: This code needs rework; I assume it will not work . It's just here
 * for future work.
 * 
 * Access to the built in Pacer function in HP130? / HP75000. This pacer
 * generates a TTL level signal with settable period length (up to 2Mhz) and
 * settable number of periods generated.
 * 
 * See manual page 88 "Pacer trigger states".
 * 
 * @author dennis
 *
 */
public class HP1300Pacer extends BaseHPDevice {
	static Logger logger = LoggerFactory.getLogger(HP1300Pacer.class);
	/**
	 * Minimum period length allowed. 500ns -> 2Mhz
	 */
	public final double MIN_PERIOD = 500E-9; //
	/**
	 * Maximum period length allowed. 8.3 seconds.
	 */
	public final double MAX_PERIOD = 8.3;

	/**
	 * Maximum number of cycles allowed.
	 */
	public long MAX_CYCLES = 8388607;

	/**
	 * Pseudo value for infinite number of cycles.
	 */
	public static final long INFINIT_CYCLES = 0;

	/**
	 * Number of pacer cycles emit.0 means infinite number of cycles.
	 */
	private long cycles;

	/**
	 * Length of complete single wave period in seconds.
	 */
	private double periodLength;

	/**
	 * CTR
	 * 
	 * @param parent
	 */
	public HP1300Pacer(VXIConnector parent, DeviceLink link) {
		super(parent, link);
	}

	/**
	 * Initializes pacer. If pacer is running, pacer is stopped. Then paer is
	 * configured with new parameters. Pacer stays in idle mode until a
	 * start/enable method is called.
	 * 
	 * Requires system being in SYSTEM selection
	 * 
	 * @param periodLength
	 *            Value in seconds. Allowed between 500ns and 8.3s.
	 * @param cycles
	 *            Number of cycles, maximum value is MAX_CYCLES. If "0" is
	 *            given, the pacer loops infinite times.
	 * @throws Exception
	 * @throws InterruptedException
	 */
	public void initialize(double periodLength, long cycles) throws Exception {
		stopPacer();
		setCycles(cycles);
		setPeriod(periodLength);
	}

	/**
	 * Sets length of period
	 * 
	 * @param periodLength
	 *            Value in seconds. Allowed between 500ns and 8.3s.
	 * @throws Exception
	 * @throws InterruptedException
	 */
	public void setPeriod(double periodLength) throws Exception {
		if (periodLength < MIN_PERIOD) {
			logger.info("Period value too small. Using " + MIN_PERIOD);
			periodLength = MIN_PERIOD;
		}
		if (periodLength > MAX_PERIOD) {
			logger.info("Period value too big. Using " + MAX_PERIOD);
			periodLength = MAX_PERIOD;
		}
		this.periodLength = periodLength;
		String answer = vxiConnector.send_and_receive(deviceLink,
				"SOUR:PULS:PER " + periodLength);
	}

	/**
	 * Set number of cycles to repeat pacing signal.
	 * 
	 * @param cycles
	 *            Number of cycles, maximum value is MAX_CYCLES. If "0" is
	 *            given, the pacer loops infinit times.
	 * @throws Exception
	 * @throws InterruptedException
	 */
	public void setCycles(long cycles) throws Exception {
		if (cycles > MAX_CYCLES) {
			System.out
					.println("Too much cycles requested. Using " + MAX_CYCLES);
			cycles = MAX_CYCLES;
		}
		this.cycles = cycles;
		String cyclesString = (cycles == INFINIT_CYCLES) ? "INF"
				: String.valueOf(cycles);

		String answer = vxiConnector.send_and_receive(deviceLink,
				"SOUR:PULS:COUN " + cyclesString);
	}

	/**
	 * Start pacer immediately. Pacer trigger source is set to "immediately" and
	 * Pacer is then inited. This starts the pacer.
	 * 
	 * @throws Exception
	 * 
	 * @throws InterruptedException
	 */
	public void startPacerSelfTriggered() throws Exception {
		String answer = vxiConnector.send_and_receive(deviceLink,
				"TRIG:SOUR IMM");
		answer = vxiConnector.send_and_receive(deviceLink, "INIT:IMM");
	}

	/**
	 * Enable pacer to use external trigger. Pacer trigger source is set to
	 * "external" and and Pacer is then inited. The pacer waits for external
	 * trigger and starts then.
	 * 
	 * @throws Exception
	 */
	public void enablePacerExternalTrigger() throws Exception {
		String answer = vxiConnector.send_and_receive(deviceLink,
				"TRIG:SOUR EXT");
		answer = vxiConnector.send_and_receive(deviceLink, "INIT:IMM");
	}

	/**
	 * Stop pacer. Sends ABORT to SYSTEM which stops pacer.
	 * 
	 * @throws Exception
	 */
	public void stopPacer() throws Exception {
		String answer = vxiConnector.send_and_receive(deviceLink, "ABORT");
	}

}
