package com.tstd2.rpc.test;

import com.tstd2.rpc.test.service.CalcService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * @author yancey
 * @date 2018/12/7 21:16
 */
public class ClientTest {

    public static void main(String[] args) {

        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("spring-config-provider.xml");

        CalcService calc = (CalcService) applicationContext.getBean("calcService");
        int add = calc.add(1, 1);
        System.out.println("calc add result:[" + add + "]");

    }

}
