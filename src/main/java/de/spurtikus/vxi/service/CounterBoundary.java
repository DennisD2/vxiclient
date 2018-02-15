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

import de.spurtikus.devices.hp.HP1333;
import de.spurtikus.devices.hp.HP1333.Attenuation;
import de.spurtikus.devices.hp.HP1333.CounterConfiguration;
import de.spurtikus.devices.hp.HP1333.Coupling;
import de.spurtikus.devices.hp.HP1333.Impedance;
import de.spurtikus.vxi.Constants;

/**
 * Boundary for counters.
 * 
 * Tested with: * HP1333 Counter. See class {HP1333}.
 * 
 * @author dennis
 *
 */
@Path("/" + Constants.URL_COUNTER)
public class CounterBoundary extends AbstractBoundary<HP1333> {
	private static final String MSG_NO_WRAPPER = "Cannot get wrapper instance. This is usually an initialization problem.";
	private Logger logger = LoggerFactory.getLogger(CounterBoundary.class);

	public CounterBoundary() {
		className = Constants.URL_COUNTER;
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
			logger.error(MSG_NO_WRAPPER);
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
	 * Configure a channel.
	 * 
	 * @param uriInfo
	 * @param mainframe
	 * @param devname
	 * @param channel
	 *            1,2 or 3.
	 * @param mode
	 *            See {CounterConfiguration}. Mode can be FREQ, NWID, PER, RAT,
	 *            TINT, TOT.
	 * @return
	 */
	@POST
	@Path("{mainframe}/{devname}/configure/{channel}/{mode}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces(MediaType.APPLICATION_JSON)
	public Response configure(@Context UriInfo uriInfo,
			@PathParam("mainframe") String mainframe,
			@PathParam("devname") String devname,
			@PathParam("channel") Integer channel,
			@PathParam("mode") String mode) {
		logger.debug("Incoming URI : {}", uriInfo.getPath());
		logger.debug("Mainframe: {}", mainframe);
		logger.debug("Device name: {}", devname);
		logger.debug("channel: {}", channel);
		logger.debug("mode: {}", mode);

		try {
			connManager = ConnectionManager.getInstance(this.getClass(),
					mainframe, devname);
		} catch (Exception e) {
			logger.error(MSG_NO_WRAPPER);
			return Response.status(Status.NOT_FOUND).build();
		}

		CounterConfiguration c = null;
		switch (mode.toLowerCase()) {
		case "freq":
			c = CounterConfiguration.FREQ;
			break;
		case "nwid":
			c = CounterConfiguration.NWID;
			break;
		case "per":
			c = CounterConfiguration.PER;
			break;
		case "rat":
			c = CounterConfiguration.RAT;
			break;
		case "tint":
			c = CounterConfiguration.TINT;
			break;
		case "tot":
			c = CounterConfiguration.TOT;
			break;
		default:
			c = CounterConfiguration.FREQ;
			break;
		}

		try {
			getDevice(mainframe, devname).initialize();
			getDevice(mainframe, devname).configureSense(channel, c);
		} catch (Exception e) {
			logger.error("Error accessing device.");
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
		return Response.ok("done").build();
	}

	/**
	 * Set coupling.
	 * 
	 * @param uriInfo
	 * @param mainframe
	 * @param devname
	 * @param channel
	 *            1,2 or 3.
	 * @param coupling
	 *            "AC" or "DC".
	 * @return
	 */
	@POST
	@Path("{mainframe}/{devname}/coupling/{coupling}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces(MediaType.APPLICATION_JSON)
	public Response setCoupling(@Context UriInfo uriInfo,
			@PathParam("mainframe") String mainframe,
			@PathParam("devname") String devname,
			@PathParam("coupling") String coupling) {
		logger.debug("Incoming URI : {}", uriInfo.getPath());
		logger.debug("Mainframe: {}", mainframe);
		logger.debug("Device name: {}", devname);
		logger.debug("coupling: {}", coupling);

		try {
			connManager = ConnectionManager.getInstance(this.getClass(),
					mainframe, devname);
		} catch (Exception e) {
			logger.error(MSG_NO_WRAPPER);
			return Response.status(Status.NOT_FOUND).build();
		}

		Coupling c = coupling.toLowerCase().equals("ac") ? Coupling.AC
				: Coupling.DC;
		try {
			getDevice(mainframe, devname).setCoupling(c);
		} catch (Exception e) {
			logger.error("Error accessing device.");
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
		return Response.ok("done").build();
	}

	/**
	 * Switch Low Pass Filter on or off.
	 * 
	 * @param uriInfo
	 * @param mainframe
	 * @param devname
	 * @param onoff
	 *            true means low pass filter switch on.
	 * @return
	 */
	@POST
	@Path("{mainframe}/{devname}/lowPassFilter/{onoff}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces(MediaType.APPLICATION_JSON)
	public Response setLowPassFilter(@Context UriInfo uriInfo,
			@PathParam("mainframe") String mainframe,
			@PathParam("devname") String devname,
			@PathParam("onoff") Boolean onoff) {
		logger.debug("Incoming URI : {}", uriInfo.getPath());
		logger.debug("Mainframe: {}", mainframe);
		logger.debug("Device name: {}", devname);
		logger.debug("onoff: {}", onoff);

		try {
			connManager = ConnectionManager.getInstance(this.getClass(),
					mainframe, devname);
		} catch (Exception e) {
			logger.error(MSG_NO_WRAPPER);
			return Response.status(Status.NOT_FOUND).build();
		}

		try {
			getDevice(mainframe, devname).setLowPassFilter(onoff);
		} catch (Exception e) {
			logger.error("Error accessing device.");
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
		return Response.ok("done").build();
	}

	/**
	 * Set impedance.
	 * 
	 * @param uriInfo
	 * @param mainframe
	 * @param devname
	 * @param impedance
	 *            "MIN" or "MAX". Device allows also a "DEF <number>" but the
	 *            boundary does not implement that. MIN is often 50 Ohms, MAX is
	 *            often >= 1MOhms.
	 * @return
	 */
	@POST
	@Path("{mainframe}/{devname}/impedance/{impedance}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces(MediaType.APPLICATION_JSON)
	public Response setImpedance(@Context UriInfo uriInfo,
			@PathParam("mainframe") String mainframe,
			@PathParam("devname") String devname,
			@PathParam("impedance") String impedance) {
		logger.debug("Incoming URI : {}", uriInfo.getPath());
		logger.debug("Mainframe: {}", mainframe);
		logger.debug("Device name: {}", devname);
		logger.debug("impedance: {}", impedance);

		try {
			connManager = ConnectionManager.getInstance(this.getClass(),
					mainframe, devname);
		} catch (Exception e) {
			logger.error(MSG_NO_WRAPPER);
			return Response.status(Status.NOT_FOUND).build();
		}

		Impedance i = (impedance.toLowerCase().equals("min")) ? Impedance.MIN
				: Impedance.MAX;
		try {
			getDevice(mainframe, devname).setImpedance(i);
		} catch (Exception e) {
			logger.error("Error accessing device.");
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
		return Response.ok("done").build();
	}

	/**
	 * Set aperture (gate time) for a channel.
	 * 
	 * @param uriInfo
	 * @param mainframe
	 * @param devname
	 * @param channel
	 *            1,2 or 3.
	 * @param aperture
	 *            Aperture float value.
	 * @return
	 */
	@POST
	@Path("{mainframe}/{devname}/aperture/{channel}/{aperture}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces(MediaType.APPLICATION_JSON)
	public Response setAperture(@Context UriInfo uriInfo,
			@PathParam("mainframe") String mainframe,
			@PathParam("devname") String devname,
			@PathParam("channel") int channel,
			@PathParam("aperture") Double aperture) {
		logger.debug("Incoming URI : {}", uriInfo.getPath());
		logger.debug("Mainframe: {}", mainframe);
		logger.debug("Device name: {}", devname);
		logger.debug("channel: {}", channel);
		logger.debug("aperture: {}", aperture);

		try {
			connManager = ConnectionManager.getInstance(this.getClass(),
					mainframe, devname);
		} catch (Exception e) {
			logger.error(MSG_NO_WRAPPER);
			return Response.status(Status.NOT_FOUND).build();
		}

		try {
			getDevice(mainframe, devname).setAperture(channel, aperture);
		} catch (Exception e) {
			logger.error("Error accessing device.");
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
		return Response.ok("done").build();
	}

	/**
	 * Set attenuation.
	 * 
	 * @param uriInfo
	 * @param mainframe
	 * @param devname
	 * @param atten
	 *            "MIN" or "MAX". Device allows also a "DEF <number>" but the
	 *            boundary does not implement that. MIN is no attenuation, MAX
	 *            is 20dB.
	 * @return
	 */
	@POST
	@Path("{mainframe}/{devname}/attenuation/{atten}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces(MediaType.APPLICATION_JSON)
	public Response setAttenuation(@Context UriInfo uriInfo,
			@PathParam("mainframe") String mainframe,
			@PathParam("devname") String devname,
			@PathParam("atten") String atten) {
		logger.debug("Incoming URI : {}", uriInfo.getPath());
		logger.debug("Mainframe: {}", mainframe);
		logger.debug("Device name: {}", devname);
		logger.debug("atten: {}", atten);

		try {
			connManager = ConnectionManager.getInstance(this.getClass(),
					mainframe, devname);
		} catch (Exception e) {
			logger.error(MSG_NO_WRAPPER);
			return Response.status(Status.NOT_FOUND).build();
		}

		Attenuation a = (atten.toLowerCase().equals("min")) ? Attenuation.MIN
				: Attenuation.MAX;
		try {
			getDevice(mainframe, devname).setAttenuation(a);
		} catch (Exception e) {
			logger.error("Error accessing device.");
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
		return Response.ok("done").build();
	}

	/**
	 * Do a measurement.
	 * 
	 * @param uriInfo
	 * @param mainframe
	 * @param devname
	 * @param channel
	 *            Channel to use.
	 * @return
	 */
	@POST
	@Path("{mainframe}/{devname}/read/{channel}")
	@Consumes({ MediaType.APPLICATION_JSON })
	@Produces(MediaType.APPLICATION_JSON)
	public Response read(@Context UriInfo uriInfo,
			@PathParam("mainframe") String mainframe,
			@PathParam("devname") String devname,
			@PathParam("channel") int channel) {
		logger.debug("Incoming URI : {}", uriInfo.getPath());
		logger.debug("Mainframe: {}", mainframe);
		logger.debug("Device name: {}", devname);
		logger.debug("channel: {}", channel);

		try {
			connManager = ConnectionManager.getInstance(this.getClass(),
					mainframe, devname);
		} catch (Exception e) {
			logger.error(MSG_NO_WRAPPER);
			return Response.status(Status.NOT_FOUND).build();
		}

		double d;
		try {
			d = getDevice(mainframe, devname).measure(channel);
			logger.debug("Frequency: " + d);
		} catch (Exception e) {
			logger.error("Error accessing device.");
			return Response.status(Status.INTERNAL_SERVER_ERROR).build();
		}
		return Response.ok(d).build();
	}

}