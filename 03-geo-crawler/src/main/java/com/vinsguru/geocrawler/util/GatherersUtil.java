package com.vinsguru.geocrawler.util;

import java.util.concurrent.Executors;
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

}
