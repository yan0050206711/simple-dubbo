package com.tstd2.rpc.test;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.io.IOException;

/**
 * @author yancey
 * @date 2018/12/7 21:17
 */
public class ServiceTest {

    public static void main(String[] args) throws IOException {
        new ClassPathXmlApplicationContext("spring-config-provider.xml");
        System.in.read();
    }
}
