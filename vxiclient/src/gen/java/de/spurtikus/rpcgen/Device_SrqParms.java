/*
 * Automatically generated by jrpcgen 1.1.3 on 15.02.18 17:25
 * jrpcgen is part of the "Remote Tea" ONC/RPC package for Java
 * See http://remotetea.sourceforge.net for details
 */
package de.spurtikus.rpcgen;
import org.acplt.oncrpc.*;
import java.io.IOException;

public class Device_SrqParms implements XdrAble {
    public byte [] handle;

    public Device_SrqParms() {
    }

    public Device_SrqParms(XdrDecodingStream xdr)
           throws OncRpcException, IOException {
        xdrDecode(xdr);
    }

    public void xdrEncode(XdrEncodingStream xdr)
           throws OncRpcException, IOException {
        xdr.xdrEncodeDynamicOpaque(handle);
    }

    public void xdrDecode(XdrDecodingStream xdr)
           throws OncRpcException, IOException {
        handle = xdr.xdrDecodeDynamicOpaque();
    }

}
// End of Device_SrqParms.java
