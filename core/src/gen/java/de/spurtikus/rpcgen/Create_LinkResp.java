/*
 * Automatically generated by jrpcgen 1.1.3 on 15.02.18 17:41
 * jrpcgen is part of the "Remote Tea" ONC/RPC package for Java
 * See http://remotetea.sourceforge.net for details
 */
package de.spurtikus.rpcgen;
import org.acplt.oncrpc.*;
import java.io.IOException;

public class Create_LinkResp implements XdrAble {
    public Device_ErrorCode error;
    public Device_Link lid;
    public short abortPort;
    public int maxRecvSize;

    public Create_LinkResp() {
    }

    public Create_LinkResp(XdrDecodingStream xdr)
           throws OncRpcException, IOException {
        xdrDecode(xdr);
    }

    public void xdrEncode(XdrEncodingStream xdr)
           throws OncRpcException, IOException {
        error.xdrEncode(xdr);
        lid.xdrEncode(xdr);
        xdr.xdrEncodeShort(abortPort);
        xdr.xdrEncodeInt(maxRecvSize);
    }

    public void xdrDecode(XdrDecodingStream xdr)
           throws OncRpcException, IOException {
        error = new Device_ErrorCode(xdr);
        lid = new Device_Link(xdr);
        abortPort = xdr.xdrDecodeShort();
        maxRecvSize = xdr.xdrDecodeInt();
    }

}
// End of Create_LinkResp.java