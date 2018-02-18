package de.spurtikus.devices.hp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hamcrest.core.IsEqual;
import org.junit.Before;
import org.junit.Test;

import de.spurtikus.vxi.Constants;
import de.spurtikus.vxi.connectors.AbstractConnectorConfig;
import de.spurtikus.vxi.connectors.ConnectorConfig;
import de.spurtikus.vxi.connectors.DeviceLink;
import de.spurtikus.vxi.connectors.VXIConnector;
import de.spurtikus.vxi.connectors.VXIConnectorFactory;
import de.spurtikus.vxi.connectors.rpc.RPCConnectorConfig;
import de.spurtikus.vxi.connectors.serial.GPIBSerialConnectorConfig;
import de.spurtikus.vxi.service.Configuration;

public class HP1326Test {
	String test_Serial_or_RPC = "RPC"; // "RPC" or "serial"
	
	String TEST_DEVICE_NAME = "hp1326";
	
	ConnectorConfig config;
	DeviceLink theLid = null;
	HP1326 testee = null;

	@Before
	public void before() throws Exception {
		int confId ;
		Class<?> targetClass;
		
		// Load configuration
		Configuration.load();
		if (test_Serial_or_RPC.equals("RPC")) {
			// Access multimeter via RPC in LAN-capable mainframe
			TEST_DEVICE_NAME = "hp1411";
			confId = Constants.RPC_CONFIG;
			targetClass = RPCConnectorConfig.class;
		} else {
			// Access multimeter via GPIB over Serial 
			TEST_DEVICE_NAME = "hp1326";
			confId = Constants.SERIAL_CONFIG;
			targetClass = GPIBSerialConnectorConfig.class;
		}
		// We assume usable config at some index
		config = Configuration.findConfigById(confId);
		assertNotNull(config);
		// We like to test a RPC connection
		assertThat(config.getClass(), IsEqual.equalTo(targetClass));
		
		System.out.println(config);
		
		VXIConnector vxiConnector = VXIConnectorFactory.getConnector(config);
		
		String deviceid = config.getDeviceIdByName(TEST_DEVICE_NAME);
		assertNotNull(deviceid);
		theLid = vxiConnector.initialize(config, deviceid);
		
		testee = new HP1326(vxiConnector, theLid);
	}
	
	@Test
	public void testMeasureSingle() throws Exception {
		System.out.println("Start...");
		testee.initialize();
		testee.initializeVoltageMeasurement(7.27, null);

		Double s = testee.measureSingle();
		System.out.println(s);
		System.out.println("... End");
	}

	@Test 
	public void HP1326_DMM_Test() throws Exception {
		System.out.println("Tests using channels require DVM+Switch configuration!");
		System.out.println("Start...");
		List<Integer> channels = generateChannels();
		
		testee.initializeVoltageMeasurement(7.27, channels);
		Map<Integer, Double> m = testee.measureChannels(channels);

		for (int channel : m.keySet()) {
			System.out.println(channel + " : " + m.get(channel));
		}
		System.out.println("... End");
	}

	protected List<Integer> generateChannels() {
		List<Integer> channels = new ArrayList<Integer>();
		channels.add(100);
		channels.add(101);
		// channels.add(102);
		// channels.add(103);
		// channels.add(104);
		// channels.add(105);
		// channels.add(106);
		// channels.add(107);
		// channels.add(108);
		// channels.add(109);
		// channels.add(110);
		// channels.add(111);
		// channels.add(112);
		// channels.add(113);
		return channels;
	}

	//@Test
	public void testMeasureChannelsLoop() throws Exception {
		System.out.println("Tests using channels require DVM+Switch configuration!");
		// MEAS:VOLT:DC? (@100,101,102,103,104,105,106)
		testee.initialize();

		List<Integer> channels = generateChannels();
		testee.initializeVoltageMeasurement(7.27, channels);
		
		long start = System.currentTimeMillis();
		int max=100;
		for (int i = 0; i < max; i++) {
			Map<Integer, Double> m = testee.measureChannels(channels);
			for (int channel : m.keySet()) {
				System.out.println(channel + " : " + m.get(channel));
			}
		}
		long time = System.currentTimeMillis() - start;
		System.out.println("Need " + new Double(time)/1000 +  " seconds for " + max + " measureements.");
	}


}
