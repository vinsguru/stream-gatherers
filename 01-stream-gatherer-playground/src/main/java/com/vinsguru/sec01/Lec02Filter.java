package com.vinsguru.sec01;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Predicate;
import java.util.stream.Gatherer;
import java.util.stream.IntStream;

/*
* Goal: To implement a custom Gatherer to simulate filter behavior.
* */
public class Lec02Filter {

    private static final Logger log = LoggerFactory.getLogger(Lec02Filter.class);

    public static void main(String[] args) {

        IntStream.rangeClosed(1, 10)
                .boxed()
              //  .filter(i -> i % 2 == 0)
                .parallel()
                .gather(filter(i -> i % 3 == 0))
                .forEach(item -> log.info("received: {}", item));

    }

    private static <T> Gatherer<T, Void, T> filter(Predicate<T> predicate){
        return Gatherer.of(
                (state, element, downstream) -> {
                    log.info("element: {}", element);
                    if(predicate.test(element)){
                        return downstream.push(element);
                    }
                    return true;
                }
        );
    }

}
