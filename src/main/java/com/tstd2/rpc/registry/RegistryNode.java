package com.tstd2.rpc.registry;

import com.tstd2.rpc.configBean.Protocol;
import com.tstd2.rpc.configBean.Service;

public class RegistryNode {

    private Protocol protocol;
    private Service service;

    public Protocol getProtocol() {
        return protocol;
    }

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }

    public Service getService() {
        return service;
    }

    public void setService(Service service) {
        this.service = service;
    }
}
