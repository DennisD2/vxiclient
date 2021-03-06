package de.spurtikus.vxi.connectors;

/**
 * Connector Interface.
 * 
 * All connector implementations must comply to this contract.
 * 
 * @author dennis
 *
 */
public interface VXIConnector {
	/**
	 * Initialize connector.
	 * 
	 * @param config
	 *            configuration to use
	 * @return link to device. This link needs to be used in subsequent calls.
	 * @throws Exception
	 */
	DeviceLink initialize(ConnectorConfig config, String deviceId) throws Exception;

	/**
	 * Send a message to a link.
	 * 
	 * @param link
	 *            link to device.
	 * @param message
	 *            message to send.
	 * @throws Exception
	 */
	void send(DeviceLink link, String message) throws Exception;

	/**
	 * Receive a message from a link.
	 * 
	 * @param link
	 *            link to device.
	 * @return the message.
	 * @throws Exception
	 */
	String receive(DeviceLink link) throws Exception;

	/**
	 * Send and receive.
	 * 
	 * @param link
	 *            link to device.
	 * @param message
	 *            the message.
	 * @return
	 * @throws Exception
	 */
	String send_and_receive(DeviceLink link, String message) throws Exception;
	
}
