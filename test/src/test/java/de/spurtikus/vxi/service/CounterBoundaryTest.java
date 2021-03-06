package de.spurtikus.vxi.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.URL;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;


/**
 * Test counter boundary. Note that the @FixMethodOrder is used to execute the
 * read test at the end.
 * 
 * @author dennis
 *
 */
@RunWith(Arquillian.class)
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CounterBoundaryTest extends BaseBoundaryTest {
	public final String DEVICECLASS = Constants.URL_COUNTER;
	public final String MAINFRAME = "mfb";
	public final String DEVICENAME = "hp1333";
	public final String URI = BASE_URI + "/" + DEVICECLASS + "/" + MAINFRAME
			+ "/" + DEVICENAME;

	@Test
	@RunAsClient
	public void info(@ArquillianResource URL contextPath) {
		String uri = URI + "/info";
		Client client = ClientBuilder.newClient();
		System.out.println(contextPath + uri);
		final Response response = client.target(contextPath + uri)
				.request(MediaType.TEXT_PLAIN).get();
		assertTrue(response.getStatus() < 400);
		String res = response.readEntity(String.class);
		System.out.println(uri + " -> " + res);
		assertEquals(Constants.URL_COUNTER, res);
	}

	@Test
	@RunAsClient
	public void idn(@ArquillianResource URL contextPath) {
		String uri = URI + "/idn";
		Client client = ClientBuilder.newClient();
		System.out.println(contextPath + uri);
		final Response response = client.target(contextPath + uri)
				.request(MediaType.APPLICATION_JSON).post(null);
		assertTrue(response.getStatus() < 400);
		String res = response.readEntity(String.class);
		System.out.println(uri + " -> " + res);
		assertEquals("HEWLETT-PACKARD,E1333A,0,A.04.01", res);
	}

	@Test
	@RunAsClient
	public void configure(@ArquillianResource URL contextPath) {
		String channel = "1";
		String mode = "freq";
		String uri = URI + "/configure/" + channel + "/" + mode;

		Client client = ClientBuilder.newClient();
		System.out.println("Call: " + contextPath + uri);
		final Response response = client.target(contextPath + uri)
				.request(MediaType.APPLICATION_JSON).post(null);
		assertTrue(response.getStatus() < 400);
		String res = response.readEntity(String.class);
		System.out.println("Call result: " + res);
	}

	@Test
	@RunAsClient
	public void setCoupling(@ArquillianResource URL contextPath) {
		String coupling = "dc";
		String uri = URI + "/coupling/" + coupling;

		Client client = ClientBuilder.newClient();
		System.out.println("Call: " + contextPath + uri);
		final Response response = client.target(contextPath + uri)
				.request(MediaType.APPLICATION_JSON).post(null);
		assertTrue(response.getStatus() < 400);
		String res = response.readEntity(String.class);
		System.out.println("Call result: " + res);
	}

	@Test
	@RunAsClient
	public void setLowPassFilter(@ArquillianResource URL contextPath) {
		String onoff = "false";
		String uri = URI + "/lowPassFilter/" + onoff;

		Client client = ClientBuilder.newClient();
		System.out.println("Call: " + contextPath + uri);
		final Response response = client.target(contextPath + uri)
				.request(MediaType.APPLICATION_JSON).post(null);
		assertTrue(response.getStatus() < 400);
		String res = response.readEntity(String.class);
		System.out.println("Call result: " + res);
	}

	@Test
	@RunAsClient
	public void setImpedance(@ArquillianResource URL contextPath) {
		String impedance = "MIN";
		String uri = URI + "/impedance/" + impedance;

		Client client = ClientBuilder.newClient();
		System.out.println("Call: " + contextPath + uri);
		final Response response = client.target(contextPath + uri)
				.request(MediaType.APPLICATION_JSON).post(null);
		assertTrue(response.getStatus() < 400);
		String res = response.readEntity(String.class);
		System.out.println("Call result: " + res);
	}

	@Test
	@RunAsClient
	public void setAperture(@ArquillianResource URL contextPath) {
		String channel = "1";
		// String aperture = "0.128";
		String aperture = "0.128";
		String uri = URI + "/aperture/" + channel + "/" + aperture;

		Client client = ClientBuilder.newClient();
		System.out.println("Call: " + contextPath + uri);
		final Response response = client.target(contextPath + uri)
				.request(MediaType.APPLICATION_JSON).post(null);
		assertTrue(response.getStatus() < 400);
		String res = response.readEntity(String.class);
		System.out.println("Call result: " + res);
	}

	@Test
	@RunAsClient
	public void setAttenuation(@ArquillianResource URL contextPath) {
		String attenuation = "MIN";
		String uri = URI + "/attenuation/" + attenuation;

		Client client = ClientBuilder.newClient();
		System.out.println("Call: " + contextPath + uri);
		final Response response = client.target(contextPath + uri)
				.request(MediaType.APPLICATION_JSON).post(null);
		assertTrue(response.getStatus() < 400);
		String res = response.readEntity(String.class);
		System.out.println("Call result: " + res);
	}

	@Test
	@RunAsClient
	public void x_read(@ArquillianResource URL contextPath) {
		String channel = "1";
		String uri = URI + "/read/" + channel;

		Client client = ClientBuilder.newClient();
		System.out.println("Call: " + contextPath + uri);
		final Response response = client.target(contextPath + uri)
				.request(MediaType.APPLICATION_JSON).post(null);
		assertTrue(response.getStatus() < 400);
		String res = response.readEntity(String.class);
		System.out.println("Call result: " + res);
		try {
			Thread.sleep(5000000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
