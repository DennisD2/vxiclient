package de.spurtikus.vxi.controller;

import de.spurtikus.devices.hp.HP1340;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.core.Response;

/**
 * Controller for frequency generators.
 * <p>
 * Tested with:
 * * HP1340 AFG. See class {HP1340}.
 *
 * @author dennis
 */
@RestController
@RequestMapping("/api/rest/" + Constants.URL_AFG)
public class FrequencyGeneratorController extends AbstractController<HP1340> {
    private static final String MSG_NO_WRAPPER = "Cannot get wrapper instance. This is usually an initialization problem.";
    private Logger logger = LoggerFactory.getLogger(FrequencyGeneratorController.class);

    public FrequencyGeneratorController() {
        className = Constants.URL_AFG;
    }

    @GetMapping("{mainframe}/{devname}/info")
    public String info(@PathVariable("mainframe") String mainframe,
                       @PathVariable("devname") String devname) {
        //logger.debug("Incoming URI : {}", uriInfo.getPath());
        logger.debug("Mainframe: {}", mainframe);
        logger.debug("Device name: {}", devname);
        return getClassName();
    }

    @PostMapping("{mainframe}/{devname}/idn")
    public String idn(@PathVariable("mainframe") String mainframe,
                      @PathVariable("devname") String devname) {
        //logger.debug("Incoming URI : {}", uriInfo.getPath());
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
     * Initialize device.
     *
     * @param mainframe Mainframe to use. Comes from vxiserver.properties, e.g. "mfb".
     * @param devname   Device to use.
     * @return
     */
    @PostMapping("{mainframe}/{devname}/initialize")
    public String initialize(@PathVariable("mainframe") String mainframe,
                             @PathVariable("devname") String devname) {
        //logger.debug("Incoming URI : {}", uriInfo.getPath());
        logger.debug("Mainframe: {}", mainframe);
        logger.debug("Device name: {}", devname);

        try {
            connManager = ConnectionManager.getInstance(this.getClass(),
                    mainframe, devname);
        } catch (Exception e) {
            logger.error(MSG_NO_WRAPPER);
            return Response.Status.NOT_FOUND.getReasonPhrase();
        }

        try {
            getDevice(mainframe, devname).initialize();
        } catch (Exception e) {
            logger.error("Error accessing device.");
            return Response.Status.INTERNAL_SERVER_ERROR.getReasonPhrase();
        }
        return "done";
    }

    /**
     * Get configuration.
     *
     * @param mainframe Mainframe to use. Comes from vxiserver.properties, e.g. "mfb".
     * @param devname   Device to use.
     * @return
     */
    @PostMapping("{mainframe}/{devname}/getConfiguration")
    //@Consumes({ MediaType.APPLICATION_JSON })
    //@Produces(MediaType.APPLICATION_JSON)
    public String getConfiguration(@PathVariable("mainframe") String mainframe,
                                   @PathVariable("devname") String devname) {
        //logger.debug("Incoming URI : {}", uriInfo.getPath());
        logger.debug("Mainframe: {}", mainframe);
        logger.debug("Device name: {}", devname);

        try {
            connManager = ConnectionManager.getInstance(this.getClass(),
                    mainframe, devname);
        } catch (Exception e) {
            logger.error(MSG_NO_WRAPPER);
            return Response.Status.NOT_FOUND.getReasonPhrase();
        }
        String answer = "";
        try {
            answer = getDevice(mainframe, devname).getConfig();
        } catch (Exception e) {
            logger.error("Error accessing device.");
            return Response.Status.INTERNAL_SERVER_ERROR.getReasonPhrase();
        }
        return answer;
    }

    /**
     * Set amplitude.
     *
     * @param mainframe Mainframe to use. Comes from vxiserver.properties, e.g. "mfb".
     * @param devname   Device to use.
     * @param amplitude Amplitude of waveform. Example '5.0' for 5,0 Volts.
     * @return
     */
    @PostMapping("{mainframe}/{devname}/setAmplitude/{amplitude}")
    //@Consumes({ MediaType.APPLICATION_JSON })
    //@Produces(MediaType.APPLICATION_JSON)
    public String setAmplitude(@PathVariable("mainframe") String mainframe,
                               @PathVariable("devname") String devname,
                               @PathVariable("amplitude") Double amplitude) {
        //logger.debug("Incoming URI : {}", uriInfo.getPath());
        logger.debug("Mainframe: {}", mainframe);
        logger.debug("Device name: {}", devname);
        logger.debug("Amplitude: {}", amplitude);

        try {
            connManager = ConnectionManager.getInstance(this.getClass(),
                    mainframe, devname);
        } catch (Exception e) {
            logger.error(MSG_NO_WRAPPER);
            return Response.Status.NOT_FOUND.getReasonPhrase();
        }

        try {
            getDevice(mainframe, devname).stop();
            getDevice(mainframe, devname).setAmplitude(amplitude);
            getDevice(mainframe, devname).start();
        } catch (Exception e) {
            logger.error("Error accessing device.");
            return Response.Status.INTERNAL_SERVER_ERROR.getReasonPhrase();
        }
        return "done";
    }

    /**
     * Set offset.
     *
     * @param mainframe Mainframe to use. Comes from vxiserver.properties, e.g. "mfb".
     * @param devname   Device to use.
     * @param offset    Offset of waveform. Example '5.0' for 5,0 Volts.
     * @return
     */
    @PostMapping("{mainframe}/{devname}/setOffset/{offset}")
    //@Consumes({ MediaType.APPLICATION_JSON })
    //@Produces(MediaType.APPLICATION_JSON)
    public String setOffset(@PathVariable("mainframe") String mainframe,
                            @PathVariable("devname") String devname,
                            @PathVariable("offset") Double offset) {
        //logger.debug("Incoming URI : {}", uriInfo.getPath());
        logger.debug("Mainframe: {}", mainframe);
        logger.debug("Device name: {}", devname);
        logger.debug("offset: {}", offset);

        try {
            connManager = ConnectionManager.getInstance(this.getClass(),
                    mainframe, devname);
        } catch (Exception e) {
            logger.error(MSG_NO_WRAPPER);
            return Response.Status.NOT_FOUND.getReasonPhrase();
        }

        try {
            getDevice(mainframe, devname).stop();
            getDevice(mainframe, devname).setOffset(offset);
            getDevice(mainframe, devname).start();
        } catch (Exception e) {
            logger.error("Error accessing device.");
            return Response.Status.INTERNAL_SERVER_ERROR.getReasonPhrase();
        }
        return "done";
    }

    /**
     * Set frequency.
     *
     * @param mainframe Mainframe to use. Comes from vxiserver.properties, e.g. "mfb".
     * @param devname   Device to use.
     * @param frequency Frequency of waveform. Example '5e6' for 5MHz and '44000' for
     *                  44KHz.
     * @return
     */
    @PostMapping("{mainframe}/{devname}/setFrequency/{frequency}")
    //@Consumes({ MediaType.APPLICATION_JSON })
    //@Produces(MediaType.APPLICATION_JSON)
    public String setFrequency(@PathVariable("mainframe") String mainframe,
                               @PathVariable("devname") String devname,
                               @PathVariable("frequency") Double frequency) {
        //logger.debug("Incoming URI : {}", uriInfo.getPath());
        logger.debug("Mainframe: {}", mainframe);
        logger.debug("Device name: {}", devname);
        logger.debug("Frequency: {}", frequency);

        try {
            connManager = ConnectionManager.getInstance(this.getClass(),
                    mainframe, devname);
        } catch (Exception e) {
            logger.error(MSG_NO_WRAPPER);
            return Response.Status.NOT_FOUND.getReasonPhrase();
        }

        try {
            getDevice(mainframe, devname).stop();
            getDevice(mainframe, devname).setFrequency(frequency);
            getDevice(mainframe, devname).start();
        } catch (Exception e) {
            logger.error("Error accessing device.");
            return Response.Status.INTERNAL_SERVER_ERROR.getReasonPhrase();
        }
        return "done";
    }

    /**
     * Set shape of StandardWaveForm type.
     *
     * @param mainframe Mainframe to use. Comes from vxiserver.properties, e.g. "mfb".
     * @param devname   Device to use.
     * @param waveform  Shape of waveform. Example "ramp" or "sin". See
     *                  {HP1340.StandardWaveForm}.
     * @return
     */
    @PostMapping("{mainframe}/{devname}/setShape/standard/{waveform}")
    //@Consumes({ MediaType.APPLICATION_JSON })
    //@Produces(MediaType.APPLICATION_JSON)
    public String setStandardShape(@PathVariable("mainframe") String mainframe,
                                   @PathVariable("devname") String devname,
                                   @PathVariable("waveform") String waveform) {
        //logger.debug("Incoming URI : {}", uriInfo.getPath());
        logger.debug("Mainframe: {}", mainframe);
        logger.debug("Device name: {}", devname);
        logger.debug("waveform: {}", waveform);

        try {
            connManager = ConnectionManager.getInstance(this.getClass(),
                    mainframe, devname);
        } catch (Exception e) {
            logger.error(MSG_NO_WRAPPER);
            return Response.Status.NOT_FOUND.getReasonPhrase();
        }

        HP1340.StandardWaveForm wv = toStandardWaveForm(waveform);
        // @formatter:on
        try {
            getDevice(mainframe, devname).stop();
            getDevice(mainframe, devname).setShape(wv);
            getDevice(mainframe, devname).start();
        } catch (Exception e) {
            logger.error("Error accessing device.");
            return Response.Status.INTERNAL_SERVER_ERROR.getReasonPhrase();
        }
        return wv.getValue();
    }

    /**
     * Set shape of BuiltinWaveForm type.
     *
     * @param mainframe Mainframe to use. Comes from vxiserver.properties, e.g. "mfb".
     * @param devname   Device to use.
     * @param waveform  Shape of waveform. Example "sine" or "haversine". See
     *                  {HP1340.BuiltinWaveForm}.
     * @param segment   Target segment ('A'..'D'). {HP1340.StandardWaveForm}.
     * @return
     */
    @PostMapping("{mainframe}/{devname}/setShape/builtin/{waveform}/{segment}")
    //@Consumes({ MediaType.APPLICATION_JSON })
    //@Produces(MediaType.APPLICATION_JSON)
    public String setBuiltinShape(@PathVariable("mainframe") String mainframe,
                                  @PathVariable("devname") String devname,
                                  @PathVariable("waveform") String waveform,
                                  @PathVariable("segment") char segment) {
        //logger.debug("Incoming URI : {}", uriInfo.getPath());
        logger.debug("Mainframe: {}", mainframe);
        logger.debug("Device name: {}", devname);
        logger.debug("segment: {}", segment);

        try {
            connManager = ConnectionManager.getInstance(this.getClass(),
                    mainframe, devname);
        } catch (Exception e) {
            logger.error(MSG_NO_WRAPPER);
            return Response.Status.NOT_FOUND.getReasonPhrase();
        }

        HP1340.BuiltinWaveForm wv = toBuiltinWaveForm(waveform);
        try {
            getDevice(mainframe, devname).stop();
            getDevice(mainframe, devname).setShape(wv, segment);
            getDevice(mainframe, devname).start();
        } catch (Exception e) {
            logger.error("Error accessing device.");
            return Response.Status.INTERNAL_SERVER_ERROR.getReasonPhrase();
        }
        return wv.getValue();
    }

    /**
     * Set sweep.
     *
     * @param mainframe      Mainframe to use. Comes from vxiserver.properties, e.g. "mfb".
     * @param devname        Device to use.
     * @param startFrequency Start frequency.
     * @param stopFrequency  Stop frequency.
     * @param points         number of points in sweep.
     * @param duration       sweep duration in seconds.
     * @param amplitude      sweep amplitude in volts.
     * @param waveform       Shape of waveform. Example "sine" or "haversine". See
     *                       {HP1340.BuiltinWaveForm}.
     * @return
     */
    @PostMapping("{mainframe}/{devname}/setSweep/{start}/{stop}/{points}/{duration}/{amplitude}/{waveform}")
    //@Consumes({ MediaType.APPLICATION_JSON })
    //@Produces(MediaType.APPLICATION_JSON)
    public String setSweep(@PathVariable("mainframe") String mainframe,
                           @PathVariable("devname") String devname,
                           @PathVariable("start") double startFrequency,
                           @PathVariable("stop") double stopFrequency,
                           @PathVariable("points") int points,
                           @PathVariable("duration") double duration,
                           @PathVariable("amplitude") double amplitude,
                           @PathVariable("waveform") String waveform) {
        //logger.debug("Incoming URI : {}", uriInfo.getPath());
        logger.debug("Mainframe: {}", mainframe);
        logger.debug("Device name: {}", devname);
        logger.debug("startFrequency: {}", startFrequency);
        logger.debug("stopFrequency: {}", stopFrequency);
        logger.debug("Points: {}", points);
        logger.debug("Duration: {}", duration);
        logger.debug("Amplitude: {}", amplitude);
        logger.debug("waveform: {}", waveform);

        try {
            connManager = ConnectionManager.getInstance(this.getClass(),
                    mainframe, devname);
        } catch (Exception e) {
            logger.error(MSG_NO_WRAPPER);
            return Response.Status.NOT_FOUND.getReasonPhrase();
        }

        HP1340.StandardWaveForm wv = toStandardWaveForm(waveform);
        try {
            getDevice(mainframe, devname).stop();
            getDevice(mainframe, devname).setSweep(startFrequency,
                    stopFrequency, points, duration, amplitude, wv);
            getDevice(mainframe, devname).start();
        } catch (Exception e) {
            logger.error("Error accessing device.");
            return Response.Status.INTERNAL_SERVER_ERROR.getReasonPhrase();
        }
        return wv.getValue();
    }

    /**
     * Set marker.
     *
     * @param mainframe Mainframe to use. Comes from vxiserver.properties, e.g. "mfb".
     * @param devname   Device to use.
     * @param source    Marker source.
     * @param polarity  Marker polarity.
     * @return
     */
    @PostMapping("{mainframe}/{devname}/setMarker/{source}/{polarity}")
    //@Consumes({ MediaType.APPLICATION_JSON })
    //@Produces(MediaType.APPLICATION_JSON)
    public String setMarker(@PathVariable("mainframe") String mainframe,
                            @PathVariable("devname") String devname,
                            @PathVariable("source") String source,
                            @PathVariable("polarity") String polarity) {
        //logger.debug("Incoming URI : {}", uriInfo.getPath());
        logger.debug("Mainframe: {}", mainframe);
        logger.debug("Device name: {}", devname);
        logger.debug("source: {}", source);
        logger.debug("polarity: {}", polarity);

        try {
            connManager = ConnectionManager.getInstance(this.getClass(),
                    mainframe, devname);
        } catch (Exception e) {
            logger.error(MSG_NO_WRAPPER);
            return Response.Status.NOT_FOUND.getReasonPhrase();
        }

        HP1340.MarkerFeedType mf;
        // @formatter:off
        switch (source.toLowerCase()) {
            case "segm":
                mf = HP1340.MarkerFeedType.SEGMENT;
                break;
            case "sour_rosc":
                mf = HP1340.MarkerFeedType.SOURCE_ROSC;
                break;
            case "sour_sweep":
                mf = HP1340.MarkerFeedType.SOURCE_SWEEP;
                break;
            case "outp_zero":
            default:
                mf = HP1340.MarkerFeedType.OUTPUT_ZERO;
                break;
        }
        HP1340.PolarityType pol;
        switch (polarity.toLowerCase()) {
            case "inv":
                pol = HP1340.PolarityType.INV;
                break;
            case "norm":
            default:
                pol = HP1340.PolarityType.NORM;
                break;
        }
        // @formatter:on
        try {
            getDevice(mainframe, devname).stop();
            getDevice(mainframe, devname).setMarker(mf, pol);
            getDevice(mainframe, devname).start();
        } catch (Exception e) {
            logger.error("Error accessing device.");
            return Response.Status.INTERNAL_SERVER_ERROR.getReasonPhrase();
        }
        return mf.getValue();
    }

    /**
     * Converts a string to a StandardWaveForm enum value.
     *
     * @param waveform string to convert.
     * @return StandardWaveForm enum value.
     */
    protected HP1340.StandardWaveForm toStandardWaveForm(String waveform) {
        HP1340.StandardWaveForm wv;
        // @formatter:off
        switch (waveform.toLowerCase()) {
            case "dc":
                wv = HP1340.StandardWaveForm.DC;
                break;
            case "ramp":
                wv = HP1340.StandardWaveForm.RAMP;
                break;
            case "sine":
                wv = HP1340.StandardWaveForm.SINE;
                break;
            case "triangle":
                wv = HP1340.StandardWaveForm.TRIANGLE;
                break;
            case "usera":
                wv = HP1340.StandardWaveForm.USERA;
                break;
            case "userb":
                wv = HP1340.StandardWaveForm.USERB;
                break;
            case "userc":
                wv = HP1340.StandardWaveForm.USERC;
                break;
            case "userd":
                wv = HP1340.StandardWaveForm.USERD;
                break;
            case "square":
            default:
                wv = HP1340.StandardWaveForm.SQUARE;
                break;
        }
        // @formatter:on
        return wv;
    }

    /**
     * Converts a string to a BuiltinWaveForm enum value.
     *
     * @param waveform string to convert.
     * @return BuiltinWaveForm enum value.
     */
    protected HP1340.BuiltinWaveForm toBuiltinWaveForm(String waveform) {
        HP1340.BuiltinWaveForm wv;
        // @formatter:off
        switch (waveform.toLowerCase()) {
            case "haversine":
                wv = HP1340.BuiltinWaveForm.Haversine;
                break;
            case "ramp_falling":
                wv = HP1340.BuiltinWaveForm.Ramp_Falling;
                break;
            case "ramp_falling_first_20":
                wv = HP1340.BuiltinWaveForm.Ramp_Falling_First_20;
                break;
            case "ramp_rising":
                wv = HP1340.BuiltinWaveForm.Ramp_Rising;
                break;
            case "ramp_rising_first_20":
                wv = HP1340.BuiltinWaveForm.Ramp_Rising_First_20;
                break;
            case "sine":
                wv = HP1340.BuiltinWaveForm.Sine;
                break;
            case "sine_linear_rising_8_cycles":
                wv = HP1340.BuiltinWaveForm.Sine_Linear_Rising_8_cycles;
                break;
            case "sine_positive_half_cycle":
                wv = HP1340.BuiltinWaveForm.Sine_Positive_Half_Cycle;
                break;
            case "sinx_per_x":
                wv = HP1340.BuiltinWaveForm.Sinx_per_x;
                break;
            case "square":
                wv = HP1340.BuiltinWaveForm.Square;
                break;
            case "square_first_10":
                wv = HP1340.BuiltinWaveForm.Square_First_10;
                break;
            case "square_first_4":
                wv = HP1340.BuiltinWaveForm.Square_First_4;
                break;
            case "triangle":
                wv = HP1340.BuiltinWaveForm.Triangle;
                break;
            case "white_noise":
                wv = HP1340.BuiltinWaveForm.White_Noise;
                break;
            case "white_noise_modulated":
                wv = HP1340.BuiltinWaveForm.White_Noise_Modulated;
                break;
            case "harmonic":
            default:
                wv = HP1340.BuiltinWaveForm.Harmonic_Chord_3rd_4th_5th;
                break;
        }
        // @formatter:on
        return wv;
    }
}

