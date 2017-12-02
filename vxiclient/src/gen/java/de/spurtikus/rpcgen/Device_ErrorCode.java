/*
 * Automatically generated by jrpcgen 1.1.3 on 02.12.17 10:39
 * jrpcgen is part of the "Remote Tea" ONC/RPC package for Java
 * See http://remotetea.sourceforge.net for details
 */
package de.spurtikus.rpcgen;
import org.acplt.oncrpc.*;
import java.io.IOException;

public class Device_ErrorCode implements XdrAble {

    public int value;

    public Device_ErrorCode() {
    }

    public Device_ErrorCode(int value) {
        this.value = value;
    }

    public Device_ErrorCode(XdrDecodingStream xdr)
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
// End of Device_ErrorCode.java
