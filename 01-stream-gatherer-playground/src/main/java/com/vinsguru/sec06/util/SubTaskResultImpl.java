package com.vinsguru.sec06.util;

import java.util.concurrent.Future;
import java.util.function.Function;

class SubTaskResultImpl<T> implements SubTaskResult<T> {

    private final Future<T> future;
    private Function<Throwable, T> errorHandler = ex -> { throw new RuntimeException(ex); };

    public SubTaskResultImpl(Future<T> future) {
        this.future = future;
    }

    @Override
    public T get() {
        try {
            return this.future.get();
        } catch (Exception e) {
            return this.errorHandler.apply(e);
        }
    }

    @Override
    public SubTaskResult<T> onError(Function<Throwable, T> errorHandler) {
        this.errorHandler = errorHandler;
        return this;
    }
}
