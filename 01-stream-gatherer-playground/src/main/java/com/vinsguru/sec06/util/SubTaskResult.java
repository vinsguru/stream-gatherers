package com.vinsguru.sec06.util;

import java.util.function.Function;

public interface SubTaskResult<T> {

    T get();

    SubTaskResult<T> onError(Function<Throwable, T> errorHandler);

}
