package de.spurtikus.vxi.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Boundary for device system, a partly virtual device keeping information about
 * the mainframes and the REST interface.
 * 
 * @author dennis
 *
 */
@Path("/api/system")
public class SystemBoundary {
	public final static String className = "SystemBoundary";

	private Logger logger = LoggerFactory.getLogger(SystemBoundary.class);

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/info")
	public Response info(@Context UriInfo uriInfo) {
		logger.debug("Incoming URI : {}", uriInfo.getPath());
		return Response.ok("VXI system REST API").build();
	}
}