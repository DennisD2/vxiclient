package de.spurtikus.vxi.service;

import static org.junit.Assert.*;

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
import org.junit.Test;
import org.junit.runner.RunWith;

import de.spurtikus.vxi.Constants;

@RunWith(Arquillian.class)
public class AFGBoundaryTest {

	public final String BASE_URI = Constants.SERVICE_ROOT;
	public final String DEVICECLASS = Constants.URL_AFG;
	public final String MAINFRAME = "mfb";
	public final String DEVICENAME = "hp1340";
	public final String URI = BASE_URI + "/" + DEVICECLASS + "/" + MAINFRAME + "/" + DEVICENAME;

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
		assertEquals(Constants.URL_AFG, res);
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
		assertEquals("HEWLETT-PACKARD,E1340A,0,A.01.02", res);
	}

	// @Ignore
	@Test
	@RunAsClient
	public void shape(@ArquillianResource URL contextPath) {
		String waveform = "sin";
		String amplitude = "5.0";
		String frequency = "5e5";
		String uri = URI + "/shape/" + waveform + "/" + amplitude + "/"
				+ frequency;
		
		Client client = ClientBuilder.newClient();
		System.out.println("Call: " + contextPath + uri);
		final Response response = client.target(contextPath + uri)
				.request(MediaType.APPLICATION_JSON).post(null);
		assertTrue(response.getStatus()<400);
		String res = response.readEntity(String.class);
		System.out.println("Call result: " + res);
		try {
			Thread.sleep(500000000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
