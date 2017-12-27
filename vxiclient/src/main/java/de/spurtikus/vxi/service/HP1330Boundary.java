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

import de.spurtikus.devices.hp.HP1330;
import de.spurtikus.devices.hp.HP1330.PortDescription;
import de.spurtikus.vxi.Constants;

/**
 * Boundary for HP E1330 Digital I/O control. See class {HP1330}.
 * 
 * @author dennis
 *
 */
@Path(Constants.URL_DIGITALIO)
public class HP1330Boundary extends AbstractBoundary<HP1330> {
	public final static String className = "HP1330Boundary";

	private Logger logger = LoggerFactory.getLogger(HP1330Boundary.class);

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("{mainframe}/{devname}/info")
	public Response info(@Context UriInfo uriInfo,
			@PathParam("mainframe") String mainframe,
			@PathParam("devname") String devname) {
		logger.debug("Incoming URI : {}", uriInfo.getPath());
		logger.debug("Mainframe: {}", mainframe);
		logger.debug("Device name: {}", devname);
		return Response.ok("HP1330").build();
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
	 * Set a bit in a port.
	 * 
	 * @param uriInfo
	 * @param mainframe
	 * @param devname
	 * @param port
	 *            port to use
	 * @param value
	 *            bit value
	 * @return
	 */
	@POST
	@Path("{mainframe}/{devname}/setBit/{value}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces(MediaType.APPLICATION_JSON)
	public Response setBit(@Context UriInfo uriInfo,
			@PathParam("mainframe") String mainframe,
			@PathParam("devname") String devname, 
			@PathParam("value") Boolean value,
			PortDescription port) {
		logger.debug("Incoming URI : {}", uriInfo.getPath());
		logger.debug("Mainframe: {}", mainframe);
		logger.debug("Device name: {}", devname);
		logger.debug("Value: {}", value);
		logger.debug("Port: {}", port);

		try {
			connManager = ConnectionManager.getInstance(this.getClass(),
					mainframe, devname);
		} catch (Exception e) {
			logger.error(
					"Cannot get wrapper instance. This is usually an initialization problem.");
			return Response.status(Status.NOT_FOUND).build();
		}

		boolean b;
		try {
			b = getDevice(mainframe, devname).setBit(port, value);
		} catch (Exception e) {
			logger.error("Error accessing device.");
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
		return Response.ok(b).build();
	}

	
	/**
	 * Get a bit in a port.
	 * 
	 * @param uriInfo
	 * @param mainframe
	 * @param devname
	 * @param port
	 *            port to use
	 * @return
	 */
	@POST
	@Path("{mainframe}/{devname}/getBit")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces(MediaType.APPLICATION_JSON)
	public Response setBit(@Context UriInfo uriInfo,
			@PathParam("mainframe") String mainframe,
			@PathParam("devname") String devname, 
			PortDescription port) {
		logger.debug("Incoming URI : {}", uriInfo.getPath());
		logger.debug("Mainframe: {}", mainframe);
		logger.debug("Device name: {}", devname);
		logger.debug("Port: {}", port);

		try {
			connManager = ConnectionManager.getInstance(this.getClass(),
					mainframe, devname);
		} catch (Exception e) {
			logger.error(
					"Cannot get wrapper instance. This is usually an initialization problem.");
			return Response.status(Status.NOT_FOUND).build();
		}

		boolean b;
		try {
			b = getDevice(mainframe, devname).getBit(port);
		} catch (Exception e) {
			logger.error("Error accessing device.");
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
		return Response.ok(b).build();
	}

}