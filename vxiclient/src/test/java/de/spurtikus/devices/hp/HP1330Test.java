package de.spurtikus.devices.hp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import org.hamcrest.core.IsEqual;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import de.spurtikus.devices.hp.HP1330.PortDescription;
import de.spurtikus.vxi.Constants;
import de.spurtikus.vxi.connectors.ConnectorConfig;
import de.spurtikus.vxi.connectors.DeviceLink;
import de.spurtikus.vxi.connectors.VXIConnector;
import de.spurtikus.vxi.connectors.VXIConnectorFactory;
import de.spurtikus.vxi.connectors.rpc.RPCConnectorConfig;
import de.spurtikus.vxi.service.Configuration;

public class HP1330Test {
	static final String TEST_DEVICE_NAME = "hp1330";
	ConnectorConfig config;
	DeviceLink theLid = null;
	HP1330 testee = null;

	@Before
	public void before() throws Exception {
		// Load configuration
		 Configuration.load();
		// We assume usable config at some index
		config = Configuration.findConfigById(Constants.RPC_CONFIG);
		assertNotNull(config);
		// We like to test a net GPIBSerial
		assertThat(config.getClass(), IsEqual.equalTo(RPCConnectorConfig.class));
		System.out.println(config);
		
		VXIConnector vxiConnector = VXIConnectorFactory.getConnector(config);
		
		String deviceid = config.getDeviceIdByName(TEST_DEVICE_NAME);
		assertNotNull(deviceid);
		theLid = vxiConnector.initialize(config, deviceid);
		
		testee = new HP1330(vxiConnector, theLid);
	}
	
	//@Ignore
	@Test
	public void testGetAndSet() throws Exception {
		testee.initialize();

		PortDescription get = new PortDescription(HP1330.Port.DATA0, HP1330.Bit.BIT0);
		boolean b = testee.getBit(get);
		System.out.println("Bit value: " + b);

		PortDescription set = new PortDescription(HP1330.Port.DATA0, HP1330.Bit.BIT1);
		testee.setBit(set, true);
		b = testee.getBit(set);
		System.out.println("Bit value: " + b);
		
		testee.sleep(10);
		
		testee.setBit(set, false);
		b = testee.getBit(set);
		System.out.println("Bit value: " + b);
	}

	@Ignore
	@Test
	public void testSetterLoop() throws Exception {
		testee.initialize();

		PortDescription set = new PortDescription(HP1330.Port.DATA0, HP1330.Bit.BIT1);
		while (true) {
			//System.out.println("1");
			testee.setBit(set, true);
			//testee.sleep(10);
			testee.setBit(set, false);
			//System.out.println("0");
			//testee.sleep(10);
		}
	}

}
