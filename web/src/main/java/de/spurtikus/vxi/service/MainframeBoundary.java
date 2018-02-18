package de.spurtikus.vxi.service;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriInfo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.spurtikus.devices.hp.HP1300b;
import de.spurtikus.vxi.Constants;
import de.spurtikus.vxi.mainframes.hp1300b.VXIDevice;

/**
 * Mainframe boundary.
 * 
 * Tested with:
 * * HP1300 mainframe. See class {HP1300b}.
 * 
 * @author dennis
 *
 */
@Path("/" + Constants.URL_MAINFRAME)
public class MainframeBoundary extends AbstractBoundary<HP1300b> {
	private Logger logger = LoggerFactory.getLogger(MainframeBoundary.class);
	
	public MainframeBoundary() {
		className = Constants.URL_MAINFRAME;
	}

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	@Path("{mainframe}/{devname}/info")
	public Response info(@Context UriInfo uriInfo,
			@PathParam("mainframe") String mainframe,
			@PathParam("devname") String devname) {
		logger.debug("Incoming URI : {}", uriInfo.getPath());
		logger.debug("Mainframe: {}", mainframe);
		logger.debug("Device name: {}", devname);
		return Response.ok(getClassName()).build();
	}

	@POST
	@Path("{mainframe}/{devname}/idn")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces(MediaType.APPLICATION_JSON)
	public Response idn(@Context UriInfo uriInfo,
			@PathParam("mainframe") String mainframe,
			@PathParam("devname") String devname) {
		logger.debug("Incoming URI : {}", uriInfo.getPath());
		logger.debug("Mainframe: {}", mainframe);
		logger.debug("Device name: {}", devname);

		try {
			connManager = ConnectionManager.getInstance(this.getClass(),
					mainframe, devname);
		} catch (Exception e) {
			logger.error(
					"Cannot get wrapper instance. This is usually an initialization problem.");
			return Response.status(Status.NOT_FOUND).build();
		}

		String answer;
		try {
			answer = connManager
					.getConnector(this.getClass(), mainframe, devname)
					.send_and_receive(connManager.getLink(this.getClass(),
							mainframe, devname), "*IDN?");
		} catch (Exception e) {
			logger.error("Error in send_and_receive().");
			return Response.status(Status.NOT_FOUND).build();
		}
		answer = answer.replace("\n", "");
		return Response.ok(answer).build();
	}

	@POST
	@Path("{mainframe}/{devname}/devices")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces(MediaType.APPLICATION_JSON)
	public Response listDevices(@Context UriInfo uriInfo,
			@PathParam("mainframe") String mainframe,
			@PathParam("devname") String devname) {
		logger.debug("Incoming URI : {}", uriInfo.getPath());
		logger.debug("Mainframe: {}", mainframe);
		logger.debug("Device name: {}", devname);

		try {
			connManager = ConnectionManager.getInstance(this.getClass(),
					mainframe, devname);
		} catch (Exception e) {
			logger.error(
					"Cannot get wrapper instance. This is usually an initialization problem.");
			return Response.status(Status.NOT_FOUND).build();
		}

		boolean fakeResult = false;
		List<VXIDevice> devices = new ArrayList<>();
		try {
			devices = getDevice(mainframe, devname).listDevices(fakeResult);
		} catch (Exception e) {
			logger.error("Error accessing device.");
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
		for (VXIDevice d : devices) {
			logger.debug("{}", d.toString());
		}
		return Response.ok(devices).build();
	}
	
	//[{"name":"hp1301","type":"mainframe","URL":"/hp1300/mfb/hp1301","mainframe":"mfb"},{"name":"hp1300pacer","type":"pacer","URL":"/hp1300pacer/mfb/hp1300pacer","mainframe":"mfb"},{"name":"hp1326","type":"multimeter","URL":"/hp1326/mfb/hp1326","mainframe":"mfb"},{"name":"hp1333","type":"counter","URL":"/hp1333/mfb/hp1333","mainframe":"mfb"},{"name":"hp1351","type":"switch","URL":"/hp1351/mfb/hp1351","mainframe":"mfb"},{"name":"hp1340","type":"afg","URL":"/hp1340/mfb/hp1340","mainframe":"mfb"},{"name":"hp1330","type":"digitalIO","URL":"/hp1330/mfb/hp1330","mainframe":"mfb"},{"name":"hp1411","type":"multimeter","URL":"/hp1326/mfc/hp1411","mainframe":"mfc"},{"name":"hp1330","type":"digitalIO","URL":"/hp1330/mfc/hp1330","mainframe":"mfc"}]
	@POST
	@Path("{mainframe}/{devname}/Fakedevices")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces(MediaType.APPLICATION_JSON)
	public Response listDevicesFake(@Context UriInfo uriInfo,
			@PathParam("mainframe") String mainframe,
			@PathParam("devname") String devname) {
		logger.debug("Incoming URI : {}", uriInfo.getPath());
		logger.debug("Mainframe: {}", mainframe);
		logger.debug("Device name: {}", devname);

		try {
			connManager = ConnectionManager.getInstance(this.getClass(),
					mainframe, devname);
		} catch (Exception e) {
			logger.error(
					"Cannot get wrapper instance. This is usually an initialization problem.");
			return Response.status(Status.NOT_FOUND).build();
		}

		boolean fakeResult = true;
		List<VXIDevice> devices = new ArrayList<>();
		try {
			devices = getDevice(mainframe, devname).listDevices(fakeResult);
		} catch (Exception e) {
			logger.error("Error accessing device.");
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
		for (VXIDevice d : devices) {
			logger.debug("{}", d.toString());
		}
		return Response.ok(devices).build();
	}

}