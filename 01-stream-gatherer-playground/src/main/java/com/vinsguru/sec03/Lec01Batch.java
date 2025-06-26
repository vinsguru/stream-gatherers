package com.vinsguru.sec03;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Gatherer;
import java.util.stream.IntStream;

/*
 * Goal: Collect items and emit them in batches.
 * */
public class Lec01Batch {

    private static final Logger log = LoggerFactory.getLogger(Lec01Batch.class);

    public static void main(String[] args) {

        IntStream.rangeClosed(1, 10)
                 .boxed()
                 // .gather(Gatherers.windowFixed(3))
                 .gather(batch(4))
                 .forEach(item -> log.info("{}", item));

    }

    private static <T> Gatherer<T, ?, List<T>> batch(int size) {
        return Gatherer.ofSequential(
                () -> new BatchBuffer<T>(size),
                Gatherer.Integrator.ofGreedy(BatchBuffer::integrate),
                BatchBuffer::finish
        );
    }

    private static class BatchBuffer<T> {

        private final int size;
        private final List<T> list;

        BatchBuffer(int size) {
            this.size = size;
            this.list = new ArrayList<>();
        }

        boolean integrate(T element, Gatherer.Downstream<? super List<T>> downstream) {
            log.info("received element: {}", element);
            this.list.add(element);
            if (this.list.size() < this.size) {
                return true;
            }
            var result = List.copyOf(this.list);
            this.list.clear();
            return downstream.push(result);
        }

        // invoked when the stream completes. either all items are processed or it short-circuits.
        void finish(Gatherer.Downstream<? super List<T>> downstream) {
            log.info("called finish");
            if(!downstream.isRejecting() && !this.list.isEmpty()){
                downstream.push(List.copyOf(this.list));
            }
        }

    }


}
