package com.tstd2.soa.remoting.netty;

import com.tstd2.soa.remoting.netty.handler.NettyClientInHandler;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.codec.LengthFieldPrepender;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NettyChannelPool {

    private Map<String, Channel> channels = new ConcurrentHashMap<>();

    /**
     * 同步获取netty channel
     */
    public Channel syncGetChannel(String ip, int port) throws InterruptedException {

        // 取出对应ip port的channel
        String host = ip + ":" + port;
        Channel channel = channels.get(host);
        // 如果能获取到,直接返回
        if (channel != null && channel.isActive()) {
            return channel;
        }

        synchronized (host) {
            // 这里必须再次做判断,当锁被释放后，之前等待的线程已经可以直接拿到结果了。
            if (channel != null && channel.isActive()) {
                return channel;
            }

            // 开始跟服务端交互，获取channel
            channel = connectToServer(ip, port);

            channels.put(host, channel);
        }

        return channel;
    }

    private Channel connectToServer(String ip, int port) throws InterruptedException {
        // 异步调用
        // 基于NIO的非阻塞实现并行调用，客户端不需要启动多线程即可完成并行调用多个远程服务，相对多线程开销较小
        // 构建RpcProxyHandler异步处理响应的Handler
        final NettyClientInHandler nettyClientInHandler = new NettyClientInHandler();

        // netty
        EventLoopGroup group = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.group(group)
                .channel(NioSocketChannel.class)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .option(ChannelOption.TCP_NODELAY, true)
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ChannelPipeline pipeline = ch.pipeline();
                        pipeline.addLast(new LengthFieldBasedFrameDecoder(Integer.MAX_VALUE, 0, 4, 0, 4));
                        pipeline.addLast(new LengthFieldPrepender(4));
                        pipeline.addLast("encoder", new ObjectEncoder());
                        pipeline.addLast("decoder", new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.weakCachingConcurrentResolver(this.getClass().getClassLoader())));
                        pipeline.addLast(nettyClientInHandler);

                    }
                });

        ChannelFuture future = bootstrap.connect(ip, port);
        Channel channel = future.sync().channel();

        return channel;
    }
}