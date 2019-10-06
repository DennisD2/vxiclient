package de.spurtikus.vxi.controller;

import de.spurtikus.vxi.connectors.ConnectorConfig;
import de.spurtikus.vxi.service.DeviceInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.List;

/**
 * Controller for VXI system.
 * <p>
 * This controller is the umbrella around a set of VXI mainframes defined in application.properties file.
 * Basically it provides the content of that file to the frontend.
 */
@RequestMapping("/" + Constants.URL_SYSTEM)
public class SystemController {
    private Logger logger = LoggerFactory.getLogger(SystemController.class);

    protected ConnectionManager connManager;

    private String className;

    private final String defaultMessage = "VXI system REST API.";

    public SystemController() {
        className = Constants.URL_SYSTEM.replace("/", "");
    }

    /*
     * TODO: the Path("/") produces tomcat startup warning:
     * The following warnings have been detected: WARNING:
     * The (sub)resource method defaultAnswer in de.spurtikus.vxi.service.SystemBoundary
     * contains empty path annotation.
     */
    //@GetMapping("/")
    //public String defaultAnswer() {
    //    return defaultMessage;
    //}

    @GetMapping("/info")
    public String info() {
        return className;
    }

    @PostMapping("/getConfig")
    //@Consumes({ MediaType.APPLICATION_JSON })
    //@Produces({ MediaType.APPLICATION_JSON })
    public List<ExternalVXIDescriptor> getConfig() {
        // Load configuration
        try {
            Configuration.load();
        } catch (Exception e) {
            logger.error("Cannot load configuration");
            e.printStackTrace();
        }
        List<ConnectorConfig> confs = Configuration.getEnabledConfigs();
        List<ExternalVXIDescriptor> extDevs = new ArrayList<>();
        for (ConnectorConfig c : confs) {
            for (DeviceInfo d : c.getDevices()) {
                String name = d.getName();
                String url = restUrlForType(d.getType()) + '/' + d.getMainframe() + '/' + d.getName();
                String type = d.getType();
                String mf = d.getMainframe();
                ExternalVXIDescriptor de = new ExternalVXIDescriptor(name, type, url, mf);
                extDevs.add(de);
            }
        }
        return extDevs;
    }

    private String restUrlForType(String type) {
        String u = "?";
        switch (type) {
            case "mainframe":
                u = Constants.URL_MAINFRAME;
                break;
            case "multimeter":
                u = Constants.URL_MULTIMETER;
                break;
            case "pacer":
                u = Constants.URL_PACER;
                break;
            case "counter":
                u = Constants.URL_COUNTER;
                break;
            case "switch":
                u = Constants.URL_SWITCH;
                break;
            case "afg":
                u = Constants.URL_AFG;
                break;
            case "digitalIO":
                u = Constants.URL_DIGITALIO;
                break;
            default:
                logger.error("Cannot derive REST URL for device type : {}", type);
        }
        // handle complex types
        if (type.startsWith("multimeter/")) {
            //String subType = type.replace("multimeter/", "");
            u = Constants.URL_MULTIMETER;
        }
        return u;
    }
}
