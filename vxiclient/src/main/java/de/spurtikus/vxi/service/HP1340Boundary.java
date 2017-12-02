package de.spurtikus.vxi.service;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import de.spurtikus.vxi.connectors.ConnectorConfig;

/**
 * Boundary for device system, a partly virtual device keeping information about
 * the mainframe.
 * 
 * @author dennis
 *
 */
@Path("/api/hp1340")
public class HP1340Boundary {

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/info")
	public Response info() {
		return Response.ok("HP1340").build();
	}

	@POST
	@Path("/{op}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces(MediaType.APPLICATION_JSON)
	public Response doOperatkion(@Context UriInfo uriInfo,
			@PathParam("op") String op) throws Exception {
		System.out.println(uriInfo.getPath());
		System.out.println("Op: " + op);

		return Response.ok("{\"hehe\":\"uhuh\"}").build();
	}
}