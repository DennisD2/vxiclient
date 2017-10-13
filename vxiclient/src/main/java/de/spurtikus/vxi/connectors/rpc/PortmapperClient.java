package de.spurtikus.vxi.connectors.rpc;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.acplt.oncrpc.OncRpcException;
import org.acplt.oncrpc.OncRpcPortmapClient;
import org.acplt.oncrpc.OncRpcProgramNotRegisteredException;
import org.acplt.oncrpc.OncRpcProtocols;
import org.acplt.oncrpc.OncRpcServerIdent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.spurtikus.rpcgen.vxi11;

/**
 * Portmapper Client implementation.
 * 
 * The portmapper client accesses a remote portmapper and inquires to port to
 * use for the VXI program.
 * 
 * @author dennis
 *
 */
public class PortmapperClient {
	Logger logger = LoggerFactory.getLogger("PortmapperClient");

	/** portmapper default port */
	// static final int PORTMAPPER_PORT = 111;

	/** Portmapper object */
	OncRpcPortmapClient portmapper = null;

	/** port to use */
	int port = 0;

	public PortmapperClient(String host) throws OncRpcException, IOException {
		// Create RpcPortMapper Client
		InetAddress address = null;
		try {
			address = InetAddress.getByName(host);
		} catch (UnknownHostException e) {
			logger.error("get inet address by name failed for {}.", host);
			e.printStackTrace();
			throw e;
		}
		try {
		portmapper = new OncRpcPortmapClient(address,
				OncRpcProtocols.ONCRPC_TCP);
		} catch  (Exception e) {
			logger.error("Cannot create portmap client for {}.", host);
			e.printStackTrace();
			throw e;
		}
		logger.info("Created portmapper client for {}", host);
	}

	/**
	 * Gets port for VXI11 LAN connection.
	 * 
	 * @return port for VXI11 LAN connection.
	 * @throws OncRpcException
	 */
	public int getVXIPort() throws OncRpcException {
		if (port == 0) {
			try {
				port = portmapper.getPort(vxi11.DEVICE_CORE, 1,
						OncRpcProtocols.ONCRPC_TCP);
			} catch (OncRpcProgramNotRegisteredException e) {
				logger.error("ONC/RPC program server not found");
				System.exit(0);
			}
		}
		logger.info("VXI ONC RPC Program available at port {}", port);
		return port;
	}

	public void listServices() throws Exception {
		OncRpcServerIdent[] serviceList = portmapper.listServers();
		for (OncRpcServerIdent s : serviceList) {
			logger.info("Port: " + s.port + ", Program: " + s.program
					+ ", Protocol: " + s.protocol + ", Version: " + s.version);
		}
	}
}
