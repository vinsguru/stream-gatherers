package com.vinsguru.sec03;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Predicate;
import java.util.stream.Gatherer;
import java.util.stream.Stream;

/*
 * Goal: Collect items into a batch and emit the batch when the given condition is met.
 */
public class Lec02BatchUntil {

    private static final Logger log = LoggerFactory.getLogger(Lec02BatchUntil.class);

    public static void main(String[] args) {

        Stream.generate(() -> ThreadLocalRandom.current().nextInt(1, 100))
              .gather(batchUntil(i -> i % 5 == 0))
              .limit(5)
              .forEach(item -> log.info("{}", item));

    }

    // If you want to exclude the delimiter in some cases, we can accept a boolean
    private static <T> Gatherer<T, ?, List<T>> batchUntil(Predicate<T> predicate) {
        return Gatherer.ofSequential(
                () -> new BatchBuffer<T>(predicate),
                Gatherer.Integrator.ofGreedy(BatchBuffer::integrate),
                BatchBuffer::finish
        );
    }

    private static class BatchBuffer<T> {

        private final Predicate<T> predicate;
        private final List<T> list;

        BatchBuffer(Predicate<T> predicate) {
            this.predicate = predicate;
            this.list = new ArrayList<>();
        }

        boolean integrate(T element, Gatherer.Downstream<? super List<T>> downstream) {
            this.list.add(element);
            if (this.predicate.negate().test(element)) {
                return true;
            }
            var result = List.copyOf(this.list);
            this.list.clear();
            return downstream.push(result);
        }

        // invoked when the stream completes. either all items are processed or it short-circuits.
        void finish(Gatherer.Downstream<? super List<T>> downstream) {
            if (!downstream.isRejecting() && !this.list.isEmpty()) {
                downstream.push(List.copyOf(this.list));
            }
        }

    }


}
