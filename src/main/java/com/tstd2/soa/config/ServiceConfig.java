package com.tstd2.soa.config;

import com.tstd2.soa.registry.BaseRegistryDelegate;
import com.tstd2.soa.remoting.Server;
import com.tstd2.soa.remoting.netty.server.NettyServer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author yancey
 * @date 2020/8/23 22:23
 */
public class ServiceConfig {

    private String interfaceName;

    protected boolean export;

    private final static transient Map<String, Server> SERVER_MAP = new ConcurrentHashMap<>();

    /**
     * 将服务发布
     */
    public void export() {
        if (!export) {

            /**
             * 启动netty服务
             */
            ProtocolBean protocolBean = SpringContextHolder.getBean(ProtocolBean.class);
            if ("netty".equals(protocolBean.getName())) {
                Server server = SERVER_MAP.get(protocolBean.getPort());
                if (server == null) {
                    server = new NettyServer(Integer.parseInt(protocolBean.getPort()), protocolBean.getSerialize());
                    Server finalServer = server;
                    new Thread(() -> finalServer.doOpen()).start();
                }
            }

            /**
             * 将本service注册到注册中心
             */
            BaseRegistryDelegate.registry(getInterfaceName());
            export = true;
        }
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }
}
