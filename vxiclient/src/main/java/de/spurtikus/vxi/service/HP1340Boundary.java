package de.spurtikus.vxi.service;

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

/**
 * Boundary for HP1340.
 * 
 * @author dennis
 *
 */
@Path("/api/hp1340")
public class HP1340Boundary {
	Logger logger = LoggerFactory.getLogger(HP1340Boundary.class);

	ConnectionManager connManager;

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("{mainframe}/info")
	public Response info(@Context UriInfo uriInfo,
			@PathParam("mainframe") String mainframe) {
		logger.debug("Incoming URI : {}", uriInfo.getPath());
		logger.debug("Mainframe: {}", mainframe);
		return Response.ok("HP1340").build();
	}

	@POST
	@Path("{mainframe}/{devname}/idn}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces(MediaType.APPLICATION_JSON)
	public Response idn(@Context UriInfo uriInfo,
			@PathParam("mainframe") String mainframe,
			@PathParam("devname") String devname) {
		logger.debug("Incoming URI : {}", uriInfo.getPath());
		logger.debug("Mainframe: {}", mainframe);
		logger.debug("Device name: {}", devname);

		try {
			connManager = ConnectionManager.getInstance(mainframe, devname);
		} catch (Exception e) {
			logger.error(
					"Cannot get wrapper instance. This is usually an initialization problem.");
			return Response.status(Status.NOT_FOUND).build();
		}

		String answer;
		try {
			answer = connManager.getConnector(mainframe, devname).send_and_receive(
					connManager.getLink(mainframe, devname), "*IDN?");
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
	 *            injected uriInfo (injected by HK2/REST)
	 * @param amplitude
	 *            amplitude of waveform. Example '5.0' for 5,0 Volts.
	 * @param frequency
	 *            frequency of waveform. Example '5e6' for 5MHz and '44000' for
	 *            44KHz.
	 * @return
	 */
	@POST
	@Path("{mainframe}/{devname}/shape/{amplitude}/{frequency}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces(MediaType.APPLICATION_JSON)
	public Response shape(@Context UriInfo uriInfo,
			@PathParam("mainframe") String mainframe,
			@PathParam("devname") String devname,
			@PathParam("amplitude") String amplitude,
			@PathParam("frequency") String frequency) {
		logger.debug("Incoming URI : {}", uriInfo.getPath());
		logger.debug("Mainframe: {}", mainframe);
		logger.debug("Device name: {}", devname);
		logger.debug("Amplitude: {}", amplitude);
		logger.debug("Frequency: {}", frequency);

		try {
			connManager = ConnectionManager.getInstance(mainframe, devname);
		} catch (Exception e) {
			logger.error(
					"Cannot get wrapper instance. This is usually an initialization problem.");
			return Response.status(Status.NOT_FOUND).build();
		}

		Double a = armedDouble(amplitude);
		Double f = armedDouble(frequency);
		try {
			connManager.getDevice(mainframe, devname).stop();
			connManager.getDevice(mainframe, devname).setAmplitude(a);
			connManager.getDevice(mainframe, devname).setFrequency(f);
			connManager.getDevice(mainframe, devname)
					.setShape(HP1340.StandardWaveForm.RAMP);

			connManager.getDevice(mainframe, devname).start();
		} catch (Exception e) {
			logger.error("Error accessing device.");
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
		return Response.ok("ok").build();
	}

	private Double armedDouble(String s) {
		Double d = Double.parseDouble(s);
		return d;
	}

}