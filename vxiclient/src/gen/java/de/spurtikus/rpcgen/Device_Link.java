/*
 * Automatically generated by jrpcgen 1.1.3 on 27.10.17 15:23
 * jrpcgen is part of the "Remote Tea" ONC/RPC package for Java
 * See http://remotetea.sourceforge.net for details
 */
package de.spurtikus.rpcgen;
import org.acplt.oncrpc.*;
import java.io.IOException;

public class Device_Link implements XdrAble {

    public int value;

    public Device_Link() {
    }

    public Device_Link(int value) {
        this.value = value;
    }

    public Device_Link(XdrDecodingStream xdr)
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
// End of Device_Link.java
