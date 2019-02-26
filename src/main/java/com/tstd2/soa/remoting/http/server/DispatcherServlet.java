package com.tstd2.soa.remoting.http.server;

import com.tstd2.soa.common.serialize.hessian.HessianCodecUtil;
import com.tstd2.soa.remoting.ResponseClientUtil;
import com.tstd2.soa.remoting.exchange.model.Request;
import com.tstd2.soa.remoting.exchange.model.Response;
import org.springframework.util.Base64Utils;

import javax.servlet.AsyncContext;
import javax.servlet.ReadListener;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class DispatcherServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        AsyncContext asyncContext = req.startAsync();

        ThreadPoolExecutor executor = (ThreadPoolExecutor) req.getServletContext().getAttribute("executor");

        ServletInputStream inputStream = req.getInputStream();
        inputStream.setReadListener(new ReadListener() {
            @Override
            public void onDataAvailable() throws IOException {

            }

            @Override
            public void onAllDataRead() throws IOException {
                executor.execute(() -> {
                    try {
                        String result = invoke(req);
                        PrintWriter writer = asyncContext.getResponse().getWriter();
                        writer.write(result);
                        writer.flush();
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    asyncContext.complete();
                });
            }

            @Override
            public void onError(Throwable t) {
                asyncContext.complete();
            }
        });


    }

    private String invoke(HttpServletRequest req) throws IOException {
        // 解析请求参数
        String q = req.getParameter("q");
        byte[] reqBody = Base64Utils.decodeFromString(q);
        Request request = (Request) HessianCodecUtil.decode(reqBody);

        // 处理请求
        Response response = new Response();
        response = ResponseClientUtil.response(request, response);

        // 响应结果
        byte[] respBody = HessianCodecUtil.encode(response);
        String result = Base64Utils.encodeToString(respBody);

        return result;
    }

}
