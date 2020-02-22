package com.tstd2.soa.config.spring.annotation;

import com.tstd2.soa.config.annotation.Reference;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;

import java.lang.reflect.Field;

public class ReferenceAnnotationBeanDefinitionParser extends AbstractAnnotationBeanDefinitionParser<Reference> {

    public ReferenceAnnotationBeanDefinitionParser(Class<?> beanClass) {
        super(beanClass);
    }

    @Override
    protected Class getAnnotation() {
        return Reference.class;
    }

    @Override
    protected String generateServiceBeanName(Class beanClass, Reference reference) {
        if (StringUtils.isNotBlank(reference.value())) {
            return reference.value();
        }

        String interfaceName = beanClass.getSimpleName();
        return com.tstd2.soa.common.StringUtils.lowerFirstCapse(interfaceName);
    }

    @Override
    protected void registerBeanClass(BeanDefinitionRegistry registry, Class clazz) {
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            Reference reference = field.getAnnotation(Reference.class);
            if (reference == null) {
                continue;
            }

            RootBeanDefinition beanDefinition = new RootBeanDefinition();
            // spring会把这个beanClass进行实例化
            beanDefinition.setBeanClass(super.getBeanClass());
            beanDefinition.setLazyInit(false);
            String id = this.generateServiceBeanName(field.getType(), reference);
            String inf = field.getType().getName();
            String loadbalance = reference.loadbalance();
            String protocol = reference.protocol();
            String cluster = reference.cluster();
            String retries = reference.retries();
            int timeout = reference.timeout();
            String proxy = reference.proxy();

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
            registry.registerBeanDefinition(id, beanDefinition);

        }
    }
}