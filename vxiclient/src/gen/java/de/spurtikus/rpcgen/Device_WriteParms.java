/*
 * Automatically generated by jrpcgen 1.1.3 on 04.10.17 17:58
 * jrpcgen is part of the "Remote Tea" ONC/RPC package for Java
 * See http://remotetea.sourceforge.net for details
 */
package de.spurtikus.rpcgen;
import org.acplt.oncrpc.*;
import java.io.IOException;

public class Device_WriteParms implements XdrAble {
    public Device_Link lid;
    public int io_timeout;
    public int lock_timeout;
    public Device_Flags flags;
    public byte [] data;

    public Device_WriteParms() {
    }

    public Device_WriteParms(XdrDecodingStream xdr)
           throws OncRpcException, IOException {
        xdrDecode(xdr);
    }

    public void xdrEncode(XdrEncodingStream xdr)
           throws OncRpcException, IOException {
        lid.xdrEncode(xdr);
        xdr.xdrEncodeInt(io_timeout);
        xdr.xdrEncodeInt(lock_timeout);
        flags.xdrEncode(xdr);
        xdr.xdrEncodeDynamicOpaque(data);
    }

    public void xdrDecode(XdrDecodingStream xdr)
           throws OncRpcException, IOException {
        lid = new Device_Link(xdr);
        io_timeout = xdr.xdrDecodeInt();
        lock_timeout = xdr.xdrDecodeInt();
        flags = new Device_Flags(xdr);
        data = xdr.xdrDecodeDynamicOpaque();
    }

}
// End of Device_WriteParms.java
