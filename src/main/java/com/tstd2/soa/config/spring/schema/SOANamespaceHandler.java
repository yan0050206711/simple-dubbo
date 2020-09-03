package com.tstd2.soa.config.spring.schema;

import com.tstd2.soa.config.ProtocolBean;
import com.tstd2.soa.config.ReferenceBean;
import com.tstd2.soa.config.RegistryBean;
import com.tstd2.soa.config.ServiceBean;
import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

public class SOANamespaceHandler extends NamespaceHandlerSupport {
    @Override
    public void init() {
        registerBeanDefinitionParser("registry", new RegistryBeanDefinitionParse(RegistryBean.class));
        registerBeanDefinitionParser("protocol", new ProtocolBeanDefinitionParse(ProtocolBean.class));
        registerBeanDefinitionParser("reference", new ReferenceBeanDefinitionParse(ReferenceBean.class));
        registerBeanDefinitionParser("service", new ServiceBeanDefinitionParse(ServiceBean.class));
        registerBeanDefinitionParser("annotation", new AnnotationBeanDefinitionParser());
    }
}
