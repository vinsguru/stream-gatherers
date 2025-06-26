package com.vinsguru.sec02;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashSet;
import java.util.stream.Gatherer;
import java.util.stream.Stream;

public class Lec02Distinct {

    private static final Logger log = LoggerFactory.getLogger(Lec02Distinct.class);

    public static void main(String[] args) {

        Stream.of(1, 2, 2, 1, 3, 4, 3, 1, 5)
              //.distinct()
              .gather(distinct())
              .forEach(item -> log.info("received: {}", item));


    }

    private static <T> Gatherer<T, ?, T> distinct() {
        return Gatherer.ofSequential(
                () -> new HashSet<T>(),
                Gatherer.Integrator.ofGreedy((state, element, downstream) -> {
                    if (state.contains(element)) {
                        return true;
                    }
                    state.add(element);
                    return downstream.push(element);
                })
        );
    }

}
