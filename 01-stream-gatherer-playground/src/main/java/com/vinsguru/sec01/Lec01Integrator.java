package com.vinsguru.sec01;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Gatherer;
import java.util.stream.IntStream;

/*
* Goal: To understand how the integrator works!
* */
public class Lec01Integrator {

    private static final Logger log = LoggerFactory.getLogger(Lec01Integrator.class);


    public static void main(String[] args) {

        IntStream.rangeClosed(1, 5)
                .boxed()
                .gather(Gatherer.of(new SimpleIntegrator()))
                .limit(4)
                .forEach(item -> log.info("received: {}", item));

    }

    private static class SimpleIntegrator implements Gatherer.Integrator<Void, Integer, Integer> {

        // The default state will be null.
        // 'element' refers to the item received from the upstream in the stream pipeline.
        // The 'integrate' method will be invoked whenever we receive an item.
        // The 'integrate' method can process the item and optionally emit it downstream.
        // If 'integrate' returns true, it indicates that it wants more items from upstream.
        // If 'integrate' returns false, it indicates that no more items are needed (short-circuiting).
        // 'downstream.push(...)' is used to emit items. It returns a boolean to indicate if it wants more items.
        // 'downstream.isRejecting()' is effectively equivalent to '!downstream.push(...)'. (there is a limitation when chained)

        @Override
        public boolean integrate(Void state, Integer element, Gatherer.Downstream<? super Integer> downstream) {
            log.info("state: {}, element: {}", state, element);
            return downstream.push(element);
        }
    }

}
