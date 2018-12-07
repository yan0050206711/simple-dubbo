package com.tstd2.rpc.registry;

import org.springframework.context.ApplicationContext;

import java.util.List;

public interface BaseRegistry {

    boolean registry(String ref, ApplicationContext application);

    List<String> getRegistry(String id, ApplicationContext application);

}
