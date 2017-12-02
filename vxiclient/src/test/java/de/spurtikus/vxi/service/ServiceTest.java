package de.spurtikus.vxi.service;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.net.URL;

import javax.inject.Inject;
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

import de.spurtikus.vxi.service.beans.Greeter;

@RunWith(Arquillian.class)
public class ServiceTest {
	@Inject
	Greeter greeter;

	@Deployment
	public static WebArchive createDeployment() {
		File[] lib = Maven.resolver()
				.resolve("org.jboss.weld.servlet:weld-servlet:1.1.9.Final",
						"de.spurtikus:vxiclient:0.0.1-SNAPSHOT")
				.withTransitivity().as(File.class);

		WebArchive jar = ShrinkWrap.create(WebArchive.class, "vxi.war")
				.addClass(Greeter.class)
				.addClass(Service.class)
				.addAsManifestResource("arquillian.xml").addAsLibraries(lib)
				// .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
				.addAsManifestResource("META-INF/beans.xml", "beans.xml")
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
		System.out.println(contextPath + "rest/api/hehe");
		final Response response = client.target(contextPath + "rest/api/hehe")
				.request(MediaType.TEXT_PLAIN).get();
		assertEquals("hehe", response.readEntity(String.class));
	}

}
