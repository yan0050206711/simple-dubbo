package com.tstd2.soa.config.spring.schema;

import com.tstd2.soa.common.StringUtils;
import com.tstd2.soa.config.spring.annotation.ReferenceAnnotationBeanPostProcessor;
import com.tstd2.soa.config.spring.annotation.ServiceAnnotationBeanPostProcessor;
import com.tstd2.soa.config.spring.util.BeanRegistrar;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.xml.AbstractSingleBeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.w3c.dom.Element;

import static org.springframework.util.StringUtils.commaDelimitedListToStringArray;
import static org.springframework.util.StringUtils.trimArrayElements;

/**
 * @author yancey
 * @date 2020/8/23 22:56
 */
public class AnnotationBeanDefinitionParser extends AbstractSingleBeanDefinitionParser {

    @Override
    protected void doParse(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {

        String packageToScan = element.getAttribute("package");

        String[] packagesToScan = trimArrayElements(commaDelimitedListToStringArray(packageToScan));

        builder.addConstructorArgValue(packagesToScan);

        builder.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);

        // Registers ReferenceAnnotationBeanPostProcessor
        registerReferenceAnnotationBeanPostProcessor(parserContext.getRegistry());
    }

    private void registerReferenceAnnotationBeanPostProcessor(BeanDefinitionRegistry registry) {

        // Register @Reference Annotation Bean Processor
        BeanRegistrar.registerInfrastructureBean(registry,
                StringUtils.lowerFirstCapse(ReferenceAnnotationBeanPostProcessor.class.getName()), ReferenceAnnotationBeanPostProcessor.class);

    }

    @Override
    protected boolean shouldGenerateIdAsFallback() {
        return true;
    }

    @Override
    protected Class<?> getBeanClass(Element element) {
        return ServiceAnnotationBeanPostProcessor.class;
    }
}
