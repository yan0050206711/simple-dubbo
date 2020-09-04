package com.tstd2.soa.annotation.test;

import com.tstd2.soa.annotation.service.HelloService;
import com.tstd2.soa.config.annotation.Reference;
import com.tstd2.soa.rpc.RpcContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.CompletableFuture;

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
        String result = this.helloService.hello("yancey");
        System.out.println(result);
    }

    @Test
    public void testHelloAsync() throws Exception {
        RpcContext.getContext().useAsync();
        this.helloService.hello("yancey");
        RpcContext.getContext().removeAsync();

        CompletableFuture<?> future = RpcContext.getContext().getFuture();
        future.whenComplete((result, e) -> {
            if (e == null) {
                System.out.println(result);
            } else {
                e.printStackTrace();
            }
        });
        System.out.println("other...");

        Thread.sleep(1000);
    }

}
