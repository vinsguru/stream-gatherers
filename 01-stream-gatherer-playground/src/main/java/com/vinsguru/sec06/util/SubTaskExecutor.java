package com.vinsguru.sec06.util;

import java.util.concurrent.Callable;

public interface SubTaskExecutor {

    <T> SubTaskResult<T> execute(Callable<T> callable);

}
