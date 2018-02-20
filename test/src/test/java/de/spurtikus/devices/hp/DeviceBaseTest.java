package de.spurtikus.devices.hp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import org.hamcrest.core.IsEqual;

import de.spurtikus.vxi.Constants;
import de.spurtikus.vxi.connectors.ConnectorConfig;
import de.spurtikus.vxi.connectors.rpc.RPCConnectorConfig;
import de.spurtikus.vxi.connectors.serial.GPIBSerialConnectorConfig;
import de.spurtikus.vxi.service.Configuration;

public class DeviceBaseTest {

	protected ConnectorConfig loadConfig(String configType) throws Exception {
		final Class<?> targetClass;
		final int confId ;
		// Load configuration
		Configuration.load();
		if (configType.equals("RPC")) {
			// Access device via RPC in LAN-capable mainframe
			confId = Constants.RPC_CONFIG;
			targetClass = RPCConnectorConfig.class;
		} else {
			// Access device via GPIB over Serial 
			confId = Constants.SERIAL_CONFIG;
			targetClass = GPIBSerialConnectorConfig.class;
		}
		// We assume usable config at some index
		ConnectorConfig config = Configuration.findConfigById(confId);
		assertNotNull(config);
		// We like to test a RPC connection
		assertThat(config.getClass(), IsEqual.equalTo(targetClass));
		return config;
	}

}
