/*
 * Automatically generated by jrpcgen 1.1.3 on 15.02.18 17:41
 * jrpcgen is part of the "Remote Tea" ONC/RPC package for Java
 * See http://remotetea.sourceforge.net for details
 */
package de.spurtikus.rpcgen;
import org.acplt.oncrpc.*;
import java.io.IOException;

public class Device_ReadStbResp implements XdrAble {
    public Device_ErrorCode error;
    public byte stb;

    public Device_ReadStbResp() {
    }

    public Device_ReadStbResp(XdrDecodingStream xdr)
           throws OncRpcException, IOException {
        xdrDecode(xdr);
    }

    public void xdrEncode(XdrEncodingStream xdr)
           throws OncRpcException, IOException {
        error.xdrEncode(xdr);
        xdr.xdrEncodeByte(stb);
    }

    public void xdrDecode(XdrDecodingStream xdr)
           throws OncRpcException, IOException {
        error = new Device_ErrorCode(xdr);
        stb = xdr.xdrDecodeByte();
    }

}
// End of Device_ReadStbResp.java
