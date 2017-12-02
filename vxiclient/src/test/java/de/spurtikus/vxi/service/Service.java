package de.spurtikus.vxi.service;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import de.spurtikus.vxi.beans.Greeter;


@Path( "/api" )
public class Service {
	//@Inject
	Greeter greeter = new Greeter();

	@GET 
	@Produces( MediaType.TEXT_PLAIN )
	@Path("/hehe")
	// @Produces(MediaType.APPLICATION_JSON)
	// @Consumes(MediaType.APPLICATION_JSON)
	public Response hehe() {
		return Response.ok("hehe " + greeter.createGreeting("blub")).build();
	}
}