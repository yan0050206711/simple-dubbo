package com.tstd2.soa.remoting.http.client;

import com.tstd2.soa.common.serialize.hessian.HessianCodecUtil;
import com.tstd2.soa.config.ProtocolBean;
import com.tstd2.soa.remoting.exchange.model.Request;
import com.tstd2.soa.remoting.exchange.model.Response;
import org.springframework.util.Base64Utils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class HttpClient {

    public static Object request(ProtocolBean protocol, Request request, int timeout) throws IOException {

        // 请求url
        String url = "http://" + protocol.getHost() + ":" + protocol.getPort() + protocol.getContextpath();

        // 请求参数
        Map<String, String> requestMap = new HashMap<>(1);
        byte[] reqBody = HessianCodecUtil.encode(request);
        String q = Base64Utils.encodeToString(reqBody);
        requestMap.put("q", q);
        String result = HttpClientUtils.post(url, requestMap, timeout);

        // 返回参数
        byte[] respBody = Base64Utils.decodeFromString(result);
        Response response = (Response) HessianCodecUtil.decode(respBody);

        return response.getResult();
    }

}
