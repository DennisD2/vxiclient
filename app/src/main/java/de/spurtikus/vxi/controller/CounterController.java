package de.spurtikus.vxi.controller;


import de.spurtikus.devices.hp.HP1333;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.core.Response;

/**
 * Controller for counters.
 * <p>
 * Tested with: * HP1333 Counter. See class {HP1333}.
 *
 * @author dennis
 */
@RestController
@RequestMapping(Constants.SERVICE_ROOT + Constants.URL_COUNTER)
public class CounterController extends AbstractController<HP1333> {
    private static final String MSG_NO_WRAPPER = "Cannot get wrapper instance. This is usually an initialization problem.";
    private Logger logger = LoggerFactory.getLogger(CounterController.class);

    public CounterController() {
        className = Constants.URL_COUNTER;
    }

    @GetMapping("{mainframe}/{devname}/info")
    public String info(
            @PathVariable("mainframe") String mainframe,
            @PathVariable("devname") String devname) {

        logger.debug("Mainframe: {}", mainframe);
        logger.debug("Device name: {}", devname);
        return getClassName();
    }

    @PostMapping("{mainframe}/{devname}/idn")
    //@Consumes({ MediaType.APPLICATION_JSON })
    //@Produces(MediaType.APPLICATION_JSON)
    public String idn(
            @PathVariable("mainframe") String mainframe,
            @PathVariable("devname") String devname) {
        logger.debug("Mainframe: {}", mainframe);
        logger.debug("Device name: {}", devname);

        try {
            connManager = ConnectionManager.getInstance(this.getClass(),
                    mainframe, devname);
        } catch (Exception e) {
            logger.error(MSG_NO_WRAPPER);
            return Response.Status.NOT_FOUND.getReasonPhrase();
        }

        String answer;
        try {
            answer = connManager
                    .getConnector(this.getClass(), mainframe, devname)
                    .send_and_receive(connManager.getLink(this.getClass(),
                            mainframe, devname), "*IDN?");
        } catch (Exception e) {
            logger.error("Error in send_and_receive().");
            return Response.Status.NOT_FOUND.getReasonPhrase();
        }
        System.out.println(answer);

        return answer;
    }

    /**
     * Configure a channel.
     *
     * @param mainframe
     * @param devname
     * @param channel   1,2 or 3.
     * @param mode      See {CounterConfiguration}. Mode can be FREQ, NWID, PER, RAT,
     *                  TINT, TOT.
     * @return
     */
    @PostMapping("{mainframe}/{devname}/configure/{channel}/{mode}")
    //@Consumes({ MediaType.APPLICATION_JSON })
    //@Produces(MediaType.APPLICATION_JSON)
    public String configure(
            @PathVariable("mainframe") String mainframe,
            @PathVariable("devname") String devname,
            @PathVariable("channel") Integer channel,
            @PathVariable("mode") String mode) {

        logger.debug("Mainframe: {}", mainframe);
        logger.debug("Device name: {}", devname);
        logger.debug("channel: {}", channel);
        logger.debug("mode: {}", mode);

        try {
            connManager = ConnectionManager.getInstance(this.getClass(),
                    mainframe, devname);
        } catch (Exception e) {
            logger.error(MSG_NO_WRAPPER);
            return Response.Status.NOT_FOUND.getReasonPhrase();
        }

        HP1333.CounterConfiguration c = null;
        switch (mode.toLowerCase()) {
            case "nwid":
                c = HP1333.CounterConfiguration.NWID;
                break;
            case "per":
                c = HP1333.CounterConfiguration.PER;
                break;
            case "rat":
                c = HP1333.CounterConfiguration.RAT;
                break;
            case "tint":
                c = HP1333.CounterConfiguration.TINT;
                break;
            case "tot":
                c = HP1333.CounterConfiguration.TOT;
                break;
            case "freq":
            default:
                c = HP1333.CounterConfiguration.FREQ;
                break;
        }

        try {
            getDevice(mainframe, devname).initialize();
            getDevice(mainframe, devname).configureSense(channel, c);
        } catch (Exception e) {
            logger.error("Error accessing device.");
            return Response.Status.INTERNAL_SERVER_ERROR.getReasonPhrase();
        }
        return Constants.RETURNVAL_OK;
    }

    /**
     * Set coupling.
     *
     * @param mainframe
     * @param devname
     * @param coupling  "AC" or "DC".
     * @return
     */
    @PostMapping("{mainframe}/{devname}/coupling/{coupling}")
    //@Consumes({ MediaType.APPLICATION_JSON })
    //@Produces(MediaType.APPLICATION_JSON)
    public String setCoupling(
            @PathVariable("mainframe") String mainframe,
            @PathVariable("devname") String devname,
            @PathVariable("coupling") String coupling) {

        logger.debug("Mainframe: {}", mainframe);
        logger.debug("Device name: {}", devname);
        logger.debug("coupling: {}", coupling);

        try {
            connManager = ConnectionManager.getInstance(this.getClass(),
                    mainframe, devname);
        } catch (Exception e) {
            logger.error(MSG_NO_WRAPPER);
            return Response.Status.NOT_FOUND.getReasonPhrase();
        }

        HP1333.Coupling c = coupling.toLowerCase().equals("ac") ? HP1333.Coupling.AC
                : HP1333.Coupling.DC;
        try {
            getDevice(mainframe, devname).setCoupling(c);
        } catch (Exception e) {
            logger.error("Error accessing device.");
            return Response.Status.INTERNAL_SERVER_ERROR.getReasonPhrase();
        }
        return Constants.RETURNVAL_OK;
    }

    /**
     * Switch Low Pass Filter on or off.
     *
     * @param mainframe
     * @param devname
     * @param onoff     true means low pass filter switch on.
     * @return
     */
    @PostMapping("{mainframe}/{devname}/lowPassFilter/{onoff}")
    //@Consumes({ MediaType.APPLICATION_JSON })
    //@Produces(MediaType.APPLICATION_JSON)
    public String setLowPassFilter(
            @PathVariable("mainframe") String mainframe,
            @PathVariable("devname") String devname,
            @PathVariable("onoff") Boolean onoff) {

        logger.debug("Mainframe: {}", mainframe);
        logger.debug("Device name: {}", devname);
        logger.debug("onoff: {}", onoff);

        try {
            connManager = ConnectionManager.getInstance(this.getClass(),
                    mainframe, devname);
        } catch (Exception e) {
            logger.error(MSG_NO_WRAPPER);
            return Response.Status.NOT_FOUND.getReasonPhrase();
        }

        try {
            getDevice(mainframe, devname).setLowPassFilter(onoff);
        } catch (Exception e) {
            logger.error("Error accessing device.");
            return Response.Status.INTERNAL_SERVER_ERROR.getReasonPhrase();
        }
        return Constants.RETURNVAL_OK;
    }

    /**
     * Set impedance.
     *
     * @param mainframe
     * @param devname
     * @param impedance "MIN" or "MAX". Device allows also a "DEF <number>" but the
     *                  boundary does not implement that. MIN is often 50 Ohms, MAX is
     *                  often >= 1MOhms.
     * @return
     */
    @PostMapping("{mainframe}/{devname}/impedance/{impedance}")
    //@Consumes({ MediaType.APPLICATION_JSON })
    //@Produces(MediaType.APPLICATION_JSON)
    public String setImpedance(
            @PathVariable("mainframe") String mainframe,
            @PathVariable("devname") String devname,
            @PathVariable("impedance") String impedance) {

        logger.debug("Mainframe: {}", mainframe);
        logger.debug("Device name: {}", devname);
        logger.debug("impedance: {}", impedance);

        try {
            connManager = ConnectionManager.getInstance(this.getClass(),
                    mainframe, devname);
        } catch (Exception e) {
            logger.error(MSG_NO_WRAPPER);
            return Response.Status.NOT_FOUND.getReasonPhrase();
        }

        HP1333.Impedance i = (impedance.toLowerCase().equals("min")) ? HP1333.Impedance.MIN
                : HP1333.Impedance.MAX;
        try {
            getDevice(mainframe, devname).setImpedance(i);
        } catch (Exception e) {
            logger.error("Error accessing device.");
            return Response.Status.INTERNAL_SERVER_ERROR.getReasonPhrase();
        }
        return Constants.RETURNVAL_OK;
    }

    /**
     * Set aperture (gate time) for a channel.
     *
     * @param mainframe
     * @param devname
     * @param channel   1,2 or 3.
     * @param aperture  Aperture float value.
     * @return
     */
    @PostMapping("{mainframe}/{devname}/aperture/{channel}/{aperture}")
    //@Consumes({ MediaType.APPLICATION_JSON })
    //@Produces(MediaType.APPLICATION_JSON)
    public String setAperture(
            @PathVariable("mainframe") String mainframe,
            @PathVariable("devname") String devname,
            @PathVariable("channel") int channel,
            @PathVariable("aperture") Double aperture) {

        logger.debug("Mainframe: {}", mainframe);
        logger.debug("Device name: {}", devname);
        logger.debug("channel: {}", channel);
        logger.debug("aperture: {}", aperture);

        try {
            connManager = ConnectionManager.getInstance(this.getClass(),
                    mainframe, devname);
        } catch (Exception e) {
            logger.error(MSG_NO_WRAPPER);
            return Response.Status.NOT_FOUND.getReasonPhrase();
        }

        try {
            getDevice(mainframe, devname).setAperture(channel, aperture);
        } catch (Exception e) {
            logger.error("Error accessing device.");
            return Response.Status.INTERNAL_SERVER_ERROR.getReasonPhrase();
        }
        return Constants.RETURNVAL_OK;
    }

    /**
     * Set attenuation.
     *
     * @param mainframe
     * @param devname
     * @param atten     "MIN" or "MAX". Device allows also a "DEF <number>" but the
     *                  boundary does not implement that. MIN is no attenuation, MAX
     *                  is 20dB.
     * @return
     */
    @PostMapping("{mainframe}/{devname}/attenuation/{atten}")
    //@Consumes({ MediaType.APPLICATION_JSON })
    //@Produces(MediaType.APPLICATION_JSON)
    public String setAttenuation(
            @PathVariable("mainframe") String mainframe,
            @PathVariable("devname") String devname,
            @PathVariable("atten") String atten) {

        logger.debug("Mainframe: {}", mainframe);
        logger.debug("Device name: {}", devname);
        logger.debug("atten: {}", atten);

        try {
            connManager = ConnectionManager.getInstance(this.getClass(),
                    mainframe, devname);
        } catch (Exception e) {
            logger.error(MSG_NO_WRAPPER);
            return Response.Status.NOT_FOUND.getReasonPhrase();
        }

        HP1333.Attenuation a = (atten.toLowerCase().equals("min")) ? HP1333.Attenuation.MIN
                : HP1333.Attenuation.MAX;
        try {
            getDevice(mainframe, devname).setAttenuation(a);
        } catch (Exception e) {
            logger.error("Error accessing device.");
            return Response.Status.INTERNAL_SERVER_ERROR.getReasonPhrase();
        }
        return Constants.RETURNVAL_OK;
    }

    /**
     * Do a measurement.
     *
     * @param mainframe
     * @param devname
     * @param channel   Channel to use.
     * @return
     */
    @PostMapping("{mainframe}/{devname}/read/{channel}")
    //@Consumes({ MediaType.APPLICATION_JSON })
    //@Produces(MediaType.APPLICATION_JSON)
    public String read(
            @PathVariable("mainframe") String mainframe,
            @PathVariable("devname") String devname,
            @PathVariable("channel") int channel) {

        logger.debug("Mainframe: {}", mainframe);
        logger.debug("Device name: {}", devname);
        logger.debug("channel: {}", channel);

        try {
            connManager = ConnectionManager.getInstance(this.getClass(),
                    mainframe, devname);
        } catch (Exception e) {
            logger.error(MSG_NO_WRAPPER);
            return Response.Status.NOT_FOUND.getReasonPhrase();
        }

        double d;
        try {
            d = getDevice(mainframe, devname).measure(channel);
            logger.debug("Frequency: " + d);
        } catch (Exception e) {
            logger.error("Error accessing device.");
            return Response.Status.INTERNAL_SERVER_ERROR.getReasonPhrase();
        }
        return String.valueOf(d);
    }

}
