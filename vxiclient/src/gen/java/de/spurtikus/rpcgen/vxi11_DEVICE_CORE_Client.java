/*
 * Automatically generated by jrpcgen 1.1.3 on 27.10.17 15:23
 * jrpcgen is part of the "Remote Tea" ONC/RPC package for Java
 * See http://remotetea.sourceforge.net for details
 */
package de.spurtikus.rpcgen;
import org.acplt.oncrpc.*;
import java.io.IOException;

import java.net.InetAddress;

/**
 * The class <code>vxi11_DEVICE_CORE_Client</code> implements the client stub proxy
 * for the DEVICE_CORE remote program. It provides method stubs
 * which, when called, in turn call the appropriate remote method (procedure).
 */
public class vxi11_DEVICE_CORE_Client extends OncRpcClientStub {

    /**
     * Constructs a <code>vxi11_DEVICE_CORE_Client</code> client stub proxy dummy.
     * @throws OncRpcException if an ONC/RPC error occurs.
     * @throws IOException if an I/O error occurs.
     */
    public vxi11_DEVICE_CORE_Client() throws OncRpcException, IOException {
        super(null);
    }

    /**
     * Constructs a <code>vxi11_DEVICE_CORE_Client</code> client stub proxy object
     * from which the DEVICE_CORE remote program can be accessed.
     * @param host Internet address of host where to contact the remote program.
     * @param protocol {@link org.acplt.oncrpc.OncRpcProtocols Protocol} to be
     *   used for ONC/RPC calls.
     * @throws OncRpcException if an ONC/RPC error occurs.
     * @throws IOException if an I/O error occurs.
     */
    public vxi11_DEVICE_CORE_Client(InetAddress host, int protocol)
           throws OncRpcException, IOException {
        super(host, vxi11.DEVICE_CORE, 1, 0, protocol);
    }

    /**
     * Constructs a <code>vxi11_DEVICE_CORE_Client</code> client stub proxy object
     * from which the DEVICE_CORE remote program can be accessed.
     * @param host Internet address of host where to contact the remote program.
     * @param port Port number at host where the remote program can be reached.
     * @param protocol {@link org.acplt.oncrpc.OncRpcProtocols Protocol} to be
     *   used for ONC/RPC calls.
     * @throws OncRpcException if an ONC/RPC error occurs.
     * @throws IOException if an I/O error occurs.
     */
    public vxi11_DEVICE_CORE_Client(InetAddress host, int port, int protocol)
           throws OncRpcException, IOException {
        super(host, vxi11.DEVICE_CORE, 1, port, protocol);
    }

    /**
     * Constructs a <code>vxi11_DEVICE_CORE_Client</code> client stub proxy object
     * from which the DEVICE_CORE remote program can be accessed.
     * @param client ONC/RPC client connection object implementing a particular
     *   protocol.
     * @throws OncRpcException if an ONC/RPC error occurs.
     * @throws IOException if an I/O error occurs.
     */
    public vxi11_DEVICE_CORE_Client(OncRpcClient client)
           throws OncRpcException, IOException {
        super(client);
    }

    /**
     * Constructs a <code>vxi11_DEVICE_CORE_Client</code> client stub proxy object
     * from which the DEVICE_CORE remote program can be accessed.
     * @param host Internet address of host where to contact the remote program.
     * @param program Remote program number.
     * @param version Remote program version number.
     * @param protocol {@link org.acplt.oncrpc.OncRpcProtocols Protocol} to be
     *   used for ONC/RPC calls.
     * @throws OncRpcException if an ONC/RPC error occurs.
     * @throws IOException if an I/O error occurs.
     */
    public vxi11_DEVICE_CORE_Client(InetAddress host, int program, int version, int protocol)
           throws OncRpcException, IOException {
        super(host, program, version, 0, protocol);
    }

    /**
     * Constructs a <code>vxi11_DEVICE_CORE_Client</code> client stub proxy object
     * from which the DEVICE_CORE remote program can be accessed.
     * @param host Internet address of host where to contact the remote program.
     * @param program Remote program number.
     * @param version Remote program version number.
     * @param port Port number at host where the remote program can be reached.
     * @param protocol {@link org.acplt.oncrpc.OncRpcProtocols Protocol} to be
     *   used for ONC/RPC calls.
     * @throws OncRpcException if an ONC/RPC error occurs.
     * @throws IOException if an I/O error occurs.
     */
    public vxi11_DEVICE_CORE_Client(InetAddress host, int program, int version, int port, int protocol)
           throws OncRpcException, IOException {
        super(host, program, version, port, protocol);
    }

    /**
     * Call remote procedure create_link_1.
     * @param arg1 parameter (of type Create_LinkParms) to the remote procedure call.
     * @return Result from remote procedure call (of type Create_LinkResp).
     * @throws OncRpcException if an ONC/RPC error occurs.
     * @throws IOException if an I/O error occurs.
     */
    public Create_LinkResp create_link_1(Create_LinkParms arg1)
           throws OncRpcException, IOException {
        Create_LinkResp result$ = new Create_LinkResp();
        client.call(vxi11.create_link_1, vxi11.DEVICE_CORE_VERSION, arg1, result$);
        return result$;
    }

    /**
     * Call remote procedure device_write_1.
     * @param arg1 parameter (of type Device_WriteParms) to the remote procedure call.
     * @return Result from remote procedure call (of type Device_WriteResp).
     * @throws OncRpcException if an ONC/RPC error occurs.
     * @throws IOException if an I/O error occurs.
     */
    public Device_WriteResp device_write_1(Device_WriteParms arg1)
           throws OncRpcException, IOException {
        Device_WriteResp result$ = new Device_WriteResp();
        client.call(vxi11.device_write_1, vxi11.DEVICE_CORE_VERSION, arg1, result$);
        return result$;
    }

    /**
     * Call remote procedure device_read_1.
     * @param arg1 parameter (of type Device_ReadParms) to the remote procedure call.
     * @return Result from remote procedure call (of type Device_ReadResp).
     * @throws OncRpcException if an ONC/RPC error occurs.
     * @throws IOException if an I/O error occurs.
     */
    public Device_ReadResp device_read_1(Device_ReadParms arg1)
           throws OncRpcException, IOException {
        Device_ReadResp result$ = new Device_ReadResp();
        client.call(vxi11.device_read_1, vxi11.DEVICE_CORE_VERSION, arg1, result$);
        return result$;
    }

    /**
     * Call remote procedure device_readstb_1.
     * @param arg1 parameter (of type Device_GenericParms) to the remote procedure call.
     * @return Result from remote procedure call (of type Device_ReadStbResp).
     * @throws OncRpcException if an ONC/RPC error occurs.
     * @throws IOException if an I/O error occurs.
     */
    public Device_ReadStbResp device_readstb_1(Device_GenericParms arg1)
           throws OncRpcException, IOException {
        Device_ReadStbResp result$ = new Device_ReadStbResp();
        client.call(vxi11.device_readstb_1, vxi11.DEVICE_CORE_VERSION, arg1, result$);
        return result$;
    }

    /**
     * Call remote procedure device_trigger_1.
     * @param arg1 parameter (of type Device_GenericParms) to the remote procedure call.
     * @return Result from remote procedure call (of type Device_Error).
     * @throws OncRpcException if an ONC/RPC error occurs.
     * @throws IOException if an I/O error occurs.
     */
    public Device_Error device_trigger_1(Device_GenericParms arg1)
           throws OncRpcException, IOException {
        Device_Error result$ = new Device_Error();
        client.call(vxi11.device_trigger_1, vxi11.DEVICE_CORE_VERSION, arg1, result$);
        return result$;
    }

    /**
     * Call remote procedure device_clear_1.
     * @param arg1 parameter (of type Device_GenericParms) to the remote procedure call.
     * @return Result from remote procedure call (of type Device_Error).
     * @throws OncRpcException if an ONC/RPC error occurs.
     * @throws IOException if an I/O error occurs.
     */
    public Device_Error device_clear_1(Device_GenericParms arg1)
           throws OncRpcException, IOException {
        Device_Error result$ = new Device_Error();
        client.call(vxi11.device_clear_1, vxi11.DEVICE_CORE_VERSION, arg1, result$);
        return result$;
    }

    /**
     * Call remote procedure device_remote_1.
     * @param arg1 parameter (of type Device_GenericParms) to the remote procedure call.
     * @return Result from remote procedure call (of type Device_Error).
     * @throws OncRpcException if an ONC/RPC error occurs.
     * @throws IOException if an I/O error occurs.
     */
    public Device_Error device_remote_1(Device_GenericParms arg1)
           throws OncRpcException, IOException {
        Device_Error result$ = new Device_Error();
        client.call(vxi11.device_remote_1, vxi11.DEVICE_CORE_VERSION, arg1, result$);
        return result$;
    }

    /**
     * Call remote procedure device_local_1.
     * @param arg1 parameter (of type Device_GenericParms) to the remote procedure call.
     * @return Result from remote procedure call (of type Device_Error).
     * @throws OncRpcException if an ONC/RPC error occurs.
     * @throws IOException if an I/O error occurs.
     */
    public Device_Error device_local_1(Device_GenericParms arg1)
           throws OncRpcException, IOException {
        Device_Error result$ = new Device_Error();
        client.call(vxi11.device_local_1, vxi11.DEVICE_CORE_VERSION, arg1, result$);
        return result$;
    }

    /**
     * Call remote procedure device_lock_1.
     * @param arg1 parameter (of type Device_LockParms) to the remote procedure call.
     * @return Result from remote procedure call (of type Device_Error).
     * @throws OncRpcException if an ONC/RPC error occurs.
     * @throws IOException if an I/O error occurs.
     */
    public Device_Error device_lock_1(Device_LockParms arg1)
           throws OncRpcException, IOException {
        Device_Error result$ = new Device_Error();
        client.call(vxi11.device_lock_1, vxi11.DEVICE_CORE_VERSION, arg1, result$);
        return result$;
    }

    /**
     * Call remote procedure device_unlock_1.
     * @param arg1 parameter (of type Device_Link) to the remote procedure call.
     * @return Result from remote procedure call (of type Device_Error).
     * @throws OncRpcException if an ONC/RPC error occurs.
     * @throws IOException if an I/O error occurs.
     */
    public Device_Error device_unlock_1(Device_Link arg1)
           throws OncRpcException, IOException {
        Device_Error result$ = new Device_Error();
        client.call(vxi11.device_unlock_1, vxi11.DEVICE_CORE_VERSION, arg1, result$);
        return result$;
    }

    /**
     * Call remote procedure device_enable_srq_1.
     * @param arg1 parameter (of type Device_EnableSrqParms) to the remote procedure call.
     * @return Result from remote procedure call (of type Device_Error).
     * @throws OncRpcException if an ONC/RPC error occurs.
     * @throws IOException if an I/O error occurs.
     */
    public Device_Error device_enable_srq_1(Device_EnableSrqParms arg1)
           throws OncRpcException, IOException {
        Device_Error result$ = new Device_Error();
        client.call(vxi11.device_enable_srq_1, vxi11.DEVICE_CORE_VERSION, arg1, result$);
        return result$;
    }

    /**
     * Call remote procedure device_docmd_1.
     * @param arg1 parameter (of type Device_DocmdParms) to the remote procedure call.
     * @return Result from remote procedure call (of type Device_DocmdResp).
     * @throws OncRpcException if an ONC/RPC error occurs.
     * @throws IOException if an I/O error occurs.
     */
    public Device_DocmdResp device_docmd_1(Device_DocmdParms arg1)
           throws OncRpcException, IOException {
        Device_DocmdResp result$ = new Device_DocmdResp();
        client.call(vxi11.device_docmd_1, vxi11.DEVICE_CORE_VERSION, arg1, result$);
        return result$;
    }

    /**
     * Call remote procedure destroy_link_1.
     * @param arg1 parameter (of type Device_Link) to the remote procedure call.
     * @return Result from remote procedure call (of type Device_Error).
     * @throws OncRpcException if an ONC/RPC error occurs.
     * @throws IOException if an I/O error occurs.
     */
    public Device_Error destroy_link_1(Device_Link arg1)
           throws OncRpcException, IOException {
        Device_Error result$ = new Device_Error();
        client.call(vxi11.destroy_link_1, vxi11.DEVICE_CORE_VERSION, arg1, result$);
        return result$;
    }

    /**
     * Call remote procedure create_intr_chan_1.
     * @param arg1 parameter (of type Device_RemoteFunc) to the remote procedure call.
     * @return Result from remote procedure call (of type Device_Error).
     * @throws OncRpcException if an ONC/RPC error occurs.
     * @throws IOException if an I/O error occurs.
     */
    public Device_Error create_intr_chan_1(Device_RemoteFunc arg1)
           throws OncRpcException, IOException {
        Device_Error result$ = new Device_Error();
        client.call(vxi11.create_intr_chan_1, vxi11.DEVICE_CORE_VERSION, arg1, result$);
        return result$;
    }

    /**
     * Call remote procedure destroy_intr_chan_1.
     * @return Result from remote procedure call (of type Device_Error).
     * @throws OncRpcException if an ONC/RPC error occurs.
     * @throws IOException if an I/O error occurs.
     */
    public Device_Error destroy_intr_chan_1()
           throws OncRpcException, IOException {
        XdrVoid args$ = XdrVoid.XDR_VOID;
        Device_Error result$ = new Device_Error();
        client.call(vxi11.destroy_intr_chan_1, vxi11.DEVICE_CORE_VERSION, args$, result$);
        return result$;
    }

}
// End of vxi11_DEVICE_CORE_Client.java
