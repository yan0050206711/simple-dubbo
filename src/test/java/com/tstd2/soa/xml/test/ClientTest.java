package com.tstd2.soa.xml.test;

import com.tstd2.soa.xml.service.CalcService;
import com.tstd2.soa.xml.service.UserService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author yancey
 * @date 2018/12/7 21:16
 */
public class ClientTest {

    public static void main(String[] args) {

        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("xml/spring-config-consumer.xml");

        CalcService calc = applicationContext.getBean(CalcService.class);
        System.out.println(calc.add(1, 1));
        System.out.println(calc.add(2, 2));
        System.out.println(calc.add(3, 3));

        UserService userService = applicationContext.getBean(UserService.class);
        System.out.println(userService.sayHello("tstd"));
        System.out.println(userService.sayHello("yancey"));

    }

}
