package com.tstd2.soa.config.spring.parse;

import com.tstd2.soa.common.IpUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

/**
 * Protocol标签的解析类
 */
public class ProtocolBeanDefinitionParse implements BeanDefinitionParser {

    private Class<?> beanClass;

    public ProtocolBeanDefinitionParse(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {
        RootBeanDefinition beanDefinition = new RootBeanDefinition();
        // spring会把这个beanClass进行实例化
        beanDefinition.setBeanClass(beanClass);
        beanDefinition.setLazyInit(false);
        String name = element.getAttribute("name");
        String port = element.getAttribute("port");
        String host = element.getAttribute("host");
        String contextpath = element.getAttribute("contextpath");

        if (StringUtils.isBlank(name)) {
            name = "netty";
        }

        if (StringUtils.isBlank(host)) {
            try {
                host = IpUtil.getLocalHost();
            } catch (Exception e) {
                throw new RuntimeException("Protocol host is null");
            }
        }

        if (StringUtils.isBlank(port)) {
            throw new RuntimeException("Protocol port is null");
        }

        beanDefinition.getPropertyValues().addPropertyValue("name", name);
        beanDefinition.getPropertyValues().addPropertyValue("port", port);
        beanDefinition.getPropertyValues().addPropertyValue("host", host);
        beanDefinition.getPropertyValues().addPropertyValue("contextpath", contextpath);
        parserContext.getRegistry().registerBeanDefinition("Protocol-" + name, beanDefinition);

        return beanDefinition;
    }
}
