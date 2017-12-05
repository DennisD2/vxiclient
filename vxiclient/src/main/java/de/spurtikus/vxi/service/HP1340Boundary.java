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
import de.spurtikus.devices.hp.HP1340.StandardWaveForm;

/**
 * Boundary for HP1340 AFG. See class {HP1340}.
 * 
 * @author dennis
 *
 */
@Path("/api/hp1340")
public class HP1340Boundary extends AbstractBoundary {
	public final static String className = "HP1340Boundary";

	private Logger logger = LoggerFactory.getLogger(HP1340Boundary.class);

	protected HP1340 getDevice(String mainframe, String devname) {
		return (HP1340) connManager.getDevice(this.getClass(), mainframe,
				devname);
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
		return Response.ok("HP1340").build();
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
			logger.error(
					"Cannot get wrapper instance. This is usually an initialization problem.");
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

		return Response.ok("{\"*idn?\":\"" + answer + "\"}").build();
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
	 * @param amplitude
	 *            Amplitude of waveform. Example '5.0' for 5,0 Volts.
	 * @param frequency
	 *            Frequency of waveform. Example '5e6' for 5MHz and '44000' for
	 *            44KHz.
	 * @return
	 */
	@POST
	@Path("{mainframe}/{devname}/shape/{waveform}/{amplitude}/{frequency}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces(MediaType.APPLICATION_JSON)
	public Response shape(@Context UriInfo uriInfo,
			@PathParam("mainframe") String mainframe,
			@PathParam("devname") String devname,
			@PathParam("waveform") String waveform,
			@PathParam("amplitude") Double amplitude,
			@PathParam("frequency") Double frequency) {
		logger.debug("Incoming URI : {}", uriInfo.getPath());
		logger.debug("Mainframe: {}", mainframe);
		logger.debug("Device name: {}", devname);
		logger.debug("Amplitude: {}", amplitude);
		logger.debug("Frequency: {}", frequency);

		try {
			connManager = ConnectionManager.getInstance(this.getClass(),
					mainframe, devname);
		} catch (Exception e) {
			logger.error(
					"Cannot get wrapper instance. This is usually an initialization problem.");
			return Response.status(Status.NOT_FOUND).build();
		}

		StandardWaveForm wv = Arrays.stream(StandardWaveForm.values())
				.filter(v -> v.getValue().toLowerCase().equals(waveform))
				.findAny().get();
		try {
			getDevice(mainframe, devname).stop();

			getDevice(mainframe, devname).setAmplitude(amplitude);
			getDevice(mainframe, devname).setFrequency(frequency);
			getDevice(mainframe, devname).setShape(wv);

			getDevice(mainframe, devname).start();
		} catch (Exception e) {
			logger.error("Error accessing device.");
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
		return Response.ok(wv.getValue()).build();
	}

}