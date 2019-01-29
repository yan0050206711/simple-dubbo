package com.tstd2.soa.registry;

import org.springframework.context.ApplicationContext;

import java.util.List;

public interface BaseRegistry {

    /**
     * 注册服务
     *
     * @param interfaceName
     * @param application
     * @return
     */
    boolean registry(String interfaceName, ApplicationContext application);

    void unregister(String interfaceName, ServerNode serverfNode);

    void subscribe(ServerNode serverfNode, NotifyListener listener);

    void unsubscribe(ServerNode serverfNode, NotifyListener listener);

    /**
     * 获取服务列表
     *
     * @param interfaceName
     * @param application
     * @return
     */
    List<RegistryNode> getRegistry(String interfaceName, ApplicationContext application);

}
