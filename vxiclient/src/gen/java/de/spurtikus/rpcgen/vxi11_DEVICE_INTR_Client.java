/*
 * Automatically generated by jrpcgen 1.1.3 on 02.12.17 10:39
 * jrpcgen is part of the "Remote Tea" ONC/RPC package for Java
 * See http://remotetea.sourceforge.net for details
 */
package de.spurtikus.rpcgen;
import org.acplt.oncrpc.*;
import java.io.IOException;

import java.net.InetAddress;

/**
 * The class <code>vxi11_DEVICE_INTR_Client</code> implements the client stub proxy
 * for the DEVICE_INTR remote program. It provides method stubs
 * which, when called, in turn call the appropriate remote method (procedure).
 */
public class vxi11_DEVICE_INTR_Client extends OncRpcClientStub {

    /**
     * Constructs a <code>vxi11_DEVICE_INTR_Client</code> client stub proxy dummy.
     * @throws OncRpcException if an ONC/RPC error occurs.
     * @throws IOException if an I/O error occurs.
     */
    public vxi11_DEVICE_INTR_Client() throws OncRpcException, IOException {
        super(null);
    }

    /**
     * Constructs a <code>vxi11_DEVICE_INTR_Client</code> client stub proxy object
     * from which the DEVICE_INTR remote program can be accessed.
     * @param host Internet address of host where to contact the remote program.
     * @param protocol {@link org.acplt.oncrpc.OncRpcProtocols Protocol} to be
     *   used for ONC/RPC calls.
     * @throws OncRpcException if an ONC/RPC error occurs.
     * @throws IOException if an I/O error occurs.
     */
    public vxi11_DEVICE_INTR_Client(InetAddress host, int protocol)
           throws OncRpcException, IOException {
        super(host, vxi11.DEVICE_INTR, 1, 0, protocol);
    }

    /**
     * Constructs a <code>vxi11_DEVICE_INTR_Client</code> client stub proxy object
     * from which the DEVICE_INTR remote program can be accessed.
     * @param host Internet address of host where to contact the remote program.
     * @param port Port number at host where the remote program can be reached.
     * @param protocol {@link org.acplt.oncrpc.OncRpcProtocols Protocol} to be
     *   used for ONC/RPC calls.
     * @throws OncRpcException if an ONC/RPC error occurs.
     * @throws IOException if an I/O error occurs.
     */
    public vxi11_DEVICE_INTR_Client(InetAddress host, int port, int protocol)
           throws OncRpcException, IOException {
        super(host, vxi11.DEVICE_INTR, 1, port, protocol);
    }

    /**
     * Constructs a <code>vxi11_DEVICE_INTR_Client</code> client stub proxy object
     * from which the DEVICE_INTR remote program can be accessed.
     * @param client ONC/RPC client connection object implementing a particular
     *   protocol.
     * @throws OncRpcException if an ONC/RPC error occurs.
     * @throws IOException if an I/O error occurs.
     */
    public vxi11_DEVICE_INTR_Client(OncRpcClient client)
           throws OncRpcException, IOException {
        super(client);
    }

    /**
     * Constructs a <code>vxi11_DEVICE_INTR_Client</code> client stub proxy object
     * from which the DEVICE_INTR remote program can be accessed.
     * @param host Internet address of host where to contact the remote program.
     * @param program Remote program number.
     * @param version Remote program version number.
     * @param protocol {@link org.acplt.oncrpc.OncRpcProtocols Protocol} to be
     *   used for ONC/RPC calls.
     * @throws OncRpcException if an ONC/RPC error occurs.
     * @throws IOException if an I/O error occurs.
     */
    public vxi11_DEVICE_INTR_Client(InetAddress host, int program, int version, int protocol)
           throws OncRpcException, IOException {
        super(host, program, version, 0, protocol);
    }

    /**
     * Constructs a <code>vxi11_DEVICE_INTR_Client</code> client stub proxy object
     * from which the DEVICE_INTR remote program can be accessed.
     * @param host Internet address of host where to contact the remote program.
     * @param program Remote program number.
     * @param version Remote program version number.
     * @param port Port number at host where the remote program can be reached.
     * @param protocol {@link org.acplt.oncrpc.OncRpcProtocols Protocol} to be
     *   used for ONC/RPC calls.
     * @throws OncRpcException if an ONC/RPC error occurs.
     * @throws IOException if an I/O error occurs.
     */
    public vxi11_DEVICE_INTR_Client(InetAddress host, int program, int version, int port, int protocol)
           throws OncRpcException, IOException {
        super(host, program, version, port, protocol);
    }

    /**
     * Call remote procedure device_intr_srq_1.
     * @param arg1 parameter (of type Device_SrqParms) to the remote procedure call.
     * @throws OncRpcException if an ONC/RPC error occurs.
     * @throws IOException if an I/O error occurs.
     */
    public void device_intr_srq_1(Device_SrqParms arg1)
           throws OncRpcException, IOException {
        XdrVoid result$ = XdrVoid.XDR_VOID;
        client.call(vxi11.device_intr_srq_1, vxi11.DEVICE_INTR_VERSION, arg1, result$);
    }

}
// End of vxi11_DEVICE_INTR_Client.java
