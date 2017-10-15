package de.spurtikus.devices.hp;

import static org.junit.Assert.assertThat;

import java.util.List;

import org.hamcrest.core.IsEqual;
import org.junit.Before;
import org.junit.Test;

import de.spurtikus.vxi.connectors.ConnectorConfig;
import de.spurtikus.vxi.connectors.DeviceLink;
import de.spurtikus.vxi.connectors.VXIConnector;
import de.spurtikus.vxi.connectors.VXIConnectorFactory;
import de.spurtikus.vxi.connectors.serial.GPIBSerialConnectorConfig;
import de.spurtikus.vxi.mainframes.hp1300b.VXIDevice;
import de.spurtikus.vxi.service.Configuration;

public class HP1300bTest {

	VXIConnector vxiConnector = null;
	DeviceLink theLid = null;

	private ConnectorConfig simpleConfig() {
//		String tty = "/dev/ttyUSB0";
//		int baudRate = 115200;
//		GPIBSerialConnectorConfig config = new GPIBSerialConnectorConfig();
//		config.setPort(tty);
//		config.setBaudRate(baudRate);
//		config.setControllerPrimaryAddress(9);
//		config.setControllerSecondaryAddress(0);
//		return config;
		return null;
	}

	@Before
	public void beforeTest() throws Exception {
		System.out.println("Start...");

		// Get usable configuration
		Configuration configuration = Configuration.getInstance();
		List<ConnectorConfig> confs = configuration.getConfigs();
		// We assume usable config is confs[1]
		ConnectorConfig config = confs.get(2);
		// We like to test a net device
		assertThat(config.getClass(),IsEqual.equalTo(GPIBSerialConnectorConfig.class));
		System.out.println(config);

		VXIConnector vxiConnector = VXIConnectorFactory.getConnector(config);
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
