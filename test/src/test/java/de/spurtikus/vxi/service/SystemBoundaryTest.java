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
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class SystemBoundaryTest extends BaseBoundaryTest {
	public final String DEVICECLASS = Constants.URL_SYSTEM;
	public final String URI = BASE_URI + DEVICECLASS ;
	
	@Test
	@RunAsClient
	public void defaultAnswer(@ArquillianResource URL contextPath) {
		Client client = ClientBuilder.newClient();
		String url = contextPath + URI + "/";
		System.out.println(url);
		final Response response = client.target(url)
				.request(MediaType.TEXT_PLAIN).get();
		assertTrue(response.getStatus()<400);
		assertEquals("VXI system REST API. Valid URLs: /info, /config.", response.readEntity(String.class));
	}
	
	@Test
	@RunAsClient
	public void info(@ArquillianResource URL contextPath) {
		Client client = ClientBuilder.newClient();
		String url = contextPath + URI + "/info";
		System.out.println(url);
		final Response response = client.target(url)
				.request(MediaType.TEXT_PLAIN).get();
		assertTrue(response.getStatus()<400);
		assertEquals("system", response.readEntity(String.class));
	}
	
	@Test
	@RunAsClient
	public void getConfig(@ArquillianResource URL contextPath) {
		Client client = ClientBuilder.newClient();
		String url = contextPath + URI + "/getConfig";
		System.out.println(url);
		final Response response = client.target(url)
				.request(MediaType.APPLICATION_JSON).post(null);
		assertTrue(response.getStatus()<400);
		String res = response.readEntity(String.class);
		System.out.println(res);
		//assertEquals("VXI system REST API", response.readEntity(String.class));
		try {
			Thread.sleep(50000000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
