package de.spurtikus.vxi.connectors.rpc;

import de.spurtikus.vxi.connectors.ConnectorConfig;

/**
 * Configuration data container for ONC RPC connections.
 * 
 * @author dennis
 *
 */
public class RPCConnectorConfig implements ConnectorConfig {
	/** Host to use */
	private String host;
	/** Device to use */
	private String deviceId;
	/** Our client id; arbitrary, but should be unique between clients. */
	private int clientId;

	/**
	 * RPCConnectorConfig CTR.
	 * @param host host to use.
	 * @param clientId Our client id; arbitrary, but should be unique between clients. 
	 * @param deviceId device to use.
	 */
	public RPCConnectorConfig(String host, int clientId, String deviceId) {
		this.host = host;
		this.clientId = clientId;
		this.deviceId = deviceId;
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

	public String getDeviceId() {
		return deviceId;
	}

	public void setClientId(int clientId) {
		this.clientId = clientId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

}
