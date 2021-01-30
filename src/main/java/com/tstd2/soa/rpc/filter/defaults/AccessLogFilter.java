package com.tstd2.soa.rpc.filter.defaults;

import com.tstd2.soa.rpc.filter.Filter;
import com.tstd2.soa.rpc.invoke.Invocation;
import com.tstd2.soa.rpc.invoke.Invoker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author yancey
 * @date 2021/1/30 16:42
 */
public class AccessLogFilter implements Filter {

    private static final Logger LOGGER = LoggerFactory.getLogger(AccessLogFilter.class);

    @Override
    public Object invoke(Invoker invoker, Invocation invocation) {

        try {
            return invoker.invoke(invocation);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

}
