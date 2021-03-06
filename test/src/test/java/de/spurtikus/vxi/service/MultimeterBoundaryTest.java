package de.spurtikus.vxi.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class MultimeterBoundaryTest extends BaseBoundaryTest {
	public final String DEVICECLASS = Constants.URL_MULTIMETER;
	//public final String MAINFRAME = "mfb";
	//public final String DEVICENAME = "hp1326";
	//public final String DEVICE_IDN_ANSWER = "HEWLETT-PACKARD,E1326B,0,A.05.00";
	public final String MAINFRAME = "mfc";
	public final String DEVICENAME = "hp1411";
	public final String DEVICE_IDN_ANSWER = "HEWLETT-PACKARD,E1411B,0,D.06.03";

	public final String URI = BASE_URI + DEVICECLASS + "/" + MAINFRAME + "/" + DEVICENAME;

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
		assertEquals(Constants.URL_MULTIMETER,res);
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
		assertEquals(DEVICE_IDN_ANSWER,res);
	}

	@Test
	@RunAsClient
	public void read(@ArquillianResource URL contextPath) {
		String range = "7.27";
		String uri = URI + "/read/" + range;

		Client client = ClientBuilder.newClient();
		System.out.println("Call: " + contextPath + uri);
		final Response response = client.target(contextPath + uri)
				.request(MediaType.APPLICATION_JSON)
				.post(Entity.json(generateChannels()));
		assertTrue(response.getStatus()<400);
		String res = response.readEntity(String.class);
		System.out.println("Call result: " + res);
		try {
			Thread.sleep(50000000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	protected List<Integer> generateChannels() {
		List<Integer> channels = new ArrayList<Integer>();
		channels.add(100);
		channels.add(101);
		// channels.add(102);
		// channels.add(103);
		// channels.add(104);
		// channels.add(105);
		// channels.add(106);
		// channels.add(107);
		// channels.add(108);
		// channels.add(109);
		// channels.add(110);
		// channels.add(111);
		// channels.add(112);
		// channels.add(113);
		return channels;
	}

}
