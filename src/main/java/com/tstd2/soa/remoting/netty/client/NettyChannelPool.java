package com.tstd2.soa.remoting.netty.client;

import com.tstd2.soa.registry.RegistryNode;
import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;

public class NettyChannelPool {

    /**
     * 默认为每一个ip端口建议一个长连接
     * 单个长连接不适合大对象传输
     */
    private volatile Map<String, Channel[]> channelMap = new ConcurrentHashMap<>();

    private int connections;

    public NettyChannelPool(int connections) {
        this.connections = connections;
    }

    /**
     * 同步获取netty channel
     */
    public Channel syncGetChannel(RegistryNode.ProtocolUrl protocol, ConnectCall call) throws Exception {

        // 取出对应ip port的channel
        String host = protocol.getHost() + ":" + protocol.getPort();
        Channel[] channels = channelMap.get(host);

        if (channels == null) {
            synchronized (host.intern()) {
                if (channelMap.get(host) == null) {
                    channelMap.put(host, new Channel[connections]);
                }
            }
        }

        // 随机取出一个链接
        int index = connections == 1 ? 0 : ThreadLocalRandom.current().nextInt(connections);
        Channel channel = channelMap.get(host)[index];

        // 如果能获取到,直接返回
        if (channel != null && channel.isActive()) {
            return channel;
        }

        synchronized (host.intern()) {
            // 这里必须再次做判断,当锁被释放后，之前等待的线程已经可以直接拿到结果了。
            channel = channelMap.get(host)[index];
            if (channel != null && channel.isActive()) {
                return channel;
            }
            // 开始跟服务端交互，获取channel
            channel = call.connect(protocol);

            channelMap.get(host)[index] = channel;
        }

        return channel;
    }

    public interface ConnectCall {
        Channel connect(RegistryNode.ProtocolUrl protocol) throws Exception;
    }

}