package com.tstd2.soa.remoting.netty.server;

import com.tstd2.soa.config.Service;
import com.tstd2.soa.remoting.netty.ErrorCode;
import com.tstd2.soa.remoting.netty.model.Request;
import com.tstd2.soa.remoting.netty.model.Response;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.context.ApplicationContext;

import java.lang.reflect.Method;

public class ResponseClientUtil {

    public static boolean response(final Request request, Response response, ChannelHandlerContext ctx) {
        // 取出request对应sessionId
        response.setSessionId(request.getSessionId());

        try {
            Object result = reflect(request);
            response.setResult(result);
            response.setResultCode(ErrorCode.SUCCESS.errorCode);
        } catch (Exception e) {
            e.printStackTrace();
            response.setResultCode(ErrorCode.ERROR.errorCode);
            response.setErrorMsg(e.getMessage());
        }

        // netty异步的方式写回给客户端
        ctx.writeAndFlush(response).addListener(new ChannelFutureListener() {
            @Override
            public void operationComplete(ChannelFuture channelFuture) throws Exception {
                System.out.println("RPC Server Send response sessionId:" + request.getSessionId());
            }
        });
        return true;
    }

    private static Object reflect(Request request) throws Exception {
        // 从spring服务实例对象
        ApplicationContext application = Service.getApplicationContext();
        // TODO java反射性能较差，利用缓存提高
        Class<?> clazz = Class.forName(request.getClassName());
        Object serviceBean = application.getBean(clazz);

        // 代理对象里面方法名称和方法参数
        Method method = clazz.getMethod(request.getMethodName(), request.getParametersType());
        Object result = method.invoke(serviceBean, request.getParametersValue());
        return result;
    }

}
