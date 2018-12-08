package com.tstd2.rpc.registry;

import com.tstd2.rpc.configBean.Registry;
import org.springframework.context.ApplicationContext;

import java.util.List;

public class BaseRegistryDelegate {

    public static void registry(String interfaceName, ApplicationContext application) {
        Registry registry = application.getBean(Registry.class);
        String protocol = registry.getProtocol();
        BaseRegistry registryBean = registry.getRegistryMap().get(protocol);
        registryBean.registry(interfaceName, application);
    }

    public static List<RegistryNode> getRegistry(String interfaceName, ApplicationContext application) {
        Registry registry = application.getBean(Registry.class);
        String protocol = registry.getProtocol();
        BaseRegistry registryBean = registry.getRegistryMap().get(protocol);
        return registryBean.getRegistry(interfaceName, application);
    }

}
