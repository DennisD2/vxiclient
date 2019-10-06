package de.spurtikus.vxi.controller;

import de.spurtikus.devices.hp.HP1300Pacer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.core.Response;

/**
 * Controller for Pacer functionality built in in a mainframe.
 * <p>
 * Tested with:
 * * HP 1300/1301. See class {HP1300Pacer}.
 *
 * @author dennis
 */

@RestController
@RequestMapping(Constants.SERVICE_ROOT + Constants.URL_PACER)
public class PacerController extends AbstractController<HP1300Pacer> {
    private Logger logger = LoggerFactory.getLogger(PacerController.class);

    public PacerController() {
        className = Constants.URL_PACER;
    }

    @GetMapping("{mainframe}/{devname}/info")
    public String info(@PathVariable("mainframe") String mainframe,
                       @PathVariable("devname") String devname) {
        logger.debug("Mainframe: {}", mainframe);
        logger.debug("Device name: {}", devname);
        return getClassName();
    }

    @PostMapping("{mainframe}/{devname}/startSelfTriggered")
    public String startSelfTriggered(@PathVariable("mainframe") String mainframe,
                                     @PathVariable("devname") String devname) {
        logger.debug("Mainframe: {}", mainframe);
        logger.debug("Device name: {}", devname);

        try {
            connManager = ConnectionManager.getInstance(this.getClass(), mainframe, devname);
            getDevice(mainframe, devname).startPacerSelfTriggered();
        } catch (Exception e) {
            logger.error(
                    "Cannot get wrapper instance. This is usually an initialization problem.");
            return Response.Status.NOT_FOUND.getReasonPhrase();
        }
        return Response.Status.OK.getReasonPhrase();
    }

    @PostMapping("{mainframe}/{devname}/startExternalTriggered")
    public String startExternalTrigger(@PathVariable("mainframe") String mainframe,
                                       @PathVariable("devname") String devname) {
        logger.debug("Mainframe: {}", mainframe);
        logger.debug("Device name: {}", devname);

        try {
            connManager = ConnectionManager.getInstance(this.getClass(), mainframe, devname);
            getDevice(mainframe, devname).enablePacerExternalTrigger();
        } catch (Exception e) {
            logger.error(
                    "Cannot get wrapper instance. This is usually an initialization problem.");
            return Response.Status.NOT_FOUND.getReasonPhrase();
        }
        return Response.Status.OK.getReasonPhrase();
    }

    @PostMapping("{mainframe}/{devname}/stop")
    public String stop(@PathVariable("mainframe") String mainframe,
                       @PathVariable("devname") String devname) {
        logger.debug("Mainframe: {}", mainframe);
        logger.debug("Device name: {}", devname);

        try {
            connManager = ConnectionManager.getInstance(this.getClass(), mainframe, devname);
            getDevice(mainframe, devname).stopPacer();
        } catch (Exception e) {
            logger.error(
                    "Cannot get wrapper instance. This is usually an initialization problem.");
            return Response.Status.NOT_FOUND.getReasonPhrase();
        }
        return Response.Status.OK.getReasonPhrase();
    }

    @PostMapping("{mainframe}/{devname}/setCycles/{cycles}")
    public String setCycles(@PathVariable("mainframe") String mainframe,
                            @PathVariable("devname") String devname,
                            @PathVariable("cycles") Long cycles) {
        logger.debug("Mainframe: {}", mainframe);
        logger.debug("Device name: {}", devname);
        logger.debug("Cycles: {}", cycles);

        try {
            connManager = ConnectionManager.getInstance(this.getClass(), mainframe, devname);
            getDevice(mainframe, devname).setCycles(cycles);
        } catch (Exception e) {
            logger.error(
                    "Cannot get wrapper instance. This is usually an initialization problem.");
            return Response.Status.NOT_FOUND.getReasonPhrase();
        }
        return Response.Status.OK.getReasonPhrase();
    }

    @PostMapping("{mainframe}/{devname}/setPeriod/{period}")
    public String setPeriod(@PathVariable("mainframe") String mainframe,
                            @PathVariable("devname") String devname,
                            @PathVariable("period") Double period) {
        logger.debug("Mainframe: {}", mainframe);
        logger.debug("Device name: {}", devname);
        logger.debug("Period: {}", period);

        try {
            connManager = ConnectionManager.getInstance(this.getClass(), mainframe, devname);
            getDevice(mainframe, devname).setPeriod(period);
        } catch (Exception e) {
            logger.error(
                    "Cannot get wrapper instance. This is usually an initialization problem.");
            return Response.Status.NOT_FOUND.getReasonPhrase();
        }
        return Response.Status.OK.getReasonPhrase();
    }
}
