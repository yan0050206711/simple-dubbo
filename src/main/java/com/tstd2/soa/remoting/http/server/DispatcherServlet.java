package com.tstd2.soa.remoting.http.server;

import com.tstd2.soa.common.JsonUtils;
import com.tstd2.soa.remoting.exchange.model.Request;
import com.tstd2.soa.remoting.exchange.model.Response;
import com.tstd2.soa.remoting.ResponseClientUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class DispatcherServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestJson = req.getParameter("q");
        Request request = JsonUtils.fromJson(requestJson, Request.class);

        Response response = new Response();
        response = ResponseClientUtil.response(request, response);

        PrintWriter writer = resp.getWriter();
        writer.write(JsonUtils.toJson(response));
        writer.flush();
        writer.close();
    }

}
