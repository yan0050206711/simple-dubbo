package com.tstd2.soa.remoting;

import com.esotericsoftware.reflectasm.MethodAccess;
import com.tstd2.soa.common.ReflectionCache;
import com.tstd2.soa.config.SpringContextHolder;
import com.tstd2.soa.remoting.exchange.model.Request;
import com.tstd2.soa.remoting.exchange.model.Response;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ResponseClientUtil {

    private static Implementation implementation = new Implementation();

    public static Response response(Request request, Response response) {
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

        return response;
    }

    private static Object reflect(Request request) throws Exception {
        // 从spring服务实例对象
        Object serviceBean = implementation.getImplementation(request.getClassName());

        // 代理对象里面方法名称和方法参数
//        Method method = clazz.getMethod(request.getMethodName(), request.getParametersType());
//        Object result = method.invoke(serviceBean, request.getParametersValue());

        // 用reflectasm提高反射性能
//        MethodAccess methodAccess = MethodAccess.get(serviceBean.getClass());
//        int methodIndex = methodAccess.getIndex(request.getMethodName(), request.getParametersType());
        MethodAccess methodAccess = ReflectionCache.putAndGetMethodAccess(serviceBean.getClass());
        int methodIndex = ReflectionCache.putAndGetMethodIndex(serviceBean.getClass(), request.getMethodName(), request.getParameterTypes(), methodAccess);
        Object result = methodAccess.invoke(serviceBean, methodIndex, request.getArguments());

        return result;
    }

    private static class Implementation {
        private Map<Class<?>, Object> implementationCache = new ConcurrentHashMap<>();

        public Object getImplementation(String service) {
            try {
                Class<?> clazz = ReflectionCache.putAndGetClass(service);
                Object serviceBean = null;
                try {
                    serviceBean = SpringContextHolder.getBean(clazz);
                } catch (Exception e) {
                }
                if (serviceBean == null) {
                    serviceBean = implementationCache.get(clazz);
                    if (serviceBean == null) {
                        String impl = service + "Impl";
                        Class<?> cl = ReflectionCache.putAndGetClass(impl);
                        serviceBean = cl.newInstance();
                        implementationCache.put(clazz, serviceBean);
                    }
                }
                return serviceBean;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

    }

}
