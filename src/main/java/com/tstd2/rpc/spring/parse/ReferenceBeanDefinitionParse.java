package com.tstd2.rpc.spring.parse;

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

        if (id == null || "".equals(id)) {
            throw new RuntimeException("Reference id is null");
        }
        if (inf == null || "".equals(inf)) {
            throw new RuntimeException("Reference inf is null");
        }
        if (loadbalance == null || "".equals(loadbalance)) {
            throw new RuntimeException("Reference loadbalance is null");
        }
        if (protocol == null || "".equals(protocol)) {
            throw new RuntimeException("Reference protocol is null");
        }
        if (cluster == null || "".equals(cluster)) {
            throw new RuntimeException("Reference cluster is null");
        }
        if (retries == null || "".equals(retries)) {
            throw new RuntimeException("Reference retries is null");
        }


        beanDefinition.getPropertyValues().addPropertyValue("id", id);
        beanDefinition.getPropertyValues().addPropertyValue("inf", inf);
        beanDefinition.getPropertyValues().addPropertyValue("loadbalance", loadbalance);
        beanDefinition.getPropertyValues().addPropertyValue("protocol", protocol);
        beanDefinition.getPropertyValues().addPropertyValue("cluster", cluster);
        beanDefinition.getPropertyValues().addPropertyValue("retries", retries);
        parserContext.getRegistry().registerBeanDefinition("Registry" + id, beanDefinition);

        return beanDefinition;
    }
}
