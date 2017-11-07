package de.spurtikus.vxi.service;

import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.util.Arrays;

import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.spurtikus.vxi.connectors.ConnectorConfig;
import de.spurtikus.vxi.connectors.VXIConnector;
import de.spurtikus.vxi.connectors.VXIConnectorFactory;

@RunWith(Arquillian.class)
public class ServiceTest {
	@Inject
	Greeter greeter;
	@Inject 

	@Deployment
	public static WebArchive createDeployment() {
				File[] lib = Maven.resolver()
			            .resolve("org.jboss.weld.servlet:weld-servlet:1.1.9.Final", 
			            		"de.spurtikus:vxiclient:0.0.1-SNAPSHOT")
			            .withTransitivity().as(File.class);
				
				WebArchive jar = ShrinkWrap.create(WebArchive.class, "vxi.war")
		            .addClass(Greeter.class)
		            .addAsManifestResource("arquillian.xml")
		            .addAsLibraries(lib)
		            //.addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
		            .addAsManifestResource("META-INF/beans.xml", "beans.xml")
		            .setWebXML("web.xml");
		        
		        System.out.println(jar.toString(true));
		        
		        return jar;	
	}

	@Test
	public void should_create_greeting() {
	    Assert.assertEquals("Hello, Earthling!",
	        greeter.createGreeting("Earthling"));
	    greeter.greet(System.out, "Earthling");
	}
	
	//@Ignore
	@Test
	public void waittest() throws Exception {
		final int SERIAL_CONFIG = 1;
		final int RPC_CONFIG = 2;
		final String TEST_DEVICE_NAME = "hp1301";
		Configuration.load();
		ConnectorConfig config = Configuration.findConfigById(SERIAL_CONFIG);
		VXIConnector vxiConnector = VXIConnectorFactory.getConnector(config);

		String deviceid = config.getDeviceIdByName(TEST_DEVICE_NAME);
		assertNotNull(deviceid);
		//Object theLid = vxiConnector.initialize(config, deviceid);

	}

}
