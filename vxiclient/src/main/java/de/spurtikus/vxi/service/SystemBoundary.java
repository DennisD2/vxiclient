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

import de.spurtikus.vxi.Constants;
import de.spurtikus.vxi.connectors.ConnectorConfig;

/**
 * Boundary for device system, a partly virtual device keeping information about
 * the mainframes and the REST interface.
 * 
 * @author dennis
 *
 */
@Path(Constants.URL_SYSTEM)
public class SystemBoundary {
	public final static String className = "SystemBoundary";

	private Logger logger = LoggerFactory.getLogger(SystemBoundary.class);

	protected ConnectionManager connManager;

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/")
	public Response defaultAnswer(@Context UriInfo uriInfo) {
		logger.debug("Incoming URI : {}", uriInfo.getPath());
		return Response.ok("VXI system REST API. Valid URLs: /info, /config.").build();
	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("/info")
	public Response info(@Context UriInfo uriInfo) {
		logger.debug("Incoming URI : {}", uriInfo.getPath());
		return Response.ok("VXI system REST API").build();
	}
	
	@POST
	@Path("/getConfig")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces({ MediaType.APPLICATION_JSON })
	public Response setVoltageRange(@Context UriInfo uriInfo) {
		// Load configuration
		try {
			Configuration.load();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		List<ConnectorConfig> confs = Configuration.getEnabledConfigs();
		List<ExternalVXIDescriptor> extDevs = new ArrayList<>();
		for (ConnectorConfig c: confs) {
			for (DeviceInfo d: c.getDevices()) {
				String name = d.getName();
				String url = Constants.SERVICE_ROOT + restUrlForType(d.getType()) + '/' + d.getMainframe() + '/' + d.getName();
				String type = d.getType();
				ExternalVXIDescriptor de = new ExternalVXIDescriptor(name, type, url);
				extDevs.add(de);
			}
		}
		return Response.ok(extDevs).build();
	}

	private String restUrlForType(String type) {
		String u="?";
		switch (type) {
		case "mainframe": u=Constants.URL_MAINFRAME; break;
		case "voltmeter": u=Constants.URL_VOLTMETER; break;
		case "pacer":     u=Constants.URL_PACER; break;
		case "counter": u=Constants.URL_COUNTER; break;
		case "switch": u=Constants.URL_SWITCH; break;
		case "afg": u=Constants.URL_AFG; break;
		case "digitalIO": u=Constants.URL_DIGITALIO; break;
		
		}
		return u;
	}

}