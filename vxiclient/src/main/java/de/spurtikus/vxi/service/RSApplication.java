package de.spurtikus.vxi.service;

import javax.ws.rs.ApplicationPath;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;

@ApplicationPath("/")
public class RSApplication extends ResourceConfig {

	public RSApplication() {
		register(SystemBoundary.class);
		register(MainframeBoundary.class);
		register(PacerBoundary.class);
		register(MultimeterBoundary.class);
		register(DIOBoundary.class);
		register(CounterBoundary.class);
		register(AFGBoundary.class);
		register(SwitchBoundary.class);
		
		// CORS
		register(CORSFilter.class);
		// Enable Tracing support.
        property(ServerProperties.TRACING, "ALL");
	}
}
