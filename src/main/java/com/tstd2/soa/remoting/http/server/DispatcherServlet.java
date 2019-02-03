package com.tstd2.soa.remoting.http.server;

import com.tstd2.soa.common.serialize.hessian.HessianCodecUtil;
import com.tstd2.soa.remoting.ResponseClientUtil;
import com.tstd2.soa.remoting.exchange.model.Request;
import com.tstd2.soa.remoting.exchange.model.Response;
import org.springframework.util.Base64Utils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class DispatcherServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 解析请求参数
        String q = req.getParameter("q");
        byte[] reqBody = Base64Utils.decodeFromString(q);
        Request request = (Request) HessianCodecUtil.decode(reqBody);

        // 处理请求
        Response response = new Response();
        response = ResponseClientUtil.response(request, response);

        // 响应结果
        PrintWriter writer = resp.getWriter();
        byte[] respBody = HessianCodecUtil.encode(response);
        String result = Base64Utils.encodeToString(respBody);
        writer.write(result);
        writer.flush();
        writer.close();
    }

}
