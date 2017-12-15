package de.spurtikus.vxi.service;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;

@ApplicationPath("/")
public class RSApplication extends ResourceConfig {

	public RSApplication() {
		register(SystemBoundary.class);
		register(HP1300Boundary.class);
		register(HP1300PacerBoundary.class);
		register(HP1326Boundary.class);
		register(HP1330Boundary.class);
		register(HP1333Boundary.class);
		register(HP1340Boundary.class);
		register(HP1351Boundary.class);
		
		// CORS
		register(CORSFilter.class);
		// Enable Tracing support.
        property(ServerProperties.TRACING, "ALL");
	}
}
