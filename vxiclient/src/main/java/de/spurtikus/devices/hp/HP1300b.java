package de.spurtikus.devices.hp;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.spurtikus.vxi.connectors.DeviceLink;
import de.spurtikus.vxi.connectors.VXIConnector;
import de.spurtikus.vxi.connectors.serial.GPIBSerialConnector;
import de.spurtikus.vxi.mainframes.hp1300b.VXIDevice;
import de.spurtikus.vxi.util.CommunicationUtil;

/**
 * HP/Agilent/Keysight E1300B/E1301B/75000 VXI mainframe control.
 * 
 * @author dennis
 * 
 */
public class HP1300b extends BaseHPDevice {
	static Logger logger = LoggerFactory.getLogger(HP1300b.class);

	public HP1300b(VXIConnector parent, DeviceLink link) {
		super(parent, link);
	}

	/**
	 * List devices. Can produce fake data for testing without a real device.
	 * 
	 * @param fakeResult
	 * @return
	 * @throws Exception
	 */
	public List<VXIDevice> listDevices(boolean fakeResult) throws Exception {
		((GPIBSerialConnector) vxiConnector).selectDevice(deviceLink, 9, 0);
		String s;
		if (fakeResult) {
			s = "+1,-1,+4095,+1300,-1,+0,HYB,NONE,#H00000000,#H00000000,READY,,,,SYSTEM INSTALLED AT SECONDARY ADDR  0;+24,+0,+4095,+65344,-1,+0,REG,A16 ,#H00000000,#H00000000,READY,,,,VOLTMTR INSTALLED AT SECONDARY ADDR  3;+25,+0,+4095,+65280,-1,+0,REG,A16 ,#H00000000,#H00000000,READY,,,,VOLTMTR INSTALLED AT SECONDARY ADDR  3;+26,+0,+4095,+65280,-1,+0,REG,A16 ,#H00000000,#H00000000,READY,,,,VOLTMTR INSTALLED AT SECONDARY ADDR  3;+64,+0,+4095,+65300,-1,+0,REG,A16 ,#H00000000,#H00000000,READY,,,,SWITCH INSTALLED AT SECONDARY ADDR  8;+72,+0,+4095,+65296,-1,+0,REG,A16 ,#H00000000,#H00000000,READY,,,,SWITCH INSTALLED AT SECONDARY ADDR  9;+80,+0,+4095,+65440,-1,+0,REG,A16 ,#H00000000,#H00000000,READY,,,,ARB INSTALLED AT SECONDARY ADDR 10;+144,+0,+4095,+65361,-1,+0,REG,A16 ,#H00000000,#H00000000,READY,,,,DIG_I/O INSTALLED AT SECONDARY ADDR 18";
		} else {
			s = vxiConnector.send_and_receive(deviceLink, "VXI:CONF:DLIS?");
		}
		// System.out.println(s);
		List<VXIDevice> devices = new ArrayList<VXIDevice>();
		String lines[] = s.split(";");
		for (String l : lines) {
			// System.out.println(l);
			VXIDevice dev = VXIDevice.materializeFrom(l);
			logger.info("Device found: " + dev.getName());
			devices.add(dev);
		}
		return devices;
	}

	// TODO: needed? for what?
	@Deprecated
	private int answerStringToInteger(String s) {
		CommunicationUtil.dumpString(s);
		if (s == null || s.isEmpty()) {
			return 0;
		}
		return Integer.parseInt(s.replace("\n", "").replace("\r", ""));
	}

}
