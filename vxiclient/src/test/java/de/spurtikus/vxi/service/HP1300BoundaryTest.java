package de.spurtikus.vxi.service;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URL;

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

@RunWith(Arquillian.class)
public class HP1300BoundaryTest {

	public final String BASE_URI = "rest/api";
	public final String DEVICECLASS = "hp1300";
	public final String MAINFRAME = "mfb";
	public final String DEVICENAME = "hp1301";
	public final String URI = BASE_URI + "/" + DEVICECLASS + "/" + MAINFRAME + "/" + DEVICENAME;

	@Deployment
	public static WebArchive createDeployment() {
		File[] lib = Maven.resolver()
				.resolve(/*"org.jboss.weld.servlet:weld-servlet-shaded:3.0.0.Final",*/
						"de.spurtikus:vxiclient:0.0.1-SNAPSHOT")
				.withTransitivity().as(File.class);

		WebArchive jar = ShrinkWrap.create(WebArchive.class, "vxi.war")
				.addClass(SystemBoundary.class)
				.addClass(HP1300Boundary.class)
				.addAsManifestResource("arquillian.xml").addAsLibraries(lib)
				.addAsManifestResource("META-INF/context.xml", "context.xml")
				.setWebXML("web.xml");

		System.out.println(jar.toString(true));

		return jar;
	}

	//@Ignore
	@Test
	@RunAsClient
	public void test_systemInfo(@ArquillianResource URL contextPath) {
		String uri = URI + "/info";
		Client client = ClientBuilder.newClient();
		System.out.println(contextPath + uri);
		final Response response = client.target(contextPath + uri)
				.request(MediaType.TEXT_PLAIN).get();
		assertTrue(response.getStatus()<400);
		assertEquals("HP1300", response.readEntity(String.class));
	}
	
	@Test
	@RunAsClient
	public void devices(@ArquillianResource URL contextPath) {
		String uri = URI + "/devices";
		Client client = ClientBuilder.newClient();
		System.out.println(contextPath + uri);
		final Response response = client.target(contextPath + uri)
				.request(MediaType.APPLICATION_JSON).post(Entity.json(""));
		assertTrue(response.getStatus()<400);
		String d = response.readEntity(String.class);
		System.out.println(d);
	}
	
}
