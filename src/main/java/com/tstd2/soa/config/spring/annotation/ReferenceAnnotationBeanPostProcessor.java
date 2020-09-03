package com.tstd2.soa.config.spring.annotation;

import com.tstd2.soa.config.ReferenceBean;
import com.tstd2.soa.config.annotation.Reference;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.annotation.InjectionMetadata;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessorAdapter;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.util.ReflectionUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.LinkedList;
import java.util.List;

public class ReferenceAnnotationBeanPostProcessor extends InstantiationAwareBeanPostProcessorAdapter implements ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public PropertyValues postProcessPropertyValues(PropertyValues pvs, PropertyDescriptor[] pds, Object bean, String beanName) throws BeansException {
        List<InjectionMetadata.InjectedElement> elements = findFieldReferenceMetadata(bean.getClass());
        InjectionMetadata metadata = new InjectionMetadata(bean.getClass(), elements);

        try {
            metadata.inject(bean, beanName, pvs);
        } catch (Throwable e) {
            throw new BeanCreationException(beanName, "Injection of @Reference dependencies is failed", e);
        }

        return pvs;
    }


    private List<InjectionMetadata.InjectedElement> findFieldReferenceMetadata(final Class<?> beanClass) {
        final List<InjectionMetadata.InjectedElement> elements = new LinkedList<>();

        ReflectionUtils.doWithFields(beanClass, field -> {
            Reference reference = field.getAnnotation(Reference.class);
            if (reference != null) {
                if (Modifier.isStatic(field.getModifiers())) {
                    return;
                }

                InjectionMetadata.InjectedElement element = new AnnotatedFieldElement(field, reference);
                elements.add(element);
            }
        });

        return elements;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    private class AnnotatedFieldElement extends InjectionMetadata.InjectedElement {

        private final Field field;

        private final Reference reference;

        private volatile ReferenceBean referenceBean;


        protected AnnotatedFieldElement(Field field, Reference reference) {
            super(field, null);
            this.field = field;
            this.reference = reference;
        }

        @Override
        protected void inject(Object bean, String beanName, PropertyValues pvs) throws Throwable {
            // 获取字段类型
            Class<?> referenceClass = field.getType();
            referenceBean = buildReferenceBean(reference, referenceClass);
            // 字段为私有，设置setAccessible(true)
            ReflectionUtils.makeAccessible(field);
            field.set(bean, referenceBean.getObject());
        }

        private ReferenceBean buildReferenceBean(Reference reference, Class<?> referenceClass) throws Exception {
            ReferenceBean referenceBean = new ReferenceBean();
            referenceBean.setApplicationContext(applicationContext);
            referenceBean.setInf(referenceClass.getName());
            referenceBean.setLoadbalance(StringUtils.isBlank(reference.loadbalance()) ? "random" : reference.loadbalance());
            referenceBean.setCluster(StringUtils.isBlank(reference.cluster()) ? "failover" : reference.cluster());
            referenceBean.setProtocol(StringUtils.isBlank(reference.protocol()) ? "netty" : reference.protocol());
            referenceBean.setProxy(StringUtils.isBlank(reference.proxy()) ? "javassist" : reference.proxy());
            referenceBean.setRetries(StringUtils.isBlank(reference.retries()) ? "1" : reference.retries());
            referenceBean.setTimeout(String.valueOf(reference.timeout()));
            referenceBean.afterPropertiesSet();
            return referenceBean;
        }
    }
}