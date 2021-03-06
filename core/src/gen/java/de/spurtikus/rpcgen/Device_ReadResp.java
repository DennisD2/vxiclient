/*
 * Automatically generated by jrpcgen 1.1.3 on 15.02.18 17:41
 * jrpcgen is part of the "Remote Tea" ONC/RPC package for Java
 * See http://remotetea.sourceforge.net for details
 */
package de.spurtikus.rpcgen;
import org.acplt.oncrpc.*;
import java.io.IOException;

public class Device_ReadResp implements XdrAble {
    public Device_ErrorCode error;
    public int reason;
    public byte [] data;

    public Device_ReadResp() {
    }

    public Device_ReadResp(XdrDecodingStream xdr)
           throws OncRpcException, IOException {
        xdrDecode(xdr);
    }

    public void xdrEncode(XdrEncodingStream xdr)
           throws OncRpcException, IOException {
        error.xdrEncode(xdr);
        xdr.xdrEncodeInt(reason);
        xdr.xdrEncodeDynamicOpaque(data);
    }

    public void xdrDecode(XdrDecodingStream xdr)
           throws OncRpcException, IOException {
        error = new Device_ErrorCode(xdr);
        reason = xdr.xdrDecodeInt();
        data = xdr.xdrDecodeDynamicOpaque();
    }

}
// End of Device_ReadResp.java
