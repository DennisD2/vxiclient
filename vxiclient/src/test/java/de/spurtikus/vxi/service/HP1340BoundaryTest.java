package de.spurtikus.vxi.service;

import static org.junit.Assert.assertNotNull;

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
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class HP1340BoundaryTest {

	public final String BASE_URI = "rest/api";
	public final String DEVICE = "hp1340";
	public final String MAINFRAME = "mfb";
	public final String URI = BASE_URI + "/" + DEVICE + "/" + MAINFRAME;

	@Deployment
	public static WebArchive createDeployment() {
		File[] lib = Maven.resolver()
				.resolve("de.spurtikus:vxiclient:0.0.1-SNAPSHOT")
				.withTransitivity().as(File.class);

		WebArchive jar = ShrinkWrap.create(WebArchive.class, "vxi.war")
				.addClass(SystemBoundary.class).addClass(HP1340Boundary.class)
				.addAsManifestResource("arquillian.xml").addAsLibraries(lib)
				.addAsManifestResource("META-INF/context.xml", "context.xml")
				.setWebXML("web.xml");

		// System.out.println(jar.toString(true));
		return jar;
	}

	@Ignore
	@Test
	@RunAsClient
	public void test_info(@ArquillianResource URL contextPath) {
		String uri = URI + "/info";
		Client client = ClientBuilder.newClient();
		System.out.println(contextPath + uri);
		final Response response = client.target(contextPath + uri)
				.request(MediaType.TEXT_PLAIN).get();
		String res = response.readEntity(String.class);
		System.out.println(uri + " -> " + res);
		assertNotNull(res);
	}

	@Ignore
	@Test
	@RunAsClient
	public void test_idn(@ArquillianResource URL contextPath) {
		String uri = URI + "/hp1340/idn";
		Client client = ClientBuilder.newClient();
		System.out.println(contextPath + uri);
		final Response response = client.target(contextPath + uri)
				.request(MediaType.APPLICATION_JSON).post(null);
		String res = response.readEntity(String.class);
		System.out.println(uri + " -> " + res);
		assertNotNull(res);
	}

	// @Ignore
	@Test
	@RunAsClient
	public void test_shape(@ArquillianResource URL contextPath) {
		String waveform = "sin";
		String amplitude = "5.0";
		String frequency = "5e5";
		String uri = URI + "/hp1340/shape/" + waveform + "/" + amplitude + "/"
				+ frequency;
		
		Client client = ClientBuilder.newClient();
		System.out.println("Call: " + contextPath + uri);
		final Response response = client.target(contextPath + uri)
				.request(MediaType.APPLICATION_JSON).post(null);
		String res = response.readEntity(String.class);
		System.out.println("Call result: " + res);
		assertNotNull(res);
		try {
			Thread.sleep(500000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
