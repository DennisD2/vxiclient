package de.spurtikus.vxi.service;

import java.util.Arrays;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.spurtikus.devices.hp.HP1340;
import de.spurtikus.devices.hp.HP1340.BuiltinWaveForm;
import de.spurtikus.devices.hp.HP1340.MarkerFeedType;
import de.spurtikus.devices.hp.HP1340.PolarityType;
import de.spurtikus.devices.hp.HP1340.StandardWaveForm;
import de.spurtikus.vxi.Constants;

/**
 * Boundary for Arbitrary Frequency Generators (AFGs).
 * 
 * Working devices:
 * 
 * HP E1340A, see {HP1340}.
 * 
 * @author dennis
 *
 */
@Path("/" + Constants.URL_AFG)
public class AFGBoundary extends AbstractBoundary<HP1340> {
	private static final String MSG_NO_WRAPPER = "Cannot get wrapper instance. This is usually an initialization problem.";
	private Logger logger = LoggerFactory.getLogger(AFGBoundary.class);

	public AFGBoundary() {
		className = Constants.URL_AFG;
	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("{mainframe}/{devname}/info")
	public Response info(@Context UriInfo uriInfo,
			@PathParam("mainframe") String mainframe,
			@PathParam("devname") String devname) {
		logger.debug("Incoming URI : {}", uriInfo.getPath());
		logger.debug("Mainframe: {}", mainframe);
		logger.debug("Device name: {}", devname);
		return Response.ok(getClassName()).build();
	}

	@POST
	@Path("{mainframe}/{devname}/idn")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces(MediaType.APPLICATION_JSON)
	public Response idn(@Context UriInfo uriInfo,
			@PathParam("mainframe") String mainframe,
			@PathParam("devname") String devname) {
		logger.debug("Incoming URI : {}", uriInfo.getPath());
		logger.debug("Mainframe: {}", mainframe);
		logger.debug("Device name: {}", devname);

		try {
			connManager = ConnectionManager.getInstance(this.getClass(),
					mainframe, devname);
		} catch (Exception e) {
			logger.error(MSG_NO_WRAPPER);
			return Response.status(Status.NOT_FOUND).build();
		}

		String answer;
		try {
			answer = connManager
					.getConnector(this.getClass(), mainframe, devname)
					.send_and_receive(connManager.getLink(this.getClass(),
							mainframe, devname), "*IDN?");
		} catch (Exception e) {
			logger.error("Error in send_and_receive().");
			return Response.status(Status.NOT_FOUND).build();
		}
		System.out.println(answer);

		return Response.ok(answer).build();
	}

	/**
	 * Initialize device.
	 * 
	 * @param uriInfo
	 *            Injected uriInfo (injected by HK2/REST)
	 * @param mainframe
	 *            Mainframe to use. Comes from vxiserver.properties, e.g. "mfb".
	 * @param devname
	 *            Device to use.
	 * @return
	 */
	@POST
	@Path("{mainframe}/{devname}/initialize")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces(MediaType.APPLICATION_JSON)
	public Response initialize(@Context UriInfo uriInfo,
			@PathParam("mainframe") String mainframe,
			@PathParam("devname") String devname) {
		logger.debug("Incoming URI : {}", uriInfo.getPath());
		logger.debug("Mainframe: {}", mainframe);
		logger.debug("Device name: {}", devname);

		try {
			connManager = ConnectionManager.getInstance(this.getClass(),
					mainframe, devname);
		} catch (Exception e) {
			logger.error(MSG_NO_WRAPPER);
			return Response.status(Status.NOT_FOUND).build();
		}

		try {
			getDevice(mainframe, devname).initialize();
		} catch (Exception e) {
			logger.error("Error accessing device.");
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
		return Response.ok("done").build();
	}
	
	/**
	 * getConfiguration.
	 * 
	 * @param uriInfo
	 *            Injected uriInfo (injected by HK2/REST)
	 * @param mainframe
	 *            Mainframe to use. Comes from vxiserver.properties, e.g. "mfb".
	 * @param devname
	 *            Device to use.
	 * @return
	 */
	@POST
	@Path("{mainframe}/{devname}/getConfiguration")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces(MediaType.APPLICATION_JSON)
	public Response getConfiguration(@Context UriInfo uriInfo,
			@PathParam("mainframe") String mainframe,
			@PathParam("devname") String devname) {
		logger.debug("Incoming URI : {}", uriInfo.getPath());
		logger.debug("Mainframe: {}", mainframe);
		logger.debug("Device name: {}", devname);

		try {
			connManager = ConnectionManager.getInstance(this.getClass(),
					mainframe, devname);
		} catch (Exception e) {
			logger.error(MSG_NO_WRAPPER);
			return Response.status(Status.NOT_FOUND).build();
		}
		String answer = "";
		try {
			answer = getDevice(mainframe, devname).getConfig();
		} catch (Exception e) {
			logger.error("Error accessing device.");
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
		return Response.ok(answer).build();
	}


	/**
	 * Set amplitude.
	 * 
	 * @param uriInfo
	 *            Injected uriInfo (injected by HK2/REST)
	 * @param mainframe
	 *            Mainframe to use. Comes from vxiserver.properties, e.g. "mfb".
	 * @param devname
	 *            Device to use.
	 * @param amplitude
	 *            Amplitude of waveform. Example '5.0' for 5,0 Volts.
	 * @return
	 */
	@POST
	@Path("{mainframe}/{devname}/setAmplitude/{amplitude}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces(MediaType.APPLICATION_JSON)
	public Response setAmplitude(@Context UriInfo uriInfo,
			@PathParam("mainframe") String mainframe,
			@PathParam("devname") String devname,
			@PathParam("amplitude") Double amplitude) {
		logger.debug("Incoming URI : {}", uriInfo.getPath());
		logger.debug("Mainframe: {}", mainframe);
		logger.debug("Device name: {}", devname);
		logger.debug("Amplitude: {}", amplitude);

		try {
			connManager = ConnectionManager.getInstance(this.getClass(),
					mainframe, devname);
		} catch (Exception e) {
			logger.error(MSG_NO_WRAPPER);
			return Response.status(Status.NOT_FOUND).build();
		}

		try {
			getDevice(mainframe, devname).stop();
			getDevice(mainframe, devname).setAmplitude(amplitude);
			getDevice(mainframe, devname).start();
		} catch (Exception e) {
			logger.error("Error accessing device.");
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
		return Response.ok("done").build();
	}

	/**
	 * Set frequency.
	 * 
	 * @param uriInfo
	 *            Injected uriInfo (injected by HK2/REST)
	 * @param mainframe
	 *            Mainframe to use. Comes from vxiserver.properties, e.g. "mfb".
	 * @param devname
	 *            Device to use.
	 * @param frequency
	 *            Frequency of waveform. Example '5e6' for 5MHz and '44000' for
	 *            44KHz.
	 * @return
	 */
	@POST
	@Path("{mainframe}/{devname}/setFrequency/{frequency}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces(MediaType.APPLICATION_JSON)
	public Response setFrequency(@Context UriInfo uriInfo,
			@PathParam("mainframe") String mainframe,
			@PathParam("devname") String devname,
			@PathParam("frequency") Double frequency) {
		logger.debug("Incoming URI : {}", uriInfo.getPath());
		logger.debug("Mainframe: {}", mainframe);
		logger.debug("Device name: {}", devname);
		logger.debug("Frequency: {}", frequency);

		try {
			connManager = ConnectionManager.getInstance(this.getClass(),
					mainframe, devname);
		} catch (Exception e) {
			logger.error(MSG_NO_WRAPPER);
			return Response.status(Status.NOT_FOUND).build();
		}

		try {
			getDevice(mainframe, devname).stop();
			getDevice(mainframe, devname).setFrequency(frequency);
			getDevice(mainframe, devname).start();
		} catch (Exception e) {
			logger.error("Error accessing device.");
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
		return Response.ok("done").build();
	}

	/**
	 * Set shape of StandardWaveForm type.
	 * 
	 * @param uriInfo
	 *            Injected uriInfo (injected by HK2/REST)
	 * @param mainframe
	 *            Mainframe to use. Comes from vxiserver.properties, e.g. "mfb".
	 * @param devname
	 *            Device to use.
	 * @param waveform
	 *            Shape of waveform. Example "ramp" or "sin". See
	 *            {HP1340.StandardWaveForm}.
	 * @return
	 */
	@POST
	@Path("{mainframe}/{devname}/setShape/standard/{waveform}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces(MediaType.APPLICATION_JSON)
	public Response setStandardShape(@Context UriInfo uriInfo,
			@PathParam("mainframe") String mainframe,
			@PathParam("devname") String devname,
			@PathParam("waveform") String waveform) {
		logger.debug("Incoming URI : {}", uriInfo.getPath());
		logger.debug("Mainframe: {}", mainframe);
		logger.debug("Device name: {}", devname);
		logger.debug("waveform: {}", waveform);

		try {
			connManager = ConnectionManager.getInstance(this.getClass(),
					mainframe, devname);
		} catch (Exception e) {
			logger.error(MSG_NO_WRAPPER);
			return Response.status(Status.NOT_FOUND).build();
		}

		StandardWaveForm wv;
		// @formatter:off
		switch (waveform.toLowerCase()) {
		case "dc": wv = StandardWaveForm.DC; break;
		case "ramp": wv = StandardWaveForm.RAMP; break;
		case "sine": wv = StandardWaveForm.SINE; break;
		case "square": wv = StandardWaveForm.SQUARE; break;
		case "triangle": wv = StandardWaveForm.TRIANGLE; break;
		case "usera": wv = StandardWaveForm.USERA; break;
		case "userb": wv = StandardWaveForm.USERB; break;
		case "userc": wv = StandardWaveForm.USERC; break;
		case "userd": wv = StandardWaveForm.USERD; break;
		default: wv = StandardWaveForm.SQUARE; break;
		}
		// @formatter:on
		try {
			getDevice(mainframe, devname).stop();
			getDevice(mainframe, devname).setShape(wv);
			getDevice(mainframe, devname).start();
		} catch (Exception e) {
			logger.error("Error accessing device.");
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
		return Response.ok(wv.getValue()).build();
	}

	/**
	 * Set shape of BuiltinWaveForm type.
	 * 
	 * @param uriInfo
	 *            Injected uriInfo (injected by HK2/REST)
	 * @param mainframe
	 *            Mainframe to use. Comes from vxiserver.properties, e.g. "mfb".
	 * @param devname
	 *            Device to use.
	 * @param waveform
	 *            Shape of waveform. Example "sine" or "haversine". See
	 *            {HP1340.BuiltinWaveForm}.
	 * @param segment
	 *            Target segment ('A'..'D'). {HP1340.StandardWaveForm}.
	 * @return
	 */
	@POST
	@Path("{mainframe}/{devname}/setShape/builtin/{waveform}/{segment}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces(MediaType.APPLICATION_JSON)
	public Response setBuiltinShape(@Context UriInfo uriInfo,
			@PathParam("mainframe") String mainframe,
			@PathParam("devname") String devname,
			@PathParam("waveform") String waveform,
			@PathParam("segment") char segment) {
		logger.debug("Incoming URI : {}", uriInfo.getPath());
		logger.debug("Mainframe: {}", mainframe);
		logger.debug("Device name: {}", devname);
		logger.debug("segment: {}", segment);

		try {
			connManager = ConnectionManager.getInstance(this.getClass(),
					mainframe, devname);
		} catch (Exception e) {
			logger.error(MSG_NO_WRAPPER);
			return Response.status(Status.NOT_FOUND).build();
		}

		BuiltinWaveForm wv;
		// @formatter:off
		switch (waveform.toLowerCase()) {
		case "harmonic": wv = BuiltinWaveForm.Harmonic_Chord_3rd_4th_5th; break;
		case "haversine": wv = BuiltinWaveForm.Haversine; break;
		case "ramp_falling": wv = BuiltinWaveForm.Ramp_Falling; break;
		case "ramp_falling_first_20": wv = BuiltinWaveForm.Ramp_Falling_First_20; break;
		case "ramp_rising": wv = BuiltinWaveForm.Ramp_Rising; break;
		case "ramp_rising_first_20": wv = BuiltinWaveForm.Ramp_Rising_First_20; break;
		case "sine": wv = BuiltinWaveForm.Sine; break;
		case "sine_linear_rising_8_cycles": wv = BuiltinWaveForm.Sine_Linear_Rising_8_cycles; break;
		case "sine_positive_half_cycle": wv = BuiltinWaveForm.Sine_Positive_Half_Cycle; break;
		case "sinx_per_x": wv = BuiltinWaveForm.Sinx_per_x; break;
		case "square": wv = BuiltinWaveForm.Square; break;
		case "square_first_10": wv = BuiltinWaveForm.Square_First_10; break;
		case "square_first_4": wv = BuiltinWaveForm.Square_First_4; break;
		case "triangle": wv = BuiltinWaveForm.Triangle; break;
		case "white_noise": wv = BuiltinWaveForm.White_Noise; break;
		case "white_noise_modulated": wv = BuiltinWaveForm.White_Noise_Modulated; break;
		default: wv = BuiltinWaveForm.Harmonic_Chord_3rd_4th_5th; break;
		}
		// @formatter:on
		try {
			getDevice(mainframe, devname).stop();
			getDevice(mainframe, devname).setShape(wv, segment);
			getDevice(mainframe, devname).start();
		} catch (Exception e) {
			logger.error("Error accessing device.");
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
		return Response.ok(wv.getValue()).build();
	}

	/**
	 * Set sweep.
	 * 
	 * @param uriInfo
	 *            Injected uriInfo (injected by HK2/REST)
	 * @param mainframe
	 *            Mainframe to use. Comes from vxiserver.properties, e.g. "mfb".
	 * @param devname
	 *            Device to use.
	 * @param start
	 *            Start frequency.
	 * @param start
	 *            Stop frequency.
	 * @param points
	 *            number of points in sweep.
	 * @param duration
	 *            sweep duration in seconds.
	 * @param amplitude
	 *            sweep amplitude in volts.
	 * @param waveform
	 *            Shape of waveform. Example "sine" or "haversine". See
	 *            {HP1340.BuiltinWaveForm}.
	 * @return
	 */
	@POST
	@Path("{mainframe}/{devname}/setSweep/{start}/{stop}/{points}/{duration}/{amplitude}/{waveform}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces(MediaType.APPLICATION_JSON)
	public Response setSweep(@Context UriInfo uriInfo,
			@PathParam("mainframe") String mainframe,
			@PathParam("devname") String devname,
			@PathParam("start") double startFrequency,
			@PathParam("stop") double stopFrequency,
			@PathParam("points") int points,
			@PathParam("duration") double duration,
			@PathParam("amplitude") double amplitude,
			@PathParam("waveform") String waveform) {
		logger.debug("Incoming URI : {}", uriInfo.getPath());
		logger.debug("Mainframe: {}", mainframe);
		logger.debug("Device name: {}", devname);
		logger.debug("startFrequency: {}", startFrequency);
		logger.debug("stopFrequency: {}", stopFrequency);
		logger.debug("Points: {}", points);
		logger.debug("Duration: {}", duration);
		logger.debug("Amplitude: {}", amplitude);
		logger.debug("waveform: {}", waveform);

		try {
			connManager = ConnectionManager.getInstance(this.getClass(),
					mainframe, devname);
		} catch (Exception e) {
			logger.error(MSG_NO_WRAPPER);
			return Response.status(Status.NOT_FOUND).build();
		}

		StandardWaveForm wv;
		// @formatter:off
		switch (waveform.toLowerCase()) {
		case "dc": wv = StandardWaveForm.DC; break;
		case "ramp": wv = StandardWaveForm.RAMP; break;
		case "sine": wv = StandardWaveForm.SINE; break;
		case "square": wv = StandardWaveForm.SQUARE; break;
		case "triangle": wv = StandardWaveForm.TRIANGLE; break;
		case "usera": wv = StandardWaveForm.USERA; break;
		case "userb": wv = StandardWaveForm.USERB; break;
		case "userc": wv = StandardWaveForm.USERC; break;
		case "userd": wv = StandardWaveForm.USERD; break;
		default: wv = StandardWaveForm.SQUARE; break;
		}
		// @formatter:on
		try {
			getDevice(mainframe, devname).stop();
			getDevice(mainframe, devname).setSweep(startFrequency,
					stopFrequency, points, duration, amplitude, wv);
			getDevice(mainframe, devname).start();
		} catch (Exception e) {
			logger.error("Error accessing device.");
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
		return Response.ok(wv.getValue()).build();
	}

	/**
	 * Set sweep.
	 * 
	 * @param uriInfo
	 *            Injected uriInfo (injected by HK2/REST)
	 * @param mainframe
	 *            Mainframe to use. Comes from vxiserver.properties, e.g. "mfb".
	 * @param devname
	 *            Device to use.
	 * @param start
	 *            Start frequency.
	 * @param start
	 *            Stop frequency.
	 * @param points
	 *            number of points in sweep.
	 * @param duration
	 *            sweep duration in seconds.
	 * @param amplitude
	 *            sweep amplitude in volts.
	 * @param waveform
	 *            Shape of waveform. Example "sine" or "haversine". See
	 *            {HP1340.BuiltinWaveForm}.
	 * @return
	 */
	@POST
	@Path("{mainframe}/{devname}/setMarker/{source}/{polarity}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces(MediaType.APPLICATION_JSON)
	public Response setMarker(@Context UriInfo uriInfo,
			@PathParam("mainframe") String mainframe,
			@PathParam("devname") String devname,
			@PathParam("source") String source,
			@PathParam("polarity") String polarity) {
		logger.debug("Incoming URI : {}", uriInfo.getPath());
		logger.debug("Mainframe: {}", mainframe);
		logger.debug("Device name: {}", devname);
		logger.debug("source: {}", source);
		logger.debug("polarity: {}", polarity);

		try {
			connManager = ConnectionManager.getInstance(this.getClass(),
					mainframe, devname);
		} catch (Exception e) {
			logger.error(MSG_NO_WRAPPER);
			return Response.status(Status.NOT_FOUND).build();
		}

		MarkerFeedType mf;
		// @formatter:off
		switch (source.toLowerCase()) {
		case "outp_zero": mf = MarkerFeedType.OUTPUT_ZERO; break;
		case "segm": mf = MarkerFeedType.SEGMENT; break;
		case "sour_rosc": mf = MarkerFeedType.SOURCE_ROSC; break;
		case "sour_sweep": mf = MarkerFeedType.SOURCE_SWEEP; break;
		default: mf = MarkerFeedType.OUTPUT_ZERO; break;
		}
		PolarityType pol;
		switch (polarity.toLowerCase()) {
		case "norm": pol = PolarityType.NORM; break;
		case "inv": pol = PolarityType.INV; break;
		default: pol = PolarityType.NORM; break;		
		}
		// @formatter:on
		try {
			getDevice(mainframe, devname).stop();
			getDevice(mainframe, devname).setMarker(mf, pol);
			getDevice(mainframe, devname).start();
		} catch (Exception e) {
			logger.error("Error accessing device.");
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
		return Response.ok(mf.getValue()).build();
	}

}