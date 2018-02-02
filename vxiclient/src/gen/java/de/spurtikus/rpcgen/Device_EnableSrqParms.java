/*
 * Automatically generated by jrpcgen 1.1.3 on 28.01.18 15:28
 * jrpcgen is part of the "Remote Tea" ONC/RPC package for Java
 * See http://remotetea.sourceforge.net for details
 */
package de.spurtikus.rpcgen;
import org.acplt.oncrpc.*;
import java.io.IOException;

public class Device_EnableSrqParms implements XdrAble {
    public Device_Link lid;
    public boolean enable;
    public byte [] handle;

    public Device_EnableSrqParms() {
    }

    public Device_EnableSrqParms(XdrDecodingStream xdr)
           throws OncRpcException, IOException {
        xdrDecode(xdr);
    }

    public void xdrEncode(XdrEncodingStream xdr)
           throws OncRpcException, IOException {
        lid.xdrEncode(xdr);
        xdr.xdrEncodeBoolean(enable);
        xdr.xdrEncodeDynamicOpaque(handle);
    }

    public void xdrDecode(XdrDecodingStream xdr)
           throws OncRpcException, IOException {
        lid = new Device_Link(xdr);
        enable = xdr.xdrDecodeBoolean();
        handle = xdr.xdrDecodeDynamicOpaque();
    }

}
// End of Device_EnableSrqParms.java
