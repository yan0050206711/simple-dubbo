package com.tstd2.rpc.spring.parse;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * Service标签的解析类
 */
public class ServiceBeanDefinitionParse implements BeanDefinitionParser {

    private Class<?> beanClass;

    public ServiceBeanDefinitionParse(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        RootBeanDefinition beanDefinition = new RootBeanDefinition();
        // spring会把这个beanClass进行实例化
        beanDefinition.setBeanClass(beanClass);
        beanDefinition.setLazyInit(false);
        String inf = element.getAttribute("interface");
        String ref = element.getAttribute("ref");
        String protocol = element.getAttribute("protocol");

        if (inf == null || "".equals(inf)) {
            throw new RuntimeException("Service inf is null");
        }
        if (ref == null || "".equals(ref)) {
            throw new RuntimeException("Service ref is null");
        }
        if (protocol == null || "".equals(protocol)) {
            throw new RuntimeException("Service protocol is null");
        }

        beanDefinition.getPropertyValues().addPropertyValue("inf", inf);
        beanDefinition.getPropertyValues().addPropertyValue("ref", ref);
        beanDefinition.getPropertyValues().addPropertyValue("protocol", protocol);
        parserContext.getRegistry().registerBeanDefinition("Registry" + ref, beanDefinition);

        return beanDefinition;
    }
}
