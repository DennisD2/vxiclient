package de.spurtikus.vxi.controller;

import de.spurtikus.devices.hp.HP1300b;
import de.spurtikus.vxi.mainframes.hp1300b.VXIDevice;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Mainframe controller.
 * <p>
 * Tested with:
 * * HP1300 mainframe. See class {HP1300b}.
 *
 * @author dennis
 */
@RestController
@RequestMapping(Constants.SERVICE_ROOT + Constants.URL_MAINFRAME)
public class MainframeController extends AbstractController<HP1300b> {
    private Logger logger = LoggerFactory.getLogger(MainframeController.class);

    public MainframeController() {
        className = Constants.URL_MAINFRAME;
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
        answer = answer.replace("\n", "");
        return answer;
    }

    @PostMapping("{mainframe}/{devname}/devices")
    //@Consumes({ MediaType.APPLICATION_JSON })
    //@Produces(MediaType.APPLICATION_JSON)
    public List<VXIDevice> listDevices(@PathVariable("mainframe") String mainframe,
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
            return null;
        }

        boolean fakeResult = false;
        List<VXIDevice> devices = new ArrayList<>();
        try {
            devices = getDevice(mainframe, devname).listDevices(fakeResult);
        } catch (Exception e) {
            logger.error("Error accessing device.");
            return null;
        }
        for (VXIDevice d : devices) {
            logger.debug("{}", d.toString());
        }
        return devices;
    }

    //[{"name":"hp1301","type":"mainframe","URL":"/hp1300/mfb/hp1301","mainframe":"mfb"},{"name":"hp1300pacer","type":"pacer","URL":"/hp1300pacer/mfb/hp1300pacer","mainframe":"mfb"},{"name":"hp1326","type":"multimeter","URL":"/hp1326/mfb/hp1326","mainframe":"mfb"},{"name":"hp1333","type":"counter","URL":"/hp1333/mfb/hp1333","mainframe":"mfb"},{"name":"hp1351","type":"switch","URL":"/hp1351/mfb/hp1351","mainframe":"mfb"},{"name":"hp1340","type":"afg","URL":"/hp1340/mfb/hp1340","mainframe":"mfb"},{"name":"hp1330","type":"digitalIO","URL":"/hp1330/mfb/hp1330","mainframe":"mfb"},{"name":"hp1411","type":"multimeter","URL":"/hp1326/mfc/hp1411","mainframe":"mfc"},{"name":"hp1330","type":"digitalIO","URL":"/hp1330/mfc/hp1330","mainframe":"mfc"}]
    @PostMapping("{mainframe}/{devname}/Fakedevices")
    //@Consumes({ MediaType.APPLICATION_JSON })
    //@Produces(MediaType.APPLICATION_JSON)
    public List<VXIDevice> listDevicesFake(@PathVariable("mainframe") String mainframe,
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
            return null;
        }

        boolean fakeResult = true;
        List<VXIDevice> devices = new ArrayList<>();
        try {
            devices = getDevice(mainframe, devname).listDevices(fakeResult);
        } catch (Exception e) {
            logger.error("Error accessing device.");
            return null;
        }
        for (VXIDevice d : devices) {
            logger.debug("{}", d.toString());
        }
        return devices;
    }

}
