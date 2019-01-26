package com.tstd2.soa.remoting.netty.serialize;

import com.tstd2.soa.remoting.netty.serialize.hessian.HessianDecoder;
import com.tstd2.soa.remoting.netty.serialize.hessian.HessianEncoder;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

/**
 * RPC消息序序列化协议选择器接口
 */
public class RpcSerializeFrame {

    public static void select(String serialize, ChannelPipeline pipeline) {

        switch (serialize) {
            case "hession2": {
                pipeline.addLast(new HessianEncoder());
                pipeline.addLast(new HessianDecoder());
                break;
            }
            default:
                pipeline.addLast(new ObjectEncoder());
                pipeline.addLast(new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.weakCachingConcurrentResolver(RpcSerializeFrame.class.getClassLoader())));
                break;
        }
    }
}