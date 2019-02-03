package com.tstd2.soa.common.serialize.hessian;

import com.google.common.io.Closer;
import io.netty.buffer.ByteBuf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Hessian编解码工具类
 */
public class HessianCodecUtil {

    static HessianSerializePool pool = HessianSerializePool.getHessianPoolInstance();
    private static Closer closer = Closer.create();

    public HessianCodecUtil() {

    }

    public static byte[] encode(final Object message) throws IOException {
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
//            closer.register(byteArrayOutputStream);
            HessianSerialize hessianSerialization = pool.borrow();
            hessianSerialization.serialize(byteArrayOutputStream, message);
            byte[] body = byteArrayOutputStream.toByteArray();
            pool.restore(hessianSerialization);
            return body;
        } finally {
//            closer.close();
        }
    }

    public static Object decode(byte[] body) throws IOException {
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(body);
//            closer.register(byteArrayInputStream);
            HessianSerialize hessianSerialization = pool.borrow();
            Object object = hessianSerialization.deserialize(byteArrayInputStream);
            pool.restore(hessianSerialization);
            return object;
        } finally {
//            closer.close();
        }
    }
}