package de.spurtikus.vxi.connectors.rpc;

import de.spurtikus.vxi.connectors.AbstractConnectorConfig;
import de.spurtikus.vxi.connectors.ConnectorConfig;

/**
 * Configuration data container for ONC RPC connections.
 * 
 * @author dennis
 *
 */
public class RPCConnectorConfig extends AbstractConnectorConfig
		implements ConnectorConfig {
	/** Host to use */
	private String host;
	/** Our client id; arbitrary, but should be unique between clients. */
	private int clientId;

	public RPCConnectorConfig() {
	}

	/**
	 * RPCConnectorConfig CTR.
	 * 
	 * @param host
	 *            host to use.
	 * @param clientId
	 *            Our client id; arbitrary, but should be unique between
	 *            clients.
	 * @param deviceId
	 *            device to use.
	 */
	public RPCConnectorConfig(String host, int clientId) {
		this.host = host;
		this.clientId = clientId;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getClientId() {
		return clientId;
	}

	public void setClientId(int clientId) {
		this.clientId = clientId;
	}

	@Override
	public String toString() {
		return super.toString() + " client:" + clientId + ", host:"+ host  ;
	}

}
