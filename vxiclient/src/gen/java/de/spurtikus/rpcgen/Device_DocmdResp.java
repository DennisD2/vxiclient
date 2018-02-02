/*
 * Automatically generated by jrpcgen 1.1.3 on 28.01.18 15:28
 * jrpcgen is part of the "Remote Tea" ONC/RPC package for Java
 * See http://remotetea.sourceforge.net for details
 */
package de.spurtikus.rpcgen;
import org.acplt.oncrpc.*;
import java.io.IOException;

public class Device_DocmdResp implements XdrAble {
    public Device_ErrorCode error;
    public byte [] data_out;

    public Device_DocmdResp() {
    }

    public Device_DocmdResp(XdrDecodingStream xdr)
           throws OncRpcException, IOException {
        xdrDecode(xdr);
    }

    public void xdrEncode(XdrEncodingStream xdr)
           throws OncRpcException, IOException {
        error.xdrEncode(xdr);
        xdr.xdrEncodeDynamicOpaque(data_out);
    }

    public void xdrDecode(XdrDecodingStream xdr)
           throws OncRpcException, IOException {
        error = new Device_ErrorCode(xdr);
        data_out = xdr.xdrDecodeDynamicOpaque();
    }

}
// End of Device_DocmdResp.java
