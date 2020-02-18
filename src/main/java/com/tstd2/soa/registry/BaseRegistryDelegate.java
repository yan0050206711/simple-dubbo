package com.tstd2.soa.registry;

import com.tstd2.soa.config.Registry;
import com.tstd2.soa.config.SpringContextHolder;
import com.tstd2.soa.registry.support.RegistryListener;

import java.util.List;

public class BaseRegistryDelegate {

    public static void registry(String interfaceName) {
        Registry registry = SpringContextHolder.getBean(Registry.class);
        String protocol = registry.getProtocol();
        BaseRegistry registryBean = registry.getRegistryMap().get(protocol);
        registryBean.registry(interfaceName);
    }

    public static List<RegistryNode> getRegistry(String interfaceName) {
        Registry registry = SpringContextHolder.getBean(Registry.class);
        String protocol = registry.getProtocol();
        BaseRegistry registryBean = registry.getRegistryMap().get(protocol);

        // 获取注册列表
        List<RegistryNode> registryNodeList = registryBean.getRegistry(interfaceName);
        // 注册监听
        registryBean.subscribe(interfaceName, new RegistryListener(interfaceName));

        return registryNodeList;
    }

}
