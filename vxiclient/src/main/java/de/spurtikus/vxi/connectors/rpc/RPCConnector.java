package de.spurtikus.vxi.connectors.rpc;

import java.io.IOException;
import java.net.InetAddress;

import org.acplt.oncrpc.OncRpcException;
import org.acplt.oncrpc.OncRpcProtocols;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.spurtikus.rpcgen.Create_LinkParms;
import de.spurtikus.rpcgen.Create_LinkResp;
import de.spurtikus.rpcgen.Device_Flags;
import de.spurtikus.rpcgen.Device_Link;
import de.spurtikus.rpcgen.Device_ReadParms;
import de.spurtikus.rpcgen.Device_ReadResp;
import de.spurtikus.rpcgen.Device_WriteParms;
import de.spurtikus.rpcgen.Device_WriteResp;
import de.spurtikus.rpcgen.vxi11;
import de.spurtikus.rpcgen.vxi11_DEVICE_CORE_Client;
import de.spurtikus.vxi.connectors.ConnectorConfig;
import de.spurtikus.vxi.connectors.DeviceLink;
import de.spurtikus.vxi.connectors.VXIConnector;
import de.spurtikus.vxi.util.ConversionUtil;

/**
 * ONC RPC Connector implementation.
 * 
 * Allows to access VXI-over-LAN capable instruments/devices.
 * 
 * @author dennis
 *
 */
public class RPCConnector implements VXIConnector {
	static Logger logger = LoggerFactory.getLogger("RPCConnector");

	/** Singleton */
	static RPCConnector INSTANCE = null;

	/** Config to use */
	static RPCConnectorConfig theConfig = null;

	/** timeout value in [ms] */
	static final int VXI11_DEFAULT_TIMEOUT = 50000;

	/** VXI Device Core Client to use in all calls */
	private static vxi11_DEVICE_CORE_Client client = null;

	/** VXI port on remote machine */
	private static int vxiPort;

	private RPCConnector() {
		// Singleton
		
		// Initialize error array
		RPCErrors.initErrorMap();
	}

	public static VXIConnector getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new RPCConnector();
		}
		return INSTANCE;
	}

	@Override
	public DeviceLink initialize(ConnectorConfig config) throws Exception {
		theConfig = (RPCConnectorConfig) config;
		vxiPort = getPortFromPortmapper();
		client = createVXICoreDevice();
		Device_Link device_link = createVXILink(theConfig.getClientId(),
				theConfig.getDeviceId());
		if (device_link == null) {
			throw new Exception("Cannot create device link.");
		}
		return new DeviceLink(device_link);
	}

	static PortmapperClient portmapperClient = null;

	/**
	 * Creates portmapper client for remote machine and get port to use.
	 * 
	 * @return
	 * @throws OncRpcException
	 * @throws IOException
	 */
	private int getPortFromPortmapper() throws OncRpcException, IOException {
		if (portmapperClient == null) {
			portmapperClient = new PortmapperClient(theConfig.getHost());
		}
		return portmapperClient.getVXIPort();
	}

	/**
	 * Creates a client to be used for all further VXI communication.
	 * 
	 * @return
	 * 
	 */
	private static vxi11_DEVICE_CORE_Client createVXICoreDevice() {
		vxi11_DEVICE_CORE_Client c = null;
		try {
			c = new vxi11_DEVICE_CORE_Client(
					InetAddress.getByName(theConfig.getHost()),
					vxi11.DEVICE_CORE, vxi11.DEVICE_CORE_VERSION, vxiPort,
					OncRpcProtocols.ONCRPC_TCP);
		} catch (OncRpcException e) {
			logger.error("OnRpc failed");
			e.printStackTrace();
		} catch (IOException e) {
			logger.error("IOException failure");
			e.printStackTrace();
		}
		logger.info("VXI Device Core Client created successfully for {}:{} .",
				theConfig.getHost(), vxiPort);
		return c;
	}

	/**
	 * Creates a link to a device.
	 * 
	 * @param clientId
	 * @param deviceId
	 * @return
	 */
	private static Device_Link createVXILink(int clientId, String deviceId) {
		// Create a link to an instrument
		Create_LinkParms params = new Create_LinkParms();
		params.clientId = clientId;
		params.device = deviceId;
		params.lockDevice = false;
		params.lock_timeout = VXI11_DEFAULT_TIMEOUT;
		Create_LinkResp response = null;
		try {
			response = client.create_link_1(params);
		} catch (OncRpcException e) {
			System.out.println("OnRpc failed during client call");
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			System.out.println("IOException failure during client call");
			e.printStackTrace();
			return null;
		}
		if (response.lid.value==0) {
			logger.error("Link ID is 0, this seems to be wrong.");
			return null;
		}
		logger.info(
				"Created successfully a device link (Link ID={}) for device {}.",
				response.lid.value, deviceId);
		logger.debug("Max Recv Size: {}", response.maxRecvSize);

		return response.lid;
	}

	@Override
	public void send(DeviceLink link, String message) throws Exception {
		logger.debug("Send: {}", message);
		Device_WriteParms params = new Device_WriteParms();
		params.lid = link.getWrapped();
		params.flags = new Device_Flags();
		params.flags.value = 8;
		params.io_timeout = VXI11_DEFAULT_TIMEOUT;
		params.lock_timeout = VXI11_DEFAULT_TIMEOUT;
		params.data = ConversionUtil.toBytes(message + "\n");

		Device_WriteResp response = client.device_write_1(params);

		if (response.error.value != 0) {
			String es = "Error during receive: '"
					+ RPCErrors.toErrorString(response.error.value) + "' ("
					+ response.error.value + ")";
			logger.error(es);
			throw new Exception(es);
		}
		logger.debug("Response Data size: {}", response.size);
	}

	@Override
	public String receive(DeviceLink link) throws Exception {
		logger.debug("Receiving...");
		Device_ReadParms params = new Device_ReadParms();
		params.lid = link.getWrapped();
		params.requestSize = 4096;
		params.io_timeout = VXI11_DEFAULT_TIMEOUT;
		params.lock_timeout = VXI11_DEFAULT_TIMEOUT;
		params.flags = new Device_Flags();
		params.termChar = 0;

		Device_ReadResp response = client.device_read_1(params);
		if (response.error.value != 0) {
			String es = "Error during receive: '"
					+ RPCErrors.toErrorString(response.error.value) + "' ("
					+ response.error.value + ")";
			logger.error(es);
			throw new Exception(es);
		}
		logger.debug("Response Data size: {}", response.data.length);
		logger.debug("{}", ConversionUtil.toString(response.data));
		return ConversionUtil.toString(response.data);
	}

	@Override
	public String send_and_receive(DeviceLink link, String message)
			throws Exception {
		send(link, message);
		// Thread.sleep(10000);
		return receive(link);
	}

}
