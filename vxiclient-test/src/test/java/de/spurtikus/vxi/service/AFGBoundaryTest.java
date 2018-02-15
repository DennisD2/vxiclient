package de.spurtikus.vxi.service;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URL;

import javax.ws.rs.Path;
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
import org.junit.Test;
import org.junit.runner.RunWith;

import de.spurtikus.vxi.Constants;

@RunWith(Arquillian.class)
public class AFGBoundaryTest {

	public final String BASE_URI = Constants.SERVICE_ROOT;
	public final String DEVICECLASS = Constants.URL_AFG;
	public final String MAINFRAME = "mfb";
	public final String DEVICENAME = "hp1340";
	public final String URI = BASE_URI + "/" + DEVICECLASS + "/" + MAINFRAME
			+ "/" + DEVICENAME;

	@Deployment
	public static WebArchive createDeployment() {
		File[] lib = Maven.resolver()
				.resolve("de.spurtikus:vxiclient:0.0.1-SNAPSHOT")
				.withTransitivity().as(File.class);

		WebArchive jar = ShrinkWrap.create(WebArchive.class, "vxi.war")
				.addClass(SystemBoundary.class).addClass(AFGBoundary.class)
				.addAsManifestResource("arquillian.xml").addAsLibraries(lib)
				.addAsManifestResource("META-INF/context.xml", "context.xml")
				.setWebXML("web.xml");

		// System.out.println(jar.toString(true));
		return jar;
	}

	// @Ignore
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
		assertEquals(Constants.URL_AFG, res);
	}

	// @Ignore
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
		assertEquals("HEWLETT-PACKARD,E1340A,0,A.01.02", res);
	}

	@Test
	@RunAsClient
	public void initialize(@ArquillianResource URL contextPath) {
		String uri = URI + "/initialize";

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
	public void getConfiguration(@ArquillianResource URL contextPath) {
		String uri = URI + "/getConfiguration";

		Client client = ClientBuilder.newClient();
		System.out.println("Call: " + contextPath + uri);
		final Response response = client.target(contextPath + uri)
				.request(MediaType.APPLICATION_JSON).post(null);
		assertTrue(response.getStatus() < 400);
		String res = response.readEntity(String.class);
		System.out.println("Call result: " + res);
	}

	// @Ignore
	@Test
	@RunAsClient
	public void shapeStandard(@ArquillianResource URL contextPath) {
		String waveform = "sine";
		String uri = URI + "/setShape/standard/" + waveform;

		Client client = ClientBuilder.newClient();
		System.out.println("Call: " + contextPath + uri);
		final Response response = client.target(contextPath + uri)
				.request(MediaType.APPLICATION_JSON).post(null);
		assertTrue(response.getStatus() < 400);
		String res = response.readEntity(String.class);
		System.out.println("Call result: " + res);
		try {
			Thread.sleep(500000000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	// @Ignore
	@Test
	@RunAsClient
	public void shapeBuiltin(@ArquillianResource URL contextPath) {
		String waveform = "haversine";
		char segment = 'A';
		String uri = URI + "/setShape/builtin/" + waveform + "/" + segment;

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
	public void amplitude(@ArquillianResource URL contextPath) {
		String amplitude = "5.0";
		String uri = URI + "/setAmplitude/" + amplitude;

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
	public void frequency(@ArquillianResource URL contextPath) {
		String frequency = "5e5";
		String uri = URI + "/setFrequency/" + frequency;

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
	public void offset(@ArquillianResource URL contextPath) {
		String offset = "1.0";
		String uri = URI + "/setOffset/" + offset;

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
	public void sweep(@ArquillianResource URL contextPath) {
		// @Path("{mainframe}/{devname}/setSweep/builtin/{start}/{stop}/{points}/{duration}/{amplitude}/{waveform}")
		String startFreq = "5e3";
		String endFreq = "5e5";
		int points = 100;
		int duration = 10;
		String amplitude = "5";
		String waveform = "sine";

		StringBuilder sb = new StringBuilder();
		sb.append(URI);
		sb.append("/setSweep/");
		sb.append(startFreq);
		sb.append("/");
		sb.append(endFreq);
		sb.append("/");
		sb.append(points);
		sb.append("/");
		sb.append(duration);
		sb.append("/");
		sb.append(amplitude);
		sb.append("/");
		sb.append(waveform);
		String uri = sb.toString();

		Client client = ClientBuilder.newClient();
		System.out.println("Call: " + contextPath + uri);
		final Response response = client.target(contextPath + uri)
				.request(MediaType.APPLICATION_JSON).post(null);
		assertTrue(response.getStatus() < 400);
		String res = response.readEntity(String.class);
		System.out.println("Call result: " + res);
		/*
		 * try { Thread.sleep(500000000); } catch (InterruptedException e) {
		 * e.printStackTrace(); }
		 */
	}

	@Test
	@RunAsClient
	public void marker(@ArquillianResource URL contextPath) {
		// @Path("{mainframe}/{devname}/setMarker/{source}/{polarity}")
		String source = "outp_zero";
		String polarity = "inv";

		StringBuilder sb = new StringBuilder();
		sb.append(URI);
		sb.append("/setMarker/");
		sb.append(source);
		sb.append("/");
		sb.append(polarity);
		String uri = sb.toString();

		Client client = ClientBuilder.newClient();
		System.out.println("Call: " + contextPath + uri);
		final Response response = client.target(contextPath + uri)
				.request(MediaType.APPLICATION_JSON).post(null);
		assertTrue(response.getStatus() < 400);
		String res = response.readEntity(String.class);
		System.out.println("Call result: " + res);
		/*
		 * try { Thread.sleep(500000000); } catch (InterruptedException e) {
		 * e.printStackTrace(); }
		 */
	}

}
