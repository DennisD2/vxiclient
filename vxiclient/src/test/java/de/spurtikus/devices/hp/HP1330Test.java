package de.spurtikus.devices.hp;

import static org.junit.Assert.assertThat;

import java.util.List;

import org.hamcrest.core.IsEqual;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import de.spurtikus.devices.hp.HP1330.PortDescription;
import de.spurtikus.vxi.connectors.ConnectorConfig;
import de.spurtikus.vxi.connectors.DeviceLink;
import de.spurtikus.vxi.connectors.VXIConnector;
import de.spurtikus.vxi.connectors.VXIConnectorFactory;
import de.spurtikus.vxi.connectors.rpc.RPCConnectorConfig;
import de.spurtikus.vxi.service.Configuration;

public class HP1330Test {
	HP1330 testee = null;
	static final String TEST_DEVICE_ID = "iscpi,37";

	private ConnectorConfig simpleConfig() {
		//String host = "vxi1";
		//static final int CLIENT_ID = 12345;
		//return new RPCConnectorConfig(host, CLIENT_ID, TEST_DEVICE_ID);	
		return null;
	}

	@Before
	public void before() throws Exception {
		System.out.println("Start...");

		// Get usable configuration
		Configuration configuration = Configuration.getInstance();
		List<ConnectorConfig> confs = configuration.getConfigs();
		// We assume usable config is confs[0]
		ConnectorConfig config = confs.get(1);
		// We like to test a net device 
		assertThat(config.getClass(), IsEqual.equalTo(RPCConnectorConfig.class));
		System.out.println(config);
		
		VXIConnector vxiConnector = VXIConnectorFactory.getConnector(config);

		DeviceLink theLid = vxiConnector.initialize(config, TEST_DEVICE_ID);
		
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
