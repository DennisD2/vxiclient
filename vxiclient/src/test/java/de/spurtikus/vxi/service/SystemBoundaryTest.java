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
public class SystemBoundaryTest {
	public final String BASE_URI = Constants.SERVICE_ROOT;
	public final String DEVICECLASS = Constants.URL_SYSTEM;
	public final String URI = BASE_URI + DEVICECLASS ;

	@Deployment
	public static WebArchive createDeployment() {
		File[] lib = Maven.resolver()
				.resolve(/*"org.jboss.weld.servlet:weld-servlet-shaded:3.0.0.Final",*/
						"de.spurtikus:vxiclient:0.0.1-SNAPSHOT")
				.withTransitivity().as(File.class);

		WebArchive jar = ShrinkWrap.create(WebArchive.class, "vxi.war")
				.addClass(SystemBoundary.class)
				.addAsManifestResource("arquillian.xml").addAsLibraries(lib)
				.addAsManifestResource("META-INF/context.xml", "context.xml")
				.setWebXML("web.xml");

		System.out.println(jar.toString(true));

		return jar;
	}

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
		assertEquals("VXI system REST API", response.readEntity(String.class));
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
