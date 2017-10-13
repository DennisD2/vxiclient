package de.spurtikus.devices.hp;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.spurtikus.vxi.connectors.DeviceLink;
import de.spurtikus.vxi.connectors.VXIConnector;
import de.spurtikus.vxi.connectors.VXIConnectorFactory;
import de.spurtikus.vxi.connectors.serial.GPIBSerialConnectorConfig;
import de.spurtikus.vxi.mainframes.hp1300b.VXIDevice;

public class HP1300bTest {

	String tty = "/dev/ttyUSB0";
	int baudRate = 115200;

	VXIConnector vxiConnector = null;
	DeviceLink theLid = null;

	@Before
	public void beforeTest() throws Exception {
		System.out.println("Start...");

		GPIBSerialConnectorConfig config = new GPIBSerialConnectorConfig();
		config.setPort(tty);
		config.setBaudRate(baudRate);
		config.setControllerPrimaryAddress(9);
		config.setControllerSecondaryAddress(0);

		vxiConnector = VXIConnectorFactory.getConnector(config);

		theLid = vxiConnector.initialize(config);
	}

	@Test
	public void testListDevices() throws Exception {
		HP1300b mainframe = new HP1300b(vxiConnector, theLid);
		List<VXIDevice> devs = mainframe.listDevices(false);

		for (VXIDevice dev : devs) {
			if (dev.getAddress() % 8 != 0 && dev.getAddress() != 1) {
				System.out.print("'-");
			}
			System.out.print(dev.toString());
			System.out.println();
		}
	}

}
