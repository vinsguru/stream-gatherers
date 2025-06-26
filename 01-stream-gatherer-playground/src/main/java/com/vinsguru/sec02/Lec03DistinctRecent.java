package com.vinsguru.sec02;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedHashSet;
import java.util.stream.Gatherer;
import java.util.stream.Stream;

/*
* To maintain the last N unique items.
* That is, at any point, only the most recent N emitted items are remembered, and duplicates within them are skipped.
*/
public class Lec03DistinctRecent {

    private static final Logger log = LoggerFactory.getLogger(Lec03DistinctRecent.class);

    public static void main(String[] args) {

        Stream.of(1, 2, 2, 1, 3, 4, 3, 1, 5)
              //.distinct()
              .gather(distinctRecent(3))
              .forEach(item -> log.info("received: {}", item));


    }

    private static <T> Gatherer<T, ?, T> distinctRecent(int size) {
        return Gatherer.ofSequential(
                () -> new LinkedHashSet<T>(),
                Gatherer.Integrator.ofGreedy((state, element, downstream) -> {
                    if(state.size() == size){
                        state.removeFirst();
                    }
                    if (state.contains(element)) {
                        return true;
                    }
                    state.add(element);
                    return downstream.push(element);
                })
        );
    }

}
