package com.tstd2.soa.rpc;

import java.util.concurrent.CompletableFuture;

/**
 * @author yancey
 * @date 2020/9/5 00:08
 */
public class RpcContext {

    private static final ThreadLocal<RpcContext> LOCAL = ThreadLocal.withInitial(RpcContext::new);

    private static final ThreadLocal<Boolean> USE_ASYNC = ThreadLocal.withInitial(() -> false);

    private CompletableFuture<?> future;

    public static RpcContext getContext() {
        return LOCAL.get();
    }

    public CompletableFuture<?> getFuture() {
        return future;
    }

    public void setFuture(CompletableFuture<?> future) {
        this.future = future;
    }

    public boolean isAsync() {
        Boolean isAsync = USE_ASYNC.get();
        if (isAsync == null) {
            return false;
        }
        return isAsync;
    }

    public void useAsync() {
        USE_ASYNC.set(true);
    }

    public void removeAsync() {
        USE_ASYNC.remove();
    }
}
