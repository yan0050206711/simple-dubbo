package com.tstd2.soa.config.spring.annotation;

import com.tstd2.soa.common.ClassScanner;
import com.tstd2.soa.config.ServiceBean;
import com.tstd2.soa.config.annotation.Service;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.beans.factory.support.BeanDefinitionBuilder.rootBeanDefinition;

public class ServiceAnnotationBeanPostProcessor implements BeanDefinitionRegistryPostProcessor {

    private final String[] packagesToScan;

    public ServiceAnnotationBeanPostProcessor(String[] packagesToScan) {
        this.packagesToScan = packagesToScan;
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        // 扫描出所有@Serivce注解的类
        List<Class<?>> beanClassArr = this.scan(packagesToScan);

        // 注册bean
        for (Class<?> clazz : beanClassArr) {
            Service annotation = clazz.getAnnotation(Service.class);
            if (annotation == null) {
                continue;
            }

            // 注册业务service bean
            String beanName = generateServiceBeanName(clazz, annotation);
            AbstractBeanDefinition serviceBeanDefinition = buildServiceBeanDefinition(annotation, clazz, beanName);

            registry.registerBeanDefinition("Service-" + generateInterfaceName(clazz), serviceBeanDefinition);
        }
    }

    protected String generateServiceBeanName(Class<?> beanClass, Service service) {
        if (StringUtils.isNotBlank(service.value())) {
            return service.value();
        }

        String interfaceName = beanClass.getSimpleName();
        return com.tstd2.soa.common.StringUtils.lowerFirstCapse(interfaceName);
    }

    protected String generateInterfaceName(Class<?> beanClass) {
        Class<?>[] interfaces = beanClass.getInterfaces();
        String interfaceName;
        if (interfaces.length == 1) {
            interfaceName = interfaces[0].getName();
        } else {
            throw new RuntimeException("Service interfaceName is null");
        }
        return interfaceName;
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

    }

    private List<Class<?>> scan(String[] packagesToScan) {
        List<Class<?>> classList = new ArrayList<>();
        for (String pack : packagesToScan) {
            classList.addAll(ClassScanner.getClasses(pack));
        }
        return classList;
    }

    private AbstractBeanDefinition buildServiceBeanDefinition(Service service, Class<?> interfaceClass,
                                                              String annotatedServiceBeanName) {

        BeanDefinitionBuilder builder = rootBeanDefinition(ServiceBean.class);
        AbstractBeanDefinition beanDefinition = builder.getBeanDefinition();

        String interfaceName = service.interfaceName();
        String protocol = service.protocol();
        int timeout = service.timeout();

        Class<?>[] interfaces = interfaceClass.getInterfaces();
        if (StringUtils.isBlank(interfaceName)) {
            interfaceName = generateInterfaceName(interfaceClass);
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

        builder.addPropertyValue("ref", annotatedServiceBeanName);
        builder.addPropertyValue("inf", interfaceName);
        builder.addPropertyValue("protocol", protocol);
        builder.addPropertyValue("timeout", timeout);

        return builder.getBeanDefinition();
    }
}