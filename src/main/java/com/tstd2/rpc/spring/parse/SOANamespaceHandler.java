package com.tstd2.rpc.spring.parse;

import com.tstd2.rpc.configBean.Protocol;
import com.tstd2.rpc.configBean.Reference;
import com.tstd2.rpc.configBean.Registry;
import com.tstd2.rpc.configBean.Service;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class SOANamespaceHandler extends NamespaceHandlerSupport {
    @Override
    public void init() {
        registerBeanDefinitionParser("registry", new RegistryBeanDefinitionParse(Registry.class));
        registerBeanDefinitionParser("protocol", new ProtocolBeanDefinitionParse(Protocol.class));
        registerBeanDefinitionParser("reference", new ReferenceBeanDefinitionParse(Reference.class));
        registerBeanDefinitionParser("service", new ServiceBeanDefinitionParse(Service.class));
    }
}
