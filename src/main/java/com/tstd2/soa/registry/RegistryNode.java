package com.tstd2.soa.registry;

import com.tstd2.soa.common.ProtocolUrl;
import com.tstd2.soa.common.ServiceUrl;

public class RegistryNode {

    private ProtocolUrl protocol;
    private ServiceUrl service;

    public ProtocolUrl getProtocol() {
        return protocol;
    }

    public void setProtocol(ProtocolUrl protocol) {
        this.protocol = protocol;
    }

    public ServiceUrl getService() {
        return service;
    }

    public void setService(ServiceUrl service) {
        this.service = service;
    }

}
