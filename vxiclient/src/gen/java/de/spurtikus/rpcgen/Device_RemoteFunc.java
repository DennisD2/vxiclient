/*
 * Automatically generated by jrpcgen 1.1.3 on 27.10.17 15:14
 * jrpcgen is part of the "Remote Tea" ONC/RPC package for Java
 * See http://remotetea.sourceforge.net for details
 */
package de.spurtikus.rpcgen;
import org.acplt.oncrpc.*;
import java.io.IOException;

public class Device_RemoteFunc implements XdrAble {
    public int hostAddr;
    public int hostPort;
    public int progNum;
    public int progVers;
    public int progFamily;

    public Device_RemoteFunc() {
    }

    public Device_RemoteFunc(XdrDecodingStream xdr)
           throws OncRpcException, IOException {
        xdrDecode(xdr);
    }

    public void xdrEncode(XdrEncodingStream xdr)
           throws OncRpcException, IOException {
        xdr.xdrEncodeInt(hostAddr);
        xdr.xdrEncodeInt(hostPort);
        xdr.xdrEncodeInt(progNum);
        xdr.xdrEncodeInt(progVers);
        xdr.xdrEncodeInt(progFamily);
    }

    public void xdrDecode(XdrDecodingStream xdr)
           throws OncRpcException, IOException {
        hostAddr = xdr.xdrDecodeInt();
        hostPort = xdr.xdrDecodeInt();
        progNum = xdr.xdrDecodeInt();
        progVers = xdr.xdrDecodeInt();
        progFamily = xdr.xdrDecodeInt();
    }

}
// End of Device_RemoteFunc.java
