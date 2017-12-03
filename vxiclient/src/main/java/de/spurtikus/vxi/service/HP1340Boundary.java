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

	HP1340Wrapper wrapper;

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/info")
	public Response info() {
		return Response.ok("HP1340").build();
	}

	@POST
	@Path("/idn")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces(MediaType.APPLICATION_JSON)
	public Response idn(@Context UriInfo uriInfo) {
		System.out.println(uriInfo.getPath());

		try {
			wrapper = HP1340Wrapper.getInstance();
		} catch (Exception e) {
			logger.error(
					"Cannot get wrapper instance. This is usually an initialization problem.");
			return Response.status(Status.NOT_FOUND).build();
		}

		String answer;
		try {
			answer = wrapper.getConnector().send_and_receive(wrapper.getLink(),
					"*IDN?");
		} catch (Exception e) {
			logger.error("Error in send_and_receive().");
			return Response.status(Status.NOT_FOUND).build();
		}
		System.out.println(answer);

		return Response.ok("{\"*idn?\":\"" + answer + "\"}").build();
	}

	/**
	 * Set shape of StandardWaveForm type. 
	 * @param uriInfo injected uriInfo (injected by HK2/REST)
	 * @param amplitude amplitude of waveform. Example '5.0' for 5,0 Volts.
	 * @param frequency frequency of waveform. Example '5e6' for 5MHz and '44000' for 44KHz.
	 * @return
	 */
	@POST
	@Path("/shape/{amplitude}/{frequency}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces(MediaType.APPLICATION_JSON)
	public Response shape(@Context UriInfo uriInfo,
			@PathParam("amplitude") String amplitude,
			@PathParam("frequency") String frequency) {
		logger.debug("Incoming URI : {}", uriInfo.getPath());
		logger.debug("Amplitude: {}", amplitude);
		logger.debug("Frequency: {}", frequency);

		try {
			wrapper = HP1340Wrapper.getInstance();
		} catch (Exception e) {
			logger.error(
					"Cannot get wrapper instance. This is usually an initialization problem.");
			return Response.status(Status.NOT_FOUND).build();
		}

		Double a = armedDouble(amplitude);
		Double f = armedDouble(frequency);
		try {
			wrapper.getDevice().stop();
			wrapper.getDevice().setAmplitude(a);
			wrapper.getDevice().setFrequency(f);
			wrapper.getDevice().setShape(HP1340.StandardWaveForm.RAMP);

			wrapper.getDevice().start();
		} catch (Exception e) {
			logger.error("Error accessing device.");
			return Response.status(Status.NOT_FOUND).build();
		}
		return Response.ok("ok").build();
	}

	private Double armedDouble(String s) {
		Double d = Double.parseDouble(s);
		return d;
	}

}