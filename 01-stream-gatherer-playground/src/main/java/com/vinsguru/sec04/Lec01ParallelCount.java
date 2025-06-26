package com.vinsguru.sec04;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Gatherer;
import java.util.stream.IntStream;

/*
 * Goal: Create a custom Gatherer to count the number of items in a stream.
 * Although Java provides a built-in count() terminal operation,
 * we implement this gatherer to better understand how state is handled
 * in parallel stream processing.
 */
public class Lec01ParallelCount {

    private static final Logger log = LoggerFactory.getLogger(Lec01ParallelCount.class);

    public static void main(String[] args) {

        IntStream.range(0, 10)
                 .boxed()
                 .parallel()
                 .gather(count())
                 .forEach(r -> log.info("count: {}", r));

    }

    private static <T> Gatherer<T, ?, Long> count(){
        return Gatherer.of(
                ItemsCounter::new,
                Gatherer.Integrator.ofGreedy((state, element, downstream) -> state.integrate()),
                ItemsCounter::combine,
                ((itemsCounter, downstream) -> itemsCounter.finish(downstream))
        );
    }

    // to demo parallel stream
    private static class ItemsCounter {

        private static final AtomicInteger atomicInteger = new AtomicInteger(0); // just to show how many instances are created
        private long count;

        ItemsCounter(){
            this.count = 0;
            log.info("created state object. instance count: {}", atomicInteger.incrementAndGet());
        }

        boolean integrate(){
            this.count++;
            return true;
        }

        ItemsCounter combine(ItemsCounter other){
            log.info("combining {} - {}", this.count, other.count);
            this.count = this.count + other.count;
            return this;
        }

        void finish(Gatherer.Downstream<? super Long> downstream){
            log.info("called finish");
            downstream.push(this.count);
        }


    }



}
