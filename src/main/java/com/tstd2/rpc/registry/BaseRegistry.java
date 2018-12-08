package com.tstd2.rpc.registry;

import org.springframework.context.ApplicationContext;

import java.util.List;

public interface BaseRegistry {

    boolean registry(String interfaceName, ApplicationContext application);

    List<RegistryNode> getRegistry(String interfaceName, ApplicationContext application);

}
