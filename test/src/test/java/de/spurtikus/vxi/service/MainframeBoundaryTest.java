package de.spurtikus.vxi.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.URL;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(Arquillian.class)
public class MainframeBoundaryTest extends BaseBoundaryTest {
	public final String DEVICECLASS = Constants.URL_MAINFRAME;
	public final String MAINFRAME = "mfb";
	public final String DEVICENAME = "hp1301";
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
		assertTrue(response.getStatus() < 400);
		assertEquals(Constants.URL_MAINFRAME, response.readEntity(String.class));
	}

	@Test
	@RunAsClient
	public void idn(@ArquillianResource URL contextPath) {
		String uri = URI + "/idn";
		Client client = ClientBuilder.newClient();
		System.out.println(contextPath + uri);
		final Response response = client.target(contextPath + uri)
				.request(MediaType.APPLICATION_JSON).post(Entity.json(""));
		String result = response.readEntity(String.class);
		System.out.println(result);
		assertTrue(response.getStatus() < 400);
		assertEquals("HEWLETT-PACKARD,E1301A,0,A.07.00", result);
		try {
			Thread.sleep(5000000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	//@Ignore
	@Test
	@RunAsClient
	public void devices(@ArquillianResource URL contextPath) {
		String uri = URI + "/devices";
		Client client = ClientBuilder.newClient();
		System.out.println(contextPath + uri);
		final Response response = client.target(contextPath + uri)
				.request(MediaType.APPLICATION_JSON).post(Entity.json(""));
		assertTrue(response.getStatus() < 400);
		String d = response.readEntity(String.class);
		System.out.println(d);
	}

}
