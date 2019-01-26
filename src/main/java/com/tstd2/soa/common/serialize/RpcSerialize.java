package com.tstd2.soa.common.serialize;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * RPC消息序列化/反序列化接口定义
 */
public interface RpcSerialize {

    void serialize(OutputStream output, Object object) throws IOException;

    Object deserialize(InputStream input) throws IOException;
}