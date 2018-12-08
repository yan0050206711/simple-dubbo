package com.tstd2.soa.config.spring.parse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * Registry标签的解析类
 */
public class RegistryBeanDefinitionParse implements BeanDefinitionParser {

    private Class<?> beanClass;

    public RegistryBeanDefinitionParse(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        RootBeanDefinition beanDefinition = new RootBeanDefinition();
        // spring会把这个beanClass进行实例化
        beanDefinition.setBeanClass(beanClass);
        beanDefinition.setLazyInit(false);
        String protocol = element.getAttribute("protocol");
        String address = element.getAttribute("address");

        /**
         * 默认协议redis
         */
        if (StringUtils.isBlank(protocol)) {
            protocol = "redis";
        }
        if (StringUtils.isBlank(address)) {
            throw new RuntimeException("Registry address is null");
        }

        beanDefinition.getPropertyValues().addPropertyValue("protocol", protocol);
        beanDefinition.getPropertyValues().addPropertyValue("address", address);
        parserContext.getRegistry().registerBeanDefinition("Registry" + address, beanDefinition);

        return beanDefinition;
    }
}
