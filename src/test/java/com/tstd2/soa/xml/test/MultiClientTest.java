package com.tstd2.soa.xml.test;

import com.tstd2.soa.xml.service.CalcService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.concurrent.*;

public class MultiClientTest {
    public static void main(String[] args) {

        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("xml/spring-config-consumer.xml");

        final CalcService calc = applicationContext.getBean(CalcService.class);

        ExecutorService threadPool = Executors.newFixedThreadPool(200);
        CompletionService<Integer> completionService = new ExecutorCompletionService<>(threadPool);

        long start = System.currentTimeMillis();

        for (int i = 0; i < 1000000; i++) {
            final int x = i;
            completionService.submit(new Callable<Integer>() {

                @Override
                public Integer call() throws Exception {
                    return calc.add(x, x);
                }
            });

        }

        for (int i = 0; i < 1000000; i++) {
            try {
                completionService.take().get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

        System.out.println(System.currentTimeMillis() - start);
        threadPool.shutdown();
        System.exit(0);

    }
}
