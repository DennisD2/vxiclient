package de.spurtikus.vxi.service;

import static org.junit.Assert.assertEquals;

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

import de.spurtikus.vxi.beans.Greeter;

@RunWith(Arquillian.class)
public class ServiceTest {

	@Deployment
	public static WebArchive createDeployment() {
		File[] lib = Maven.resolver()
				.resolve(/*"org.jboss.weld.servlet:weld-servlet-shaded:3.0.0.Final",*/
						"de.spurtikus:vxiclient:0.0.1-SNAPSHOT")
				.withTransitivity().as(File.class);

		WebArchive jar = ShrinkWrap.create(WebArchive.class, "vxi.war")
				.addClass(Greeter.class)
				.addClass(Service.class)
				.addAsManifestResource("arquillian.xml").addAsLibraries(lib)
				.addAsWebInfResource("beans.xml", "beans.xml")
				.addAsManifestResource("META-INF/context.xml", "context.xml")
				.setWebXML("web.xml");

		System.out.println(jar.toString(true));

		return jar;
	}

	@Test
	@RunAsClient
	public void testREST(@ArquillianResource URL contextPath) {
		Client client = ClientBuilder.newClient();
		// final Response response = webTarget.path(contextPath+"/api/hehe")
		// .request(MediaType.JSON).
		// //.post(Entity.json(new UserData("myuser", "mypassword")));
		System.out.println(contextPath + "rest/api/info");
		final Response response = client.target(contextPath + "rest/api/info")
				.request(MediaType.TEXT_PLAIN).get();
		/*try {
			Thread.sleep(50000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/
		assertEquals("Hello, VXI system REST API!", response.readEntity(String.class));
	}

}
