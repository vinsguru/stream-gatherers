package com.vinsguru.sec01;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;
import java.util.stream.Gatherer;
import java.util.stream.Stream;

public class Lec06TakeUntil {

    private static final Logger log = LoggerFactory.getLogger(Lec06TakeUntil.class);

    public static void main(String[] args) {

        // demo 1
        Stream.of(1, 3, 5, 6, 7, 8)
              .gather(takeUntil(i -> i % 2 == 0))
              .forEach(item -> log.info("received: {}", item));

        // demo 2 - simple retry using gatherer
        Stream.generate(() -> ThreadLocalRandom.current().nextInt(1, 1000))
              .gather(takeUntil(i -> i > 950))
              .forEach(item -> log.info("received: {}", item));

    }

    private static <T> Gatherer<T, Void, T> takeUntil(Predicate<T> predicate) {
        return Gatherer.ofSequential(
                Gatherer.Integrator.of(
                        (state, element, downstream) -> downstream.push(element) && predicate.negate().test(element)
                )
        );
    }

}
