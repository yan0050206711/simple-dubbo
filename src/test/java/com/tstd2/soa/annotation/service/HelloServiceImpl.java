package com.tstd2.soa.annotation.service;

import com.tstd2.soa.config.annotation.Service;

/**
 * @author yancey
 * @date 2020/2/20 16:11
 */
@Service
public class HelloServiceImpl implements HelloService {

    @Override
    public void hello(String name) {
        System.out.println("hello " + name);
    }
}
