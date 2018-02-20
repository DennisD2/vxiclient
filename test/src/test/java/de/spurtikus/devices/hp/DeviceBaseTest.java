package de.spurtikus.devices.hp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import org.hamcrest.core.IsEqual;

import de.spurtikus.vxi.Constants;
import de.spurtikus.vxi.connectors.ConnectorConfig;
import de.spurtikus.vxi.connectors.rpc.RPCConnectorConfig;
import de.spurtikus.vxi.connectors.serial.GPIBSerialConnectorConfig;
import de.spurtikus.vxi.service.Configuration;

/**
 * Base class for device tests.
 * 
 * @author dennis
 *
 */
public class DeviceBaseTest {

	/**
	 * Load connector configuration
	 * 
	 * @param configType
	 *            configuration type to load. This is a string. To access device
	 *            via RPC in LAN-capable mainframe use configType="RPC". To
	 *            access device via GPIB over Serial use configType="Serial"
	 * @return usable connector configuration or null.
	 * @throws Exception
	 */
	protected ConnectorConfig loadConfig(String configType) throws Exception {
		final Class<?> expectedClass;
		final int configId;
		// Load configuration
		Configuration.load();
		if (configType.equals("RPC")) {
			// RPC access
			configId = Constants.RPC_CONFIG;
			expectedClass = RPCConnectorConfig.class;
		} else {
			// GPIB over Serial access
			configId = Constants.SERIAL_CONFIG;
			expectedClass = GPIBSerialConnectorConfig.class;
		}
		ConnectorConfig config = Configuration.findConfigById(configId);
		assertNotNull(config);
		// Ensure that we've got the correct configuration (by checking the
		// objects class)
		assertThat(config.getClass(), IsEqual.equalTo(expectedClass));
		return config;
	}

}
