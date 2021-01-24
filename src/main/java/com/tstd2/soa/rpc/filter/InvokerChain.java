package com.tstd2.soa.rpc.filter;

import com.tstd2.soa.rpc.invoke.Invocation;
import com.tstd2.soa.rpc.invoke.Invoker;

import java.util.ArrayList;
import java.util.List;

/**
 * @author yancey
 * @date 2021/1/23 16:01
 */
public class InvokerChain {

    private List<Filter> filters = new ArrayList<>();

    public InvokerChain addFilter(Filter filter) {
        filters.add(filter);
        return this;
    }

    public Invoker invoke(Invoker invoker) {
        Invoker last = invoker;
        for (Filter filter : filters) {
            final Invoker next = last;
            invoker = new Invoker() {
                @Override
                public Object invoke(Invocation invocation) throws Exception {
                    return filter.invoke(next, invocation);
                }
            };
        }
        return last;
    }
}
