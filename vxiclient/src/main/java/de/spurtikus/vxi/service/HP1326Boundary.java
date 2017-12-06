package de.spurtikus.vxi.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import de.spurtikus.devices.hp.HP1326;

/**
 * Boundary for HP1326/1411 voltmeter. See class {HP1326}.
 * 
 * @author dennis
 *
 */
@Path("/api/hp1326")
public class HP1326Boundary extends AbstractBoundary {
	public final static String className = "HP1326Boundary";

	private Logger logger = LoggerFactory.getLogger(HP1326Boundary.class);

	protected HP1326 getDevice(String mainframe, String devname) {
		return (HP1326) connManager.getDevice(this.getClass(), mainframe, devname);
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
		return Response.ok("HP1326").build();
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
			connManager = ConnectionManager.getInstance(this.getClass(), mainframe, devname);
		} catch (Exception e) {
			logger.error(
					"Cannot get wrapper instance. This is usually an initialization problem.");
			return Response.status(Status.NOT_FOUND).build();
		}

		String answer;
		try {
			answer = connManager.getConnector(this.getClass(), mainframe, devname)
					.send_and_receive(connManager.getLink(this.getClass(), mainframe, devname),
							"*IDN?");
		} catch (Exception e) {
			logger.error("Error in send_and_receive().");
			return Response.status(Status.NOT_FOUND).build();
		}
		System.out.println(answer);

		return Response.ok("{\"*idn?\":\"" + answer + "\"}").build();
	}

	/**
	 * Reads multiple channels. Channel list is assumed to be coming in as POST data.
	 * 
	 * @param uriInfo
	 *            Injected uriInfo (injected by HK2/REST)
	 * @param mainframe
	 *            Mainframe to use. Comes from vxiserver.properties, e.g. "mfb".
	 * @param devname
	 *            Device to use.
	 * @param range
	 *            Range to use during measurement. E.g. "7.2.7". See {HP1326}.
	 * @param channels
	 *            List of channels to measure. E.g. [101,102]. See {HP1326}.
	 *            
	 * @return
	 */
	@POST
	@Path("{mainframe}/{devname}/read/{range}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces(MediaType.APPLICATION_JSON)
	public Response readChannels(@Context UriInfo uriInfo,
			@PathParam("mainframe") String mainframe,
			@PathParam("devname") String devname,
			@PathParam("range") Double range, 
			List<Integer> channels) {
		logger.debug("Incoming URI : {}", uriInfo.getPath());
		logger.debug("Mainframe: {}", mainframe);
		logger.debug("Device name: {}", devname);
		logger.debug("Range: {}", range);
		logger.debug("Channels: {}", channels);

		logger.info("Using channels require DVM+Switch configuration!");

		try {
			connManager = ConnectionManager.getInstance(this.getClass(), mainframe, devname);
		} catch (Exception e) {
			logger.error(
					"Cannot get wrapper instance. This is usually an initialization problem.");
			return Response.status(Status.NOT_FOUND).build();
		}

		Map<Integer, Double> m = new HashMap<>();
		try {
			getDevice(mainframe, devname).initializeVoltageMeasurement(range, channels);
			m = getDevice(mainframe, devname).measureChannels(channels);
		} catch (Exception e) {
			logger.error("Error accessing device.");
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
		for (int channel : m.keySet()) {
			logger.debug("{} : {}", channel, m.get(channel));
		}
		return Response.ok(m).build();
	}

}