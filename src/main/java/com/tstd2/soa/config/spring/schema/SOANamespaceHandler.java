package com.tstd2.soa.config.spring.schema;

import com.tstd2.soa.config.Protocol;
import com.tstd2.soa.config.Reference;
import com.tstd2.soa.config.Registry;
import com.tstd2.soa.config.Service;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class SOANamespaceHandler extends NamespaceHandlerSupport {
    @Override
    public void init() {
        registerBeanDefinitionParser("registry", new RegistryBeanDefinitionParse(Registry.class));
        registerBeanDefinitionParser("protocol", new ProtocolBeanDefinitionParse(Protocol.class));
        registerBeanDefinitionParser("reference", new ReferenceBeanDefinitionParse(Reference.class));
        registerBeanDefinitionParser("service", new ServiceBeanDefinitionParse(Service.class));
        registerBeanDefinitionParser("annotation", new AnnotationBeanDefinitionParser());
    }
}
