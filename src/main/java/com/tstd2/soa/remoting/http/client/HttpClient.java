package com.tstd2.soa.remoting.http.client;

import com.tstd2.soa.common.JsonUtils;
import com.tstd2.soa.config.Protocol;
import com.tstd2.soa.registry.RegistryNode;
import com.tstd2.soa.remoting.exchange.model.Request;
import com.tstd2.soa.remoting.exchange.model.Response;
import com.tstd2.soa.rpc.invoke.Invocation;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class HttpClient {

    public static Object request(RegistryNode nodeInfo, Invocation invocation) {

        Request request = new Request();
        request.setSessionId(UUID.randomUUID().toString());
        request.setClassName(invocation.getReference().getInf());
        request.setMethodName(invocation.getMethod().getName());
        request.setParametersType(invocation.getMethod().getParameterTypes());
        request.setParametersValue(invocation.getObjs());

        Protocol protocol = nodeInfo.getProtocol();
        // 请求url
        String url = "http://" + protocol.getHost() + ":" + protocol.getPort() + protocol.getContextpath();

        // 默认用客户端配置的超时时间，客户端没有配置超时时间则用提供方的
        Integer timeout = Integer.parseInt(invocation.getReference().getTimeout());
        if (timeout == null || timeout == 0) {
            timeout = Integer.parseInt(nodeInfo.getService().getTimeout());
        }

        Map<String, String> requestMap = new HashMap<>(1);
        requestMap.put("q", JsonUtils.toJson(request));
        String result = HttpClientUtils.post(url, requestMap, timeout);

        Response response = JsonUtils.fromJson(result, Response.class);

        return response.getResultCode();
    }

}
