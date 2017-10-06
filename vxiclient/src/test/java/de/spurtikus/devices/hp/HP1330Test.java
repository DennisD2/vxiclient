package de.spurtikus.devices.hp;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import de.spurtikus.devices.hp.HP1330.PortDescription;
import de.spurtikus.vxi.connectors.DeviceLink;
import de.spurtikus.vxi.connectors.VXIConnector;
import de.spurtikus.vxi.connectors.VXIConnectorFactory;
import de.spurtikus.vxi.connectors.rpc.RPCConnectorConfig;

public class HP1330Test {
	String host = "vxi1";
	static final int CLIENT_ID = 12345;
	static final String TEST_DEVICE_ID = "iscpi,37";

	HP1330 testee = null;
	
	@Before
	public void before() throws Exception {
		System.out.println("Start...");

		VXIConnector vxiConnector = VXIConnectorFactory.getConnector();

		RPCConnectorConfig config = new RPCConnectorConfig(host, CLIENT_ID, TEST_DEVICE_ID);
		DeviceLink theLid = vxiConnector.initialize(config);
		
		testee = new HP1330(vxiConnector, theLid);
	}
	
	@Ignore
	@Test
	public void testGetAndSet() throws Exception {
		testee.initialize();

		PortDescription get = new PortDescription(HP1330.Port.DATA0, HP1330.Bit.BIT0);
		boolean b = testee.getBit(get);
		System.out.println("Bit value: " + b);

		PortDescription set = new PortDescription(HP1330.Port.DATA0, HP1330.Bit.BIT1);
		testee.setBit(set, true);
		testee.sleep(10);
		testee.setBit(set, false);
	}

	//@Ignore
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
