/*
 * Automatically generated by jrpcgen 1.1.3 on 28.01.18 15:28
 * jrpcgen is part of the "Remote Tea" ONC/RPC package for Java
 * See http://remotetea.sourceforge.net for details
 */
package de.spurtikus.rpcgen;
import org.acplt.oncrpc.*;
import java.io.IOException;

public class Device_Flags implements XdrAble {

    public int value;

    public Device_Flags() {
    }

    public Device_Flags(int value) {
        this.value = value;
    }

    public Device_Flags(XdrDecodingStream xdr)
           throws OncRpcException, IOException {
        xdrDecode(xdr);
    }

    public void xdrEncode(XdrEncodingStream xdr)
           throws OncRpcException, IOException {
        xdr.xdrEncodeInt(value);
    }

    public void xdrDecode(XdrDecodingStream xdr)
           throws OncRpcException, IOException {
        value = xdr.xdrDecodeInt();
    }

}
// End of Device_Flags.java
