package de.spurtikus.vxi.controller;

import de.spurtikus.devices.hp.BaseHPMultimeter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.core.Response;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * Controller for multimeters.
 * <p>
 * Tested with:
 * * HP1326 multimeter. See class {HP1326}.
 * * HP1411 multimeter. The HP1411 is just a resized HP1326. See class {HP1326}.
 * * HP3478 multimeter. See class {HP3478}.
 *
 * @author dennis
 */
@RestController
@RequestMapping("/" + Constants.URL_MULTIMETER)
public class MultimeterController extends AbstractController<BaseHPMultimeter> {
    private Logger logger = LoggerFactory.getLogger(MultimeterController.class);

    public MultimeterController() {
        className = Constants.URL_MULTIMETER;
    }

    @GetMapping(value = "{mainframe}/{devname}/info")
    public String info(@PathVariable("mainframe") String mainframe,
                       @PathVariable("devname") String devname) {
        //logger.debug("Incoming URI : {}", uriInfo.getPath());
        logger.debug("Mainframe: {}", mainframe);
        logger.debug("Device name: {}", devname);
        return getClassName();
    }

    @GetMapping(value = "{mainframe}/{devname}/init")
    public String init(@PathVariable("mainframe") String mainframe,
                       @PathVariable("devname") String devname) {
        //logger.debug("Incoming URI : {}", uriInfo.getPath());
        logger.debug("Mainframe: {}", mainframe);
        logger.debug("Device name: {}", devname);

        try {
            connManager = ConnectionManager.getInstance(this.getClass(), mainframe, devname);
        } catch (Exception e) {
            logger.error(
                    "Cannot get wrapper instance. This is usually an initialization problem.");
            return Response.Status.NOT_FOUND.getReasonPhrase();
        }
        try {
            getDevice(mainframe, devname).initialize();
        } catch (Exception e) {
            logger.error("Error in initialize().");
            return Response.Status.INTERNAL_SERVER_ERROR.getReasonPhrase();
        }
        return "initialized";
    }

    @PostMapping(value = "{mainframe}/{devname}/idn")
    //@Consumes({ MediaType.APPLICATION_JSON })
    //@Produces(MediaType.APPLICATION_JSON)
    public String idn(@PathVariable("mainframe") String mainframe,
                      @PathVariable("devname") String devname) {
        //logger.debug("Incoming URI : {}", uriInfo.getPath());
        logger.debug("Mainframe: {}", mainframe);
        logger.debug("Device name: {}", devname);

        try {
            connManager = ConnectionManager.getInstance(this.getClass(), mainframe, devname);
        } catch (Exception e) {
            logger.error(
                    "Cannot get wrapper instance. This is usually an initialization problem.");
            return Response.Status.NOT_FOUND.getReasonPhrase();
        }

        String answer;
        try {
            answer = connManager.getConnector(this.getClass(), mainframe, devname)
                    .send_and_receive(connManager.getLink(this.getClass(), mainframe, devname),
                            "*IDN?");
        } catch (Exception e) {
            logger.error("Error in send_and_receive().");
            return Response.Status.NOT_FOUND.getReasonPhrase();
        }
        System.out.println(answer);

        return answer;
    }

    /**
     * Reads multiple channels. Channel list is assumed to be coming in as POST data.
     *
     * @param mainframe Mainframe to use. Comes from vxiserver.properties, e.g. "mfb".
     * @param devname   Device to use.
     * @param range     Range to use during measurement. E.g. "7.2.7". See {HP1326}.
     * @param channels  List of channels to measure. E.g. [101,102]. See {HP1326}.
     * @return
     */
    @PostMapping(value = "{mainframe}/{devname}/read/{range}")
    //@Consumes({ MediaType.APPLICATION_JSON })
    //@Produces(MediaType.APPLICATION_JSON)
    public Map readChannels(@PathVariable("mainframe") String mainframe,
                            @PathVariable("devname") String devname,
                            @PathVariable("range") Double range,
                            @RequestBody List<Integer> channels) {
        //logger.debug("Incoming URI : {}", uriInfo.getPath());
        logger.debug("Mainframe: {}", mainframe);
        logger.debug("Device name: {}", devname);
        logger.debug("Range: {}", range);
        logger.debug("Channels: {}", channels);

        logger.info("Using channels require DVM+Switch configuration!");

        try {
            connManager = ConnectionManager.getInstance(this.getClass(), mainframe, devname);
        } catch (Exception e) {
            logger.error(
                    "Cannot get wrapper instance. This is usually an initialization problem.");
            return null;
        }

        Map<Integer, Double> m = new HashMap<>();
        try {
            //getDevice(mainframe, devname).initializeVoltageMeasurement(range, channels);
            m = getDevice(mainframe, devname).measureChannels(channels);
        } catch (Exception e) {
            logger.error("Error accessing device." + e.getMessage());
            return null;
        }
        for (int channel : m.keySet()) {
            logger.debug("{} : {}", channel, m.get(channel));
        }
        return m;
    }

    private static final int MAXCHANNELS = 50;
    double oldy[] = new double[2 * MAXCHANNELS];

    @PostMapping(value = "{mainframe}/{devname}/Fakeread/{range}")
    //@Consumes({ MediaType.APPLICATION_JSON })
    //@Produces(MediaType.APPLICATION_JSON)
    public String readChannelsFake(@PathVariable("mainframe") String mainframe,
                                   @PathVariable("devname") String devname,
                                   @PathVariable("range") Double range,
                                   @RequestBody List<Integer> channels) {
        //logger.debug("Incoming URI : {}", uriInfo.getPath());
        logger.debug("Mainframe: {}", mainframe);
        logger.debug("Device name: {}", devname);
        logger.debug("Range: {}", range);
        logger.debug("Channels: {}", channels);

		/*try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}*/

        // {"100":0.2712517,"101":-0.2288322}
        // String answer = "{\"100\":"+n+",\"101\":"+m+"}";
        double delta;
        double newy;
        String resy;
        StringBuilder sb = new StringBuilder();
        sb.append("{");
        NumberFormat formatter = new DecimalFormat("#0.0000000", DecimalFormatSymbols.getInstance(Locale.US));
        for (int i = 0; i < channels.size(); i++) {
            delta = Math.random();
            newy = oldy[i];
            if (delta <= 0.5) {
                newy = oldy[i] - delta / 5;
            } else {
                newy = oldy[i] + delta / 5;
            }
            oldy[i] = newy;
            resy = formatter.format(newy);
            String chName = "" + channels.get(i);
            sb.append("\"" + chName + "\":" + resy);
            if (i < channels.size() - 1) {
                sb.append(",");
            }
        }
        sb.append("}");

        logger.debug(sb.toString());
        return sb.toString();
    }

}