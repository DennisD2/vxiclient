package de.spurtikus.vxi.service;

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

import de.spurtikus.devices.hp.DigitalIO;
import de.spurtikus.devices.hp.DigitalIO.Bit;
import de.spurtikus.devices.hp.DigitalIO.Polarity;
import de.spurtikus.devices.hp.DigitalIO.Port;
import de.spurtikus.devices.hp.DigitalIO.PortDescription;

/**
 * Boundary for Digital I/O cards.
 * 
 * Tested with:
 * * HP E1330 Digital I/O control. See class {HP1330}.
 * 
 * @author dennis
 *
 */
@Path("/" + Constants.URL_DIGITALIO)
public class DIOBoundary extends AbstractBoundary<DigitalIO> {
	private Logger logger = LoggerFactory.getLogger(DIOBoundary.class);
	
	public DIOBoundary() {
		className = Constants.URL_DIGITALIO;
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
		System.out.println(answer);

		return Response.ok(answer).build();
	}

	/**
	 * Set a bit in a port.
	 * 
	 * @param uriInfo
	 * @param mainframe
	 * @param devname
	 * @param port
	 *            port to use
	 * @param value
	 *            bit value
	 * @return
	 */
	@POST
	@Path("{mainframe}/{devname}/setBit/{byte}/{bit}/{value}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces(MediaType.APPLICATION_JSON)
	public Response setBit(@Context UriInfo uriInfo,
			@PathParam("mainframe") String mainframe,
			@PathParam("devname") String devname, 
			@PathParam("byte") int bbyte,
			@PathParam("bit") int bit,
			@PathParam("value") boolean value) {
		logger.debug("Incoming URI : {}", uriInfo.getPath());
		logger.debug("Mainframe: {}", mainframe);
		logger.debug("Device name: {}", devname);
		logger.debug("Byte: {}", bbyte);
		logger.debug("Bit: {}", bit);
		logger.debug("Value: {}", value);

		try {
			connManager = ConnectionManager.getInstance(this.getClass(),
					mainframe, devname);
		} catch (Exception e) {
			logger.error(
					"Cannot get wrapper instance. This is usually an initialization problem.");
			return Response.status(Status.NOT_FOUND).build();
		}

		PortDescription pd = getPortDescriptor(bbyte, bit);
		
		Boolean b;
		try {
			b = getDevice(mainframe, devname).setBit(pd, value);
		} catch (Exception e) {
			logger.error("Error accessing device.");
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
		return Response.ok(b).build();
	}

	/**
	 * Get a bit in a port.
	 * 
	 * @param uriInfo
	 * @param mainframe
	 * @param devname
	 * @param port
	 *            port to use
	 * @return
	 */
	@POST
	@Path("{mainframe}/{devname}/getBit/{byte}/{bit}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces(MediaType.APPLICATION_JSON)
	public Response setBit(@Context UriInfo uriInfo,
			@PathParam("mainframe") String mainframe,
			@PathParam("devname") String devname, 
			@PathParam("byte") int bbyte,
			@PathParam("bit") int bit) {
		logger.debug("Incoming URI : {}", uriInfo.getPath());
		logger.debug("Mainframe: {}", mainframe);
		logger.debug("Device name: {}", devname);
		logger.debug("Byte: {}", bbyte);
		logger.debug("Bit: {}", bit);

		try {
			connManager = ConnectionManager.getInstance(this.getClass(),
					mainframe, devname);
		} catch (Exception e) {
			logger.error(
					"Cannot get wrapper instance. This is usually an initialization problem.");
			return Response.status(Status.NOT_FOUND).build();
		}

		PortDescription pd = getPortDescriptor(bbyte, bit);
		boolean b;
		try {
			b = getDevice(mainframe, devname).getBit(pd);
		} catch (Exception e) {
			logger.error("Error accessing device.");
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
		return Response.ok(b).build();
	}

	/**
	 * Set a bit in a port.
	 * 
	 * @param uriInfo
	 * @param mainframe
	 * @param devname
	 * @param port
	 *            port to use
	 * @param value
	 *            bit value
	 * @return
	 */
	@POST
	@Path("{mainframe}/{devname}/setPolarity/{byte}/{bit}/{value}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces(MediaType.APPLICATION_JSON)
	public Response setPolarity(@Context UriInfo uriInfo,
			@PathParam("mainframe") String mainframe,
			@PathParam("devname") String devname, 
			@PathParam("byte") Integer bbyte,
			@PathParam("bit") Integer bit,
			@PathParam("polarity") Boolean polarity) {
		logger.debug("Incoming URI : {}", uriInfo.getPath());
		logger.debug("Mainframe: {}", mainframe);
		logger.debug("Device name: {}", devname);
		logger.debug("Byte: {}", bbyte);
		logger.debug("Bit: {}", bit);
		logger.debug("Polarity: {}", polarity);

		try {
			connManager = ConnectionManager.getInstance(this.getClass(),
					mainframe, devname);
		} catch (Exception e) {
			logger.error(
					"Cannot get wrapper instance. This is usually an initialization problem.");
			return Response.status(Status.NOT_FOUND).build();
		}

		Polarity p = (polarity=true)? Polarity.POS: Polarity.NEG;
		PortDescription pd = getPortDescriptor(bbyte, bit);
		try {
			getDevice(mainframe, devname).setPolarity(pd, p);
		} catch (Exception e) {
			logger.error("Error accessing device.");
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
		return Response.ok(polarity).build();
	}

	/**
	 * Create PortDescription from byte pos and bit pos.
	 * @param bbyte byte position.
	 * @param bit bit position.
	 * @return PortDescription.
	 */
	protected PortDescription getPortDescriptor(int bbyte, int bit) {
		Port p;
		switch(bbyte) {
			case 0: p = Port.DATA0; break;
			case 1: p = Port.DATA1; break;
			case 2: p = Port.DATA2; break;
			case 3: p = Port.DATA3; break;
			default: p = Port.DATA0; break;
		}
		Bit bb;
		switch(bit) {
			case 0: bb = Bit.BIT0; break;
			case 1: bb = Bit.BIT1; break;
			case 2: bb = Bit.BIT2; break;
			case 3: bb = Bit.BIT3; break;
			case 4: bb = Bit.BIT4; break;
			case 5: bb = Bit.BIT5; break;
			case 6: bb = Bit.BIT6; break;
			case 7: bb = Bit.BIT7; break;
			default: bb = Bit.BIT0; break;
		}
		PortDescription pd = new PortDescription(p, bb);
		return pd;
	}

}