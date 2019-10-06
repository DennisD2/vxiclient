package de.spurtikus.vxi.controller;

import de.spurtikus.devices.hp.DigitalIO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.core.Response;

/**
 * Controller for Digital I/O cards.
 *
 * Tested with:
 * * HP E1330 Digital I/O control. See class {HP1330}.
 *
 * @author dennis
 *
 */
@RestController
@RequestMapping(Constants.SERVICE_ROOT + Constants.URL_DIGITALIO)
public class DIOController extends AbstractController<DigitalIO> {
    private Logger logger = LoggerFactory.getLogger(DIOController.class);

    public DIOController() {
        className = Constants.URL_DIGITALIO;
    }

    @GetMapping("{mainframe}/{devname}/info")
    public String info(@PathVariable("mainframe") String mainframe,
                         @PathVariable("devname") String devname) {
        logger.debug("Mainframe: {}", mainframe);
        logger.debug("Device name: {}", devname);
        return getClassName();
    }

    @PostMapping("{mainframe}/{devname}/idn")
    public String idn(@PathVariable("mainframe") String mainframe,
                        @PathVariable("devname") String devname) {
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

    /**
     * Set a bit in a port.
     *
     * @param mainframe
     * @param devname
     * @param value
     *            bit value
     * @return
     */
    @PostMapping("{mainframe}/{devname}/setBit/{byte}/{bit}/{value}")
    public String setBit(@PathVariable("mainframe") String mainframe,
                           @PathVariable("devname") String devname,
                           @PathVariable("byte") int bbyte,
                           @PathVariable("bit") int bit,
                           @PathVariable("value") boolean value) {
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
            return Response.Status.NOT_FOUND.getReasonPhrase();
        }

        DigitalIO.PortDescription pd = getPortDescriptor(bbyte, bit);

        Boolean b;
        try {
            b = getDevice(mainframe, devname).setBit(pd, value);
        } catch (Exception e) {
            logger.error("Error accessing device.");
            return Response.Status.INTERNAL_SERVER_ERROR.getReasonPhrase();
        }
        return b.toString();
    }

    /**
     * Get a bit in a port.
     *
     * @param mainframe
     * @param devname
     * @return
     */
    @PostMapping("{mainframe}/{devname}/getBit/{byte}/{bit}")
    public String setBit(@PathVariable("mainframe") String mainframe,
                           @PathVariable("devname") String devname,
                           @PathVariable("byte") int bbyte,
                           @PathVariable("bit") int bit) {
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
            return Response.Status.NOT_FOUND.getReasonPhrase();
        }

        DigitalIO.PortDescription pd = getPortDescriptor(bbyte, bit);
        Boolean b;
        try {
            b = getDevice(mainframe, devname).getBit(pd);
        } catch (Exception e) {
            logger.error("Error accessing device.");
            return Response.Status.INTERNAL_SERVER_ERROR.getReasonPhrase();
        }
        return b.toString();
    }

    /**
     * Set a bit in a port.
     *
     * @param mainframe
     * @param devname
     * @return
     */
    @PostMapping("{mainframe}/{devname}/setPolarity/{byte}/{bit}/{polarity}")
    public String setPolarity(@PathVariable("mainframe") String mainframe,
                                @PathVariable("devname") String devname,
                                @PathVariable("byte") Integer bbyte,
                                @PathVariable("bit") Integer bit,
                                @PathVariable("polarity") Boolean polarity) {
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
            return Response.Status.NOT_FOUND.getReasonPhrase();
        }

        DigitalIO.Polarity p = (polarity=true)? DigitalIO.Polarity.POS: DigitalIO.Polarity.NEG;
        DigitalIO.PortDescription pd = getPortDescriptor(bbyte, bit);
        try {
            getDevice(mainframe, devname).setPolarity(pd, p);
        } catch (Exception e) {
            logger.error("Error accessing device.");
            return Response.Status.INTERNAL_SERVER_ERROR.getReasonPhrase();
        }
        return polarity.toString();
    }

    /**
     * Create PortDescription from byte pos and bit pos.
     * @param bbyte byte position.
     * @param bit bit position.
     * @return PortDescription.
     */
    protected DigitalIO.PortDescription getPortDescriptor(int bbyte, int bit) {
        DigitalIO.Port p;
        switch(bbyte) {
            case 1: p = DigitalIO.Port.DATA1; break;
            case 2: p = DigitalIO.Port.DATA2; break;
            case 3: p = DigitalIO.Port.DATA3; break;
            case 0:
            default: p = DigitalIO.Port.DATA0; break;
        }
        DigitalIO.Bit bb;
        switch(bit) {
            case 1: bb = DigitalIO.Bit.BIT1; break;
            case 2: bb = DigitalIO.Bit.BIT2; break;
            case 3: bb = DigitalIO.Bit.BIT3; break;
            case 4: bb = DigitalIO.Bit.BIT4; break;
            case 5: bb = DigitalIO.Bit.BIT5; break;
            case 6: bb = DigitalIO.Bit.BIT6; break;
            case 7: bb = DigitalIO.Bit.BIT7; break;
            case 0:
            default: bb = DigitalIO.Bit.BIT0; break;
        }
        DigitalIO.PortDescription pd = new DigitalIO.PortDescription(p, bb);
        return pd;
    }

}
