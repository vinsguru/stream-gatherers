package com.vinsguru.sec04;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Gatherer;
import java.util.stream.IntStream;

/*
 * Goal: Create a custom Gatherer to find the max number in a stream.
 * Java provides a built-in max() terminal operator. But we do this as a simple exercise!
 */
public class Lec02ParallelMax {

    private static final Logger log = LoggerFactory.getLogger(Lec02ParallelMax.class);

    public static void main(String[] args) {

        IntStream.rangeClosed(0, 10)
                 .boxed()
                 .parallel()
                 .gather(max())
                 .forEach(r -> log.info("max: {}", r));

    }

    private static Gatherer<Integer, ?, Integer> max() {
        return Gatherer.of(
                () -> new int[1],
                Gatherer.Integrator.ofGreedy((state, element, downstream) -> {
                    state[0] = Math.max(state[0], element);
                    return true;
                }),
                (state1, state2) -> {
                    state1[0] = Math.max(state1[0], state2[0]);
                    return state1;
                },
                (state, downstream) -> downstream.push(state[0])
        );
    }

}
