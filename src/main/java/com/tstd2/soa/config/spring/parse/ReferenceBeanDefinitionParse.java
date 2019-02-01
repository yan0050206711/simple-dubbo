package com.tstd2.soa.config.spring.parse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * Reference标签的解析类
 */
public class ReferenceBeanDefinitionParse implements BeanDefinitionParser {

    private Class<?> beanClass;

    public ReferenceBeanDefinitionParse(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        RootBeanDefinition beanDefinition = new RootBeanDefinition();
        // spring会把这个beanClass进行实例化
        beanDefinition.setBeanClass(beanClass);
        beanDefinition.setLazyInit(false);
        String id = element.getAttribute("id");
        String inf = element.getAttribute("interface");
        String loadbalance = element.getAttribute("loadbalance");
        String protocol = element.getAttribute("protocol");
        String cluster = element.getAttribute("cluster");
        String retries = element.getAttribute("retries");
        String timeout = element.getAttribute("timeout");
        String proxy = element.getAttribute("proxy");

        if (StringUtils.isBlank(id)) {
            throw new RuntimeException("Reference id is null");
        }
        if (StringUtils.isBlank(inf)) {
            throw new RuntimeException("Reference inf is null");
        }
        if (StringUtils.isBlank(loadbalance)) {
            loadbalance = "random";
        }
        /**
         * 默认netty
         */
        if (StringUtils.isBlank(protocol)) {
            protocol = "netty";
        }
        if (StringUtils.isBlank(cluster)) {
            cluster = "failover";
        }
        /**
         * 默认不重试
         */
        if (StringUtils.isBlank(retries)) {
            retries = "1";
        }
        /**
         * 默认javassist
         */
        if (StringUtils.isBlank(proxy)) {
            proxy = "javassist";
        }

        beanDefinition.getPropertyValues().addPropertyValue("id", id);
        beanDefinition.getPropertyValues().addPropertyValue("inf", inf);
        beanDefinition.getPropertyValues().addPropertyValue("loadbalance", loadbalance);
        beanDefinition.getPropertyValues().addPropertyValue("protocol", protocol);
        beanDefinition.getPropertyValues().addPropertyValue("cluster", cluster);
        beanDefinition.getPropertyValues().addPropertyValue("retries", retries);
        beanDefinition.getPropertyValues().addPropertyValue("timeout", timeout);
        beanDefinition.getPropertyValues().addPropertyValue("proxy", proxy);
        parserContext.getRegistry().registerBeanDefinition(id, beanDefinition);

        return beanDefinition;
    }
}
