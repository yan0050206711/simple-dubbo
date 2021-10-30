package com.tstd2.soa.rpc.protocol;

import com.tstd2.soa.rpc.filter.Filter;
import com.tstd2.soa.rpc.filter.InvokerChain;
import com.tstd2.soa.rpc.invoke.Invoker;

import java.util.ServiceLoader;

/**
 * @author yancey
 * @date 2021/1/23 16:10
 */
public class ProtocolFilterWrapper implements Protocol {

    private InvokerChain invokerChain = null;

    @Override
    public <T> Invoker refer(Class<T> interfaceClass, Invoker invoker) {
        if (invokerChain == null) {
            initInvokerChain();
        }
        return invokerChain.invoke(invoker);
    }

    @Override
    public Invoker export(Invoker invoker) {
        if (invokerChain == null) {
            initInvokerChain();
        }
        return invokerChain.invoke(invoker);
    }

    public synchronized void initInvokerChain() {
        if (invokerChain == null) {
            ServiceLoader<Filter> filterLoader = ServiceLoader.load(Filter.class);

            InvokerChain invokerChain = new InvokerChain();
            for (Filter filter : filterLoader) {
                invokerChain.addFilter(filter);
            }
            this.invokerChain = invokerChain;
        }
    }
}
