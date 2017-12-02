package de.spurtikus.vxi.service;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import de.spurtikus.vxi.beans.Greeter;
import de.spurtikus.vxi.connectors.ConnectorConfig;


@Path( "/api" )
public class Service {
	//@Inject
	Greeter greeter = new Greeter();

	private class Entity {
		private String name = "a";
		private int age = 11;

		public Entity() {
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public int getAge() {
			return age;
		}

		public void setAge(int age) {
			this.age = age;
		}
	}

	@GET 
	@Produces( MediaType.TEXT_PLAIN )
	@Path("/info")
	public Response hehe() {
		return Response.ok(greeter.createGreeting("VXI system REST API")).build();
	}
	
	@POST
	@Path("/{device}/{op}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces(MediaType.APPLICATION_JSON)
	public Response sayJsonHello(@Context UriInfo uriInfo, @PathParam("device") String device,
			@PathParam("op") String op) throws Exception {
		System.out.println(uriInfo.getPath());
		System.out.println("Dev: " + device);
		System.out.println("Op: " + op);
		
		if (device.equals("sys") && op.equals("info")) {
			// Load configuration
			Configuration.load();
			// We assume usable config at some index
			List<ConnectorConfig> c = Configuration.getConfigs();
			return Response.ok(c).build();
		}
		
		Entity e = new Entity();
		e.setName(op);
		return Response.ok(e).build();
	}
}