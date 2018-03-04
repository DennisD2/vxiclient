package de.spurtikus.vxi.service;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.spurtikus.vxi.connectors.ConnectorConfig;

/**
 * Boundary for device system, a partly virtual device keeping information about
 * the mainframes and the REST interface.
 * 
 * @author dennis
 *
 */
@Path("/" + Constants.URL_SYSTEM)
public class SystemBoundary {
	private Logger logger = LoggerFactory.getLogger(SystemBoundary.class);

	protected ConnectionManager connManager;

	private String className;
	
	private final String defaultMessage = "VXI system REST API.";
	
	public SystemBoundary() {
		className = Constants.URL_SYSTEM.replace("/", "");
	}

	/*
	 * TODO: the Path("/") produces tomcat startup warning: 
	 * The following warnings have been detected: WARNING: 
	 * The (sub)resource method defaultAnswer in de.spurtikus.vxi.service.SystemBoundary 
	 * contains empty path annotation.
	 */
	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/")
	public Response defaultAnswer(@Context UriInfo uriInfo) {
		logger.debug("Incoming URI : {}", uriInfo.getPath());
		return Response.ok(defaultMessage).build();
	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/info")
	public Response info(@Context UriInfo uriInfo) {
		logger.debug("Incoming URI : {}", uriInfo.getPath());
		return Response.ok(className).build();
	}
	
	@POST
	@Path("/getConfig")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public Response getConfig(@Context UriInfo uriInfo) {
		// Load configuration
		try {
			Configuration.load();
		} catch (Exception e) {
			logger.error("Cannot load configuration");
			e.printStackTrace();
		}
		List<ConnectorConfig> confs = Configuration.getEnabledConfigs();
		List<ExternalVXIDescriptor> extDevs = new ArrayList<>();
		for (ConnectorConfig c: confs) {
			for (DeviceInfo d: c.getDevices()) {
				String name = d.getName();
				String url = restUrlForType(d.getType()) + '/' + d.getMainframe() + '/' + d.getName();
				String type = d.getType();
				String mf = d.getMainframe();
				ExternalVXIDescriptor de = new ExternalVXIDescriptor(name, type, url, mf);
				extDevs.add(de);
			}
		}
		return Response.ok(extDevs).build();
	}

	private String restUrlForType(String type) {
		String u="?";
		switch (type) {
			case "mainframe": u=Constants.URL_MAINFRAME; break;
			case "multimeter": u=Constants.URL_MULTIMETER; break;
			case "pacer": u=Constants.URL_PACER; break;
			case "counter": u=Constants.URL_COUNTER; break;
			case "switch": u=Constants.URL_SWITCH; break;
			case "afg": u=Constants.URL_AFG; break;
			case "digitalIO": u=Constants.URL_DIGITALIO; break;
			default: logger.error("Cannot derive REST URL for device type : {}", type);
		}
		// handle complex types 
		if (type.startsWith("multimeter/")) {
			//String subType = type.replace("multimeter/", "");
			u=Constants.URL_MULTIMETER;
		}
		return u;
	}

}