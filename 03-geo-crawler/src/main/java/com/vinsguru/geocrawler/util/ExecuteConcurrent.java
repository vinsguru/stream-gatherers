package com.vinsguru.geocrawler.util;

import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.function.Function;
import java.util.stream.Gatherer;

/*
* Executes the given tasks concurrently using virtual threads. It does NOT delay the errors.
* */
class ExecuteConcurrent<T, R> {

    private int taskCount;
    private final int maxConcurrency;
    private final Function<T, R> function;
    private final ExecutorService executorService;
    private final ExecutorCompletionService<R> completionService;

    ExecuteConcurrent(ExecutorService executorService, Function<T, R> function, int maxConcurrency){
        this.function = function;
        this.maxConcurrency = maxConcurrency;
        this.executorService = executorService;
        this.completionService = new ExecutorCompletionService<>(executorService);
    }

    boolean integrate(T element, Gatherer.Downstream<? super R> downstream){
        this.completionService.submit(() -> function.apply(element));
        taskCount++;
        if(taskCount < maxConcurrency){
            return true; // we can accept more
        }
        // capacity is full. we must emit at least 1 to accept 1 item from upstream.
        taskCount--;
        return downstream.push(this.takeNextCompletedResult());
    }

    void finish(Gatherer.Downstream<? super R> downstream){
        var shouldContinue = !downstream.isRejecting();
        for (int i = 0; i < taskCount && shouldContinue; i++) {
            shouldContinue = downstream.push(this.takeNextCompletedResult());
        }
        this.executorService.shutdownNow(); // it will cancel if there are any pending tasks
    }

    private R takeNextCompletedResult(){
        try {
            return this.completionService.take().get();
        } catch (Exception e) { // I throw exception for demo purposes. prefer sealed types
            this.executorService.shutdownNow(); 
            throw new RuntimeException(e);
        }
    }
}
