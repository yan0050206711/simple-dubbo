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
        String threads = element.getAttribute("threads");
        String serialize = element.getAttribute("serialize");

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
            port = "9999";
        }

        /**
         * 默认cpu核心数
         */
        if (StringUtils.isBlank(threads)) {
            threads = Runtime.getRuntime().availableProcessors() + "";
        }

        beanDefinition.getPropertyValues().addPropertyValue("name", name);
        beanDefinition.getPropertyValues().addPropertyValue("port", port);
        beanDefinition.getPropertyValues().addPropertyValue("host", host);
        beanDefinition.getPropertyValues().addPropertyValue("contextpath", contextpath);
        beanDefinition.getPropertyValues().addPropertyValue("threads", threads);
        beanDefinition.getPropertyValues().addPropertyValue("serialize", serialize);
        parserContext.getRegistry().registerBeanDefinition("Protocol-" + name, beanDefinition);

        return beanDefinition;
    }
}
