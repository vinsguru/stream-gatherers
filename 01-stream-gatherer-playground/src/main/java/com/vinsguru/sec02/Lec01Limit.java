package com.vinsguru.sec02;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Gatherer;
import java.util.stream.Stream;

/*
 * Goal: To create a simple stateful gatherer
 * */
public class Lec01Limit {

    private static final Logger log = LoggerFactory.getLogger(Lec01Limit.class);

    public static void main(String[] args) {

        Stream.of(1, 2, 3, 4, 5)
              // .limit(3)
              .gather(limit(3))
              .forEach(item -> log.info("received: {}", item));

    }

    private static <T> Gatherer<T, ?, T> limit(int maxSize) {
        return Gatherer.ofSequential(
                () -> new int[1], // mutable container
                (counter, element, downstream) -> {
                    if (counter[0] < maxSize) {
                        counter[0]++;
                        return downstream.push(element) && counter[0] < maxSize; // should continue next iteration?
                    }
                    return false; // no more items
                }
        );
    }

}
