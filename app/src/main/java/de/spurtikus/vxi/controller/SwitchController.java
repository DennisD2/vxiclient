package de.spurtikus.vxi.controller;

import de.spurtikus.devices.hp.HP1351;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.core.Response;


/**
 * Boundary for FET switches.
 * <p>
 * TODO: this boundary should be integrated into HP1330Boundary.
 * <p>
 * Tested with:
 * * HP1351 FET Switch. See class {HP1351}.
 *
 * @author dennis
 */
@RestController
@RequestMapping(Constants.SERVICE_ROOT + Constants.URL_SWITCH)
public class SwitchController extends AbstractController<HP1351> {
    private Logger logger = LoggerFactory.getLogger(SwitchController.class);

    public SwitchController() {
        className = Constants.URL_SWITCH;
    }

    //@Produces(MediaType.TEXT_PLAIN)
    @GetMapping("{mainframe}/{devname}/info")
    public String info(@PathVariable("mainframe") String mainframe,
                       @PathVariable("devname") String devname) {
        //logger.debug("Incoming URI : {}", uriInfo.getPath());
        logger.debug("Mainframe: {}", mainframe);
        logger.debug("Device name: {}", devname);
        return getClassName();
    }

    @PostMapping("{mainframe}/{devname}/idn")
    //@Consumes({ MediaType.APPLICATION_JSON })
    //@Produces(MediaType.APPLICATION_JSON)
    public String idn(@PathVariable("mainframe") String mainframe,
                      @PathVariable("devname") String devname) {
        //logger.debug("Incoming URI : {}", uriInfo.getPath());
        logger.debug("Mainframe: {}", mainframe);
        logger.debug("Device name: {}", devname);

        try {
            connManager = ConnectionManager.getInstance(this.getClass(),
                    mainframe, devname);
        } catch (Exception e) {
            logger.error(
                    "Cannot get wrapper instance. This is usually an initialization problem.");
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
}
