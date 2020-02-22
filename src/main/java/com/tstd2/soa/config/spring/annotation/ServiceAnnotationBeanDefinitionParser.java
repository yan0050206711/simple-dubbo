package com.tstd2.soa.config.spring.annotation;

import com.tstd2.soa.config.annotation.Service;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;

public class ServiceAnnotationBeanDefinitionParser extends AbstractAnnotationBeanDefinitionParser<Service> {

    public ServiceAnnotationBeanDefinitionParser(Class<?> beanClass) {
        super(beanClass);
    }

    @Override
    protected Class<Service> getAnnotation() {
        return Service.class;
    }

    @Override
    protected String generateServiceBeanName(Class<?> beanClass, Service service) {
        if (StringUtils.isNotBlank(service.value())) {
            return service.value();
        }

        String interfaceName = beanClass.getSimpleName();
        return com.tstd2.soa.common.StringUtils.lowerFirstCapse(interfaceName);
    }

    @Override
    protected void registerBeanClass(BeanDefinitionRegistry registry, Class<?> clazz) {
        Service annotation = clazz.getAnnotation(getAnnotation());
        if (annotation == null) {
            return;
        }

        // 注册业务service bean
        String beanName = generateServiceBeanName(clazz, annotation);
        RootBeanDefinition beanDefinition = new RootBeanDefinition(clazz);
        registry.registerBeanDefinition(beanName, beanDefinition);

        // 注册ServiceConfig
        RootBeanDefinition serviceBeanDefinition = new RootBeanDefinition(super.getBeanClass());
        serviceBeanDefinition.setLazyInit(false);

        String inf = annotation.interfaceName();
        String protocol = annotation.protocol();
        int timeout = annotation.timeout();

        Class<?>[] interfaces = clazz.getInterfaces();
        if (StringUtils.isBlank(inf)) {
            if (interfaces.length == 1) {
                inf = interfaces[0].getName();
            } else {
                throw new RuntimeException("Service inf is null");
            }
        }
        /**
         * 默认netty
         */
        if (StringUtils.isBlank(protocol)) {
            protocol = "netty";
        }

        /**
         * 默认1s
         */
        if (timeout == 0) {
            timeout = 1000;
        }

        serviceBeanDefinition.getPropertyValues().addPropertyValue("inf", inf);
        serviceBeanDefinition.getPropertyValues().addPropertyValue("protocol", protocol);
        serviceBeanDefinition.getPropertyValues().addPropertyValue("timeout", timeout);

        registry.registerBeanDefinition("Service-" + inf, serviceBeanDefinition);
    }

}