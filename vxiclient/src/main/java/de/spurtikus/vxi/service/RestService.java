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
import javax.ws.rs.core.UriInfo;

@Path("/msys/{device}/{op}")
public class RestService {

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
	@Produces(MediaType.TEXT_PLAIN)
	public String sayPlainTextHello() {
		System.out.println("HELLO");
		return "Hello Jersey";
	}

	@POST
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces(MediaType.APPLICATION_JSON)
	public Response sayJsonHello(@Context UriInfo uriInfo, @PathParam("device") String device,
			@PathParam("op") String op, Command command) {
		System.out.println(uriInfo.getPath());
		System.out.println("Dev: " + device);
		System.out.println("Op: " + op);
		if (command != null) {
			System.out.println("Command: " + command.getCmd());
			System.out.println("Value: " + command.getValue());
		} else {
			System.out.println("Cmd is null");
		}
		Entity e = new Entity();
		e.setName(command.getCmd());

		return Response.ok(e).build();
	}
}
