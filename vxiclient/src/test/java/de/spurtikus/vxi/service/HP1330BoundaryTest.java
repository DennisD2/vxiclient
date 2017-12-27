package de.spurtikus.vxi.service;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.spurtikus.devices.hp.HP1330;
import de.spurtikus.devices.hp.HP1330.PortDescription;
import de.spurtikus.vxi.Constants;

@RunWith(Arquillian.class)
public class HP1330BoundaryTest {

	public final String BASE_URI = Constants.SERVICE_ROOT;
	public final String DEVICECLASS = Constants.URL_DIGITALIO;
	public final String MAINFRAME = "mfb";
	public final String DEVICENAME = "hp1330";
	public final String URI = BASE_URI + DEVICECLASS + "/" + MAINFRAME + "/" + DEVICENAME;

	@Deployment
	public static WebArchive createDeployment() {
		File[] lib = Maven.resolver()
				.resolve("de.spurtikus:vxiclient:0.0.1-SNAPSHOT")
				.withTransitivity().as(File.class);

		WebArchive jar = ShrinkWrap.create(WebArchive.class, "vxi.war")
				.addClass(SystemBoundary.class).addClass(HP1330Boundary.class)
				.addAsManifestResource("arquillian.xml").addAsLibraries(lib)
				.addAsManifestResource("META-INF/context.xml", "context.xml")
				.setWebXML("web.xml");

		// System.out.println(jar.toString(true));
		return jar;
	}

	@Ignore
	@Test
	@RunAsClient
	public void info(@ArquillianResource URL contextPath) {
		String uri = URI + "/info";
		Client client = ClientBuilder.newClient();
		System.out.println(contextPath + uri);
		final Response response = client.target(contextPath + uri)
				.request(MediaType.TEXT_PLAIN).get();
		assertTrue(response.getStatus()<400);
		String res = response.readEntity(String.class);
		System.out.println(uri + " -> " + res);
	}

	@Ignore
	@Test
	@RunAsClient
	public void idn(@ArquillianResource URL contextPath) {
		String uri = URI + "/idn";
		Client client = ClientBuilder.newClient();
		System.out.println(contextPath + uri);
		final Response response = client.target(contextPath + uri)
				.request(MediaType.APPLICATION_JSON).post(null);
		assertTrue(response.getStatus()<400);
		String res = response.readEntity(String.class);
		System.out.println(uri + " -> " + res);
	}

	@Ignore
	@Test
	@RunAsClient
	public void setBit(@ArquillianResource URL contextPath) {
		String value = "true";
		String uri = URI + "/setBit/" + value;
		PortDescription port = new PortDescription(HP1330.Port.DATA0, HP1330.Bit.BIT0);
		Client client = ClientBuilder.newClient();
		System.out.println("Call: " + contextPath + uri);
		Response response = client.target(contextPath + uri)
				.request(MediaType.APPLICATION_JSON)
				.post(Entity.json(port));
		String res = response.readEntity(String.class);
		System.out.println("Call result: " + res);
		assertTrue(response.getStatus()<400);
		
		value = "false";
		uri = URI + "/setBit/" + value;
		System.out.println("Call: " + contextPath + uri);
		response = client.target(contextPath + uri)
				.request(MediaType.APPLICATION_JSON)
				.post(Entity.json(port));
		res = response.readEntity(String.class);
		System.out.println("Call result: " + res);
		assertTrue(response.getStatus()<400);
	}

	// @Ignore
	@Test
	@RunAsClient
	public void getBit(@ArquillianResource URL contextPath) {
		String uri = URI + "/getBit" ;
		PortDescription port = new PortDescription(HP1330.Port.DATA0, HP1330.Bit.BIT0);
		Client client = ClientBuilder.newClient();
		System.out.println("Call: " + contextPath + uri);
		Response response = client.target(contextPath + uri)
				.request(MediaType.APPLICATION_JSON)
				.post(Entity.json(port));
		String res = response.readEntity(String.class);
		System.out.println("Call result: " + res);
		assertTrue(response.getStatus()<400);
		
		String value = "false";
		uri = URI + "/setBit/" + value;
		System.out.println("Call: " + contextPath + uri);
		response = client.target(contextPath + uri)
				.request(MediaType.APPLICATION_JSON)
				.post(Entity.json(port));
		res = response.readEntity(String.class);
		System.out.println("Call result: " + res);
		assertTrue(response.getStatus()<400);

		uri = URI + "/getBit";
		System.out.println("Call: " + contextPath + uri);
		response = client.target(contextPath + uri)
				.request(MediaType.APPLICATION_JSON)
				.post(Entity.json(port));
		res = response.readEntity(String.class);
		System.out.println("Call result: " + res);
		assertTrue(response.getStatus()<400);
	}

}
