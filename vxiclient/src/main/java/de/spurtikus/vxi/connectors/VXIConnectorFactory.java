package de.spurtikus.vxi.connectors;

import de.spurtikus.vxi.connectors.rpc.RPCConnector;

/**
 * Connector factory.
 * 
 * @author dennis
 *
 */
public class VXIConnectorFactory {

	/**
	 * Get the connector to be used.
	 * 
	 * @return connector object.
	 */
	public static VXIConnector getConnector() {
		// for now, we have only one implementation.
		return RPCConnector.getInstance();
	}

}
