package de.spurtikus.vxi.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URL;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.PackagingType;
import org.jboss.shrinkwrap.resolver.api.maven.coordinate.MavenCoordinates;
import org.jboss.shrinkwrap.resolver.api.maven.coordinate.MavenDependencies;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.spurtikus.devices.hp.DigitalIO;
import de.spurtikus.devices.hp.DigitalIO.PortDescription;
import de.spurtikus.vxi.Constants;

@RunWith(Arquillian.class)
public class DIOBoundaryTest extends BaseBoundaryTest {
	public final String DEVICECLASS = Constants.URL_DIGITALIO;
	public final String MAINFRAME = "mfb";
	public final String DEVICENAME = "hp1330";
	public final String URI = BASE_URI + "/" + DEVICECLASS + "/" + MAINFRAME + "/" + DEVICENAME;

	//@Ignore
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
		assertEquals("dio", res);
	}

	//@Ignore
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
		assertEquals("HEWLETT-PACKARD,E1330A,0,A.04.02", res);
	}

	//@Ignore
	@Test
	@RunAsClient
	public void setBit(@ArquillianResource URL contextPath) {
		String value = "true";
		String uri = URI + "/setBit/0/0/" + value;
		Client client = ClientBuilder.newClient();
		System.out.println("Call: " + contextPath + uri);
		Response response = client.target(contextPath + uri)
				.request(MediaType.APPLICATION_JSON)
				.post(null);
		String res = response.readEntity(String.class);
		System.out.println("Call result: " + res);
		assertTrue(response.getStatus()<400);
		
		value = "false";
		uri = URI + "/setBit/0/0/" + value;
		System.out.println("Call: " + contextPath + uri);
		response = client.target(contextPath + uri)
				.request(MediaType.APPLICATION_JSON)
				.post(null);
		res = response.readEntity(String.class);
		System.out.println("Call result: " + res);
		assertTrue(response.getStatus()<400);
	}

	//@Ignore
	@Test
	@RunAsClient
	public void getBit(@ArquillianResource URL contextPath) {
		String uri = URI + "/getBit/0/0/" ;
		PortDescription port = new PortDescription(DigitalIO.Port.DATA0, DigitalIO.Bit.BIT0);
		Client client = ClientBuilder.newClient();
		System.out.println("Call: " + contextPath + uri);
		Response response = client.target(contextPath + uri)
				.request(MediaType.APPLICATION_JSON)
				.post(null);
		String res = response.readEntity(String.class);
		System.out.println("Call result: " + res);
		assertTrue(response.getStatus()<400);
		
		String value = "false";
		uri = URI + "/setBit/0/0/" + value;
		System.out.println("Call: " + contextPath + uri);
		response = client.target(contextPath + uri)
				.request(MediaType.APPLICATION_JSON)
				.post(null);
		res = response.readEntity(String.class);
		System.out.println("Call result: " + res);
		assertTrue(response.getStatus()<400);

		uri = URI + "/getBit/0/0/";
		System.out.println("Call: " + contextPath + uri);
		response = client.target(contextPath + uri)
				.request(MediaType.APPLICATION_JSON)
				.post(null);
		res = response.readEntity(String.class);
		System.out.println("Call result: " + res);
		assertTrue(response.getStatus()<400);
	}

}
