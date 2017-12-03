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

@RunWith(Arquillian.class)
public class HP1340BoundaryTest {

	@Deployment
	public static WebArchive createDeployment() {
		File[] lib = Maven.resolver()
				.resolve(/*"org.jboss.weld.servlet:weld-servlet-shaded:3.0.0.Final",*/
						"de.spurtikus:vxiclient:0.0.1-SNAPSHOT")
				.withTransitivity().as(File.class);

		WebArchive jar = ShrinkWrap.create(WebArchive.class, "vxi.war")
				.addClass(SystemBoundary.class)
				.addClass(HP1340Boundary.class)
				.addAsManifestResource("arquillian.xml").addAsLibraries(lib)
				.addAsManifestResource("META-INF/context.xml", "context.xml")
				.setWebXML("web.xml");

		System.out.println(jar.toString(true));

		return jar;
	}

	@Test
	@RunAsClient
	public void test_systemInfo(@ArquillianResource URL contextPath) {
		Client client = ClientBuilder.newClient();
		// final Response response = webTarget.path(contextPath+"/api/hehe")
		// .request(MediaType.JSON).
		// //.post(Entity.json(new UserData("myuser", "mypassword")));
		System.out.println(contextPath + "rest/api/system/info");
		final Response response = client.target(contextPath + "rest/api/system/info")
				.request(MediaType.TEXT_PLAIN).get();
		/*try {
			Thread.sleep(50000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/
		assertEquals("VXI system REST API", response.readEntity(String.class));
	}
	
	@Test
	@RunAsClient
	public void test_deviceBase(@ArquillianResource URL contextPath) {
		Client client = ClientBuilder.newClient();
		System.out.println(contextPath + "rest/api/hp1340/reset");
		final Response response = client.target(contextPath + "rest/api/hp1340/reset")
				.request(MediaType.APPLICATION_JSON).post(null);
		String res = response.readEntity(String.class);
		System.out.println("REST call  response: " + res);
		assertNotNull(res);
	}

}
