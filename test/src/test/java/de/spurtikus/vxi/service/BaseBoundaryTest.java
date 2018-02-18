package de.spurtikus.vxi.service;

import java.io.File;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.jboss.shrinkwrap.resolver.api.maven.Maven;
import org.jboss.shrinkwrap.resolver.api.maven.PackagingType;
import org.jboss.shrinkwrap.resolver.api.maven.coordinate.MavenCoordinates;
import org.jboss.shrinkwrap.resolver.api.maven.coordinate.MavenDependencies;

import de.spurtikus.vxi.Constants;

public class BaseBoundaryTest {
	public final String BASE_URI = Constants.SERVICE_ROOT;

	@Deployment
	public static WebArchive createDeployment() {
		File[] lib = Maven.resolver()
				.addDependency(MavenDependencies.createDependency(
	                    MavenCoordinates.createCoordinate(
	                            "de.spurtikus", 
	                            "vxiclient-web", 
	                            "0.0.2-SNAPSHOT", 
	                            PackagingType.JAR, 
	                            "service"), 
	                        null, 
	                        false))
				.resolve()
				/*.resolve("de.spurtikus:vxiclient-web:jar:0.0.2-SNAPSHOT:service" */
				.withTransitivity().as(File.class);
	
		WebArchive jar = ShrinkWrap.create(WebArchive.class, "vxi.war")
				.addClass(SystemBoundary.class)
				.addAsManifestResource("arquillian.xml").addAsLibraries(lib)
				.addAsManifestResource("META-INF/context.xml", "context.xml")
				.setWebXML("web.xml");
	
		System.out.println(jar.toString(true));
	
		return jar;
	}

}
