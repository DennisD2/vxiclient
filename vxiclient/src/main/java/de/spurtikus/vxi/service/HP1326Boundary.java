package de.spurtikus.vxi.service;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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
public class HP1326Boundary extends AbstractBoundary<HP1326> {
	public final static String className = "HP1326Boundary";

	private Logger logger = LoggerFactory.getLogger(HP1326Boundary.class);

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

	double old101 = 3;
	double old100 = -2;
	@POST
	@Path("{mainframe}/{devname}/readFake/{range}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces(MediaType.APPLICATION_JSON)
	public Response readChannelsFake(@Context UriInfo uriInfo,
			@PathParam("mainframe") String mainframe,
			@PathParam("devname") String devname,
			@PathParam("range") Double range, 
			List<Integer> channels) {
		logger.debug("Incoming URI : {}", uriInfo.getPath());
		logger.debug("Mainframe: {}", mainframe);
		logger.debug("Device name: {}", devname);
		logger.debug("Range: {}", range);
		logger.debug("Channels: {}", channels);

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		double delta100 = Math.random();
		double delta101 = Math.random();
		
		double n100 = old100;
		double n101 = old101;
		if (delta100<=0.5) {
			n100 = old100 - delta100/5;
		} else {
			n100 = old100 + delta100/5;	
		}
		if (delta101<=0.5) {
			n101 = old101 - delta101/5;
		} else {
			n101 = old101 + delta101/5;	
		}
		old100 = n100;
		old101 = n101;
		NumberFormat formatter = new DecimalFormat("#0.0000000", DecimalFormatSymbols.getInstance(Locale.US) );  
		String n = formatter.format(n100);
		String m = formatter.format(n101);
		// {"100":0.2712517,"101":-0.2288322}
		String answer = "{\"100\":"+n+",\"101\":"+m+"}";
		logger.debug(answer);
		return Response.ok(answer).build();
	}

}