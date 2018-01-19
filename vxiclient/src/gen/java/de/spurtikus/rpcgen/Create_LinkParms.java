/*
 * Automatically generated by jrpcgen 1.1.3 on 19.01.18 21:22
 * jrpcgen is part of the "Remote Tea" ONC/RPC package for Java
 * See http://remotetea.sourceforge.net for details
 */
package de.spurtikus.rpcgen;
import org.acplt.oncrpc.*;
import java.io.IOException;

public class Create_LinkParms implements XdrAble {
    public int clientId;
    public boolean lockDevice;
    public int lock_timeout;
    public String device;

    public Create_LinkParms() {
    }

    public Create_LinkParms(XdrDecodingStream xdr)
           throws OncRpcException, IOException {
        xdrDecode(xdr);
    }

    public void xdrEncode(XdrEncodingStream xdr)
           throws OncRpcException, IOException {
        xdr.xdrEncodeInt(clientId);
        xdr.xdrEncodeBoolean(lockDevice);
        xdr.xdrEncodeInt(lock_timeout);
        xdr.xdrEncodeString(device);
    }

    public void xdrDecode(XdrDecodingStream xdr)
           throws OncRpcException, IOException {
        clientId = xdr.xdrDecodeInt();
        lockDevice = xdr.xdrDecodeBoolean();
        lock_timeout = xdr.xdrDecodeInt();
        device = xdr.xdrDecodeString();
    }

}
// End of Create_LinkParms.java
