package com.vinsguru.sec06.util;

import java.net.ServerSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.stream.Gatherer;

public class GatherersUtil {

    public static <T, R> Gatherer<T, ?, R> executeConcurrent(Function<T, R> function){
        return executeConcurrent(function, 1000);
    }

    public static <T, R> Gatherer<T, ?, R> executeConcurrent(Function<T, R> function, int maxConcurrency){
        return Gatherer.ofSequential(
                () -> new ExecuteConcurrent<T, R>(Executors.newVirtualThreadPerTaskExecutor(), function, maxConcurrency),
                Gatherer.Integrator.ofGreedy(ExecuteConcurrent::integrate),
                ExecuteConcurrent::finish
        );
    }

    public static <T, R> Gatherer<T, ?, R> executeConcurrentDelayError(Function<T, R> function){
        return executeConcurrentDelayError(function, 1000);
    }

    public static <T, R> Gatherer<T, ?, R> executeConcurrentDelayError(Function<T, R> function, int maxConcurrency){
        return Gatherer.ofSequential(
                () -> new ExecuteConcurrentDelayError<T, R>(Executors.newVirtualThreadPerTaskExecutor(), function, maxConcurrency),
                Gatherer.Integrator.ofGreedy(ExecuteConcurrentDelayError::integrate),
                ExecuteConcurrentDelayError::finish
        );
    }

    public static <T, R1, R2, R> Gatherer<T, ?, R> aggregateConcurrent(Function<T, R1> function1,
                                                                       Function<T, R2> function2,
                                                                       BiFunction<R1, R2, R> biFunction){
        return Gatherer.ofSequential(
                () -> {
                    var executor = Executors.newVirtualThreadPerTaskExecutor();
                    Function<T, R> function = t -> {
                        var future1 = executor.submit(() -> function1.apply(t));
                        var future2 = executor.submit(() -> function2.apply(t));
                        return biFunction.apply(getResult(future1), getResult(future2));
                    };
                    return new ExecuteConcurrent<T, R>(executor, function, 1000);
                },
                Gatherer.Integrator.ofGreedy(ExecuteConcurrent::integrate),
                ExecuteConcurrent::finish
        );
    }

    public static <T, R> Gatherer<T, ?, R> aggregateConcurrent(BiFunction<SubTaskExecutor, T, R> biFunction, int maxConcurrency){
        return Gatherer.ofSequential(
                () -> {
                    var executor = Executors.newVirtualThreadPerTaskExecutor();
                    var subTaskExecutor = new SubTaskExecutorImpl(executor);
                    Function<T, R> function = t -> biFunction.apply(subTaskExecutor, t);
                    return new ExecuteConcurrent<T, R>(executor, function, maxConcurrency);
                },
                Gatherer.Integrator.ofGreedy(ExecuteConcurrent::integrate),
                ExecuteConcurrent::finish
        );
    }

    public static <T, R> Gatherer<T, ?, R> aggregateConcurrent(BiFunction<SubTaskExecutor, T, R> biFunction){
        return aggregateConcurrent(biFunction, 1000);
    }

    private static <R> R getResult(Future<R> future){
        try {
            return future.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
