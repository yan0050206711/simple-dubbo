package com.tstd2.soa.registry;

import com.tstd2.soa.config.ProtocolBean;
import com.tstd2.soa.config.ServiceBean;

public class RegistryNode {

    private ProtocolBean protocol;
    private ServiceBean service;

    public ProtocolBean getProtocol() {
        return protocol;
    }

    public void setProtocol(ProtocolBean protocol) {
        this.protocol = protocol;
    }

    public ServiceBean getService() {
        return service;
    }

    public void setService(ServiceBean service) {
        this.service = service;
    }
}
