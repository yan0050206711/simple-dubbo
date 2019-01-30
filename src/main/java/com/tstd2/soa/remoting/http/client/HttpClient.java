package com.tstd2.soa.remoting.http.client;

import com.tstd2.soa.common.JsonUtils;
import com.tstd2.soa.config.Protocol;
import com.tstd2.soa.remoting.exchange.model.Request;
import com.tstd2.soa.remoting.exchange.model.Response;

import java.util.HashMap;
import java.util.Map;

public class HttpClient {

    public static Object request(Protocol protocol, Request request, int timeout) {

        // 请求url
        String url = "http://" + protocol.getHost() + ":" + protocol.getPort() + protocol.getContextpath();

        Map<String, String> requestMap = new HashMap<>(1);
        requestMap.put("q", JsonUtils.toJson(request));
        String result = HttpClientUtils.post(url, requestMap, timeout);

        Response response = JsonUtils.fromJson(result, Response.class);

        return response.getResult();
    }

}
