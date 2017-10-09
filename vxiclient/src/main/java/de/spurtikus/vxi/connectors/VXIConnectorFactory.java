package de.spurtikus.vxi.connectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.spurtikus.vxi.connectors.rpc.RPCConnector;
import de.spurtikus.vxi.connectors.rpc.RPCConnectorConfig;
import de.spurtikus.vxi.connectors.serial.SerialConnector;
import de.spurtikus.vxi.connectors.serial.SerialConnectorConfig;

/**
 * Connector factory.
 * 
 * @author dennis
 *
 */
public class VXIConnectorFactory {
	static private Logger logger = LoggerFactory
			.getLogger(VXIConnectorFactory.class);

	/**
	 * Get the connector to be used.
	 * 
	 * @param config
	 * 
	 * @return connector object.
	 */
	public static VXIConnector getConnector(ConnectorConfig config) {
		if (config instanceof RPCConnectorConfig) {
			return RPCConnector.getInstance();
		}
		if (config instanceof SerialConnectorConfig) {
			return SerialConnector.getInstance();
		}
		logger.error("Cannot construct a connector for the configuration "
				+ config.getClass().getName());
		return null;
	}

}
