package com.tstd2.soa.annotation.test;

import com.tstd2.soa.annotation.service.HelloService;
import com.tstd2.soa.config.annotation.Reference;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author yancey
 * @date 2018/12/7 21:16
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:annotation/spring-config-consumer.xml")
public class ClientTest {

    @Reference
    private HelloService helloService;

    @Test
    public void testHello() {
        this.helloService.hello("yancey");
    }

}
