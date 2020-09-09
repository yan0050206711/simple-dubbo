package com.tstd2.soa.registry;

import com.tstd2.soa.registry.support.NotifyListener;

import java.util.List;

public interface BaseRegistry {

    /**
     * 注册服务
     *
     * @param interfaceName
     * @param registryNode
     * @return
     */
    boolean registry(String interfaceName, RegistryNode registryNode);

    /**
     * 取消注册
     *
     * @param interfaceName
     * @param registryNode
     */
    void unregister(String interfaceName, RegistryNode registryNode);

    /**
     * 订阅服务
     *
     * @param interfaceName
     * @param listener
     */
    void subscribe(String interfaceName, NotifyListener listener);

    /**
     * 取消订阅
     *
     * @param interfaceName
     * @param listener
     */
    void unsubscribe(String interfaceName, NotifyListener listener);

    /**
     * 获取服务列表
     *
     * @param interfaceName
     * @return
     */
    List<RegistryNode> getRegistry(String interfaceName);

}
