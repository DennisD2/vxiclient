/*
 * Automatically generated by jrpcgen 1.1.3 on 19.01.18 21:37
 * jrpcgen is part of the "Remote Tea" ONC/RPC package for Java
 * See http://remotetea.sourceforge.net for details
 */
package de.spurtikus.rpcgen;
import org.acplt.oncrpc.*;
import java.io.IOException;

import java.net.InetAddress;

import org.acplt.oncrpc.server.*;

/**
 */
public abstract class vxi11_DEVICE_ASYNC_ServerStub extends OncRpcServerStub implements OncRpcDispatchable {

    public vxi11_DEVICE_ASYNC_ServerStub()
           throws OncRpcException, IOException {
        this(0);
    }

    public vxi11_DEVICE_ASYNC_ServerStub(int port)
           throws OncRpcException, IOException {
        this(null, port);
    }

    public vxi11_DEVICE_ASYNC_ServerStub(InetAddress bindAddr, int port)
           throws OncRpcException, IOException {
        info = new OncRpcServerTransportRegistrationInfo [] {
            new OncRpcServerTransportRegistrationInfo(vxi11.DEVICE_ASYNC, 1),
        };
        transports = new OncRpcServerTransport [] {
            new OncRpcUdpServerTransport(this, bindAddr, port, info, 32768)
            ,
            new OncRpcTcpServerTransport(this, bindAddr, port, info, 32768)
        };
    }

    public void dispatchOncRpcCall(OncRpcCallInformation call, int program, int version, int procedure)
           throws OncRpcException, IOException {
        if ( version == 1 ) {
            switch ( procedure ) {
            case 1: {
                Device_Link args$ = new Device_Link();
                call.retrieveCall(args$);
                Device_Error result$ = device_abort_1(args$);
                call.reply(result$);
                break;
            }
            case 0: {
                call.retrieveCall(XdrVoid.XDR_VOID);
                call.reply(XdrVoid.XDR_VOID);
                break;
            }
            default:
                call.failProcedureUnavailable();
            }
        } else {
            call.failProgramUnavailable();
        }
    }

    public abstract Device_Error device_abort_1(Device_Link arg1);

}
// End of vxi11_DEVICE_ASYNC_ServerStub.java
