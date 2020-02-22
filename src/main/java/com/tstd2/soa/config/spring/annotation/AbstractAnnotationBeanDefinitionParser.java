package com.tstd2.soa.config.spring.annotation;

import com.tstd2.soa.common.ClassScanner;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractAnnotationBeanDefinitionParser<A extends Annotation> implements BeanDefinitionParser {

    private Class<?> beanClass;

    public Class<?> getBeanClass() {
        return beanClass;
    }

    public AbstractAnnotationBeanDefinitionParser(Class<?> beanClass) {
        this.beanClass = beanClass;
    }

    @Override
    public BeanDefinition parse(Element element, ParserContext parserContext) {

        String packageToScan = element.getAttribute("package");

        String[] packagesToScan = packageToScan.split(",");

        registerServiceBeans(packagesToScan, parserContext.getRegistry());

        return new RootBeanDefinition(beanClass);
    }

    private void registerServiceBeans(String[] packagesToScan, BeanDefinitionRegistry registry) {

        // 扫描出所有@Serivce注解的类
        List<Class<?>> beanClassArr = this.scan(packagesToScan);

        // 注册bean
        for (Class<?> clazz : beanClassArr) {
            registerBeanClass(registry, clazz);
        }

    }

    protected abstract void registerBeanClass(BeanDefinitionRegistry registry, Class<?> clazz);

    protected abstract Class<A> getAnnotation();

    protected abstract String generateServiceBeanName(Class<?> beanClass, A annotation);

    private List<Class<?>> scan(String[] packagesToScan) {
        List<Class<?>> classList = new ArrayList<>();
        for (String pack : packagesToScan) {
            classList.addAll(ClassScanner.getClasses(pack));
        }
        return classList;
    }

}