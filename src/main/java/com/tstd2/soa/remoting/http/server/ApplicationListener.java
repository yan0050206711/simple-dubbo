package com.tstd2.soa.remoting.http.server;

import com.tstd2.soa.common.ThreadPoolFactory;
import com.tstd2.soa.config.Protocol;
import org.springframework.context.ApplicationContext;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.util.concurrent.ThreadPoolExecutor;

@WebListener
public class ApplicationListener implements ServletContextListener {

    // 从spring服务实例对象
    private ApplicationContext application = Protocol.getApplicationContext();
    // 拿到服务端协议配置
    private Protocol protocol = application.getBean(Protocol.class);

    int threads = Integer.parseInt(protocol.getThreads());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ThreadPoolExecutor executor = ThreadPoolFactory.initThreadPool(threads, protocol);
        sce.getServletContext().setAttribute("executor", executor);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        ThreadPoolExecutor executor = (ThreadPoolExecutor) sce.getServletContext().getAttribute("executor");
        executor.shutdown();
    }
}