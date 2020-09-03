package com.tstd2.soa.registry;

import com.tstd2.soa.config.RegistryBean;
import com.tstd2.soa.config.SpringContextHolder;
import com.tstd2.soa.registry.support.RegistryListener;

import java.util.List;

public class BaseRegistryDelegate {

    public static void registry(String interfaceName) {
        RegistryBean registry = SpringContextHolder.getBean(RegistryBean.class);
        String protocol = registry.getProtocol();
        BaseRegistry registryBean = registry.getRegistryMap().get(protocol);
        registryBean.registry(interfaceName);
    }

    public static List<RegistryNode> getRegistry(String interfaceName) {
        RegistryBean registry = SpringContextHolder.getBean(RegistryBean.class);
        String protocol = registry.getProtocol();
        BaseRegistry registryBean = registry.getRegistryMap().get(protocol);

        // 获取注册列表
        List<RegistryNode> registryNodeList = registryBean.getRegistry(interfaceName);
        // 注册监听
        registryBean.subscribe(interfaceName, new RegistryListener(interfaceName));

        return registryNodeList;
    }

}
