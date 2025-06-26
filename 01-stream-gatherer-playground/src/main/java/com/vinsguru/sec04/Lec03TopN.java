package com.vinsguru.sec04;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.PriorityQueue;
import java.util.Queue;
import java.util.stream.Gatherer;
import java.util.stream.IntStream;

// To track the top N items
public class Lec03TopN {

    private static final Logger log = LoggerFactory.getLogger(Lec03TopN.class);

    public static void main(String[] args) {

        IntStream.rangeClosed(1, 100)
                .boxed()
                .parallel()
                .gather(topN(5))
                .limit(2)
                .forEach(item -> log.info("{}", item));

    }

    private static Gatherer<Integer, ?, Integer> topN(int maxSize){
        return Gatherer.of(
                () -> new TopNTracker(maxSize),
                Gatherer.Integrator.ofGreedy((state, element, downstream) -> state.integrate(element)),
                TopNTracker::combine,
                (state, downstream) -> state.finish(downstream)
        );
    }

    private static class TopNTracker {

        private final int maxSize;
        private final Queue<Integer> queue;

        TopNTracker(int maxSize){
            this.maxSize = maxSize;
            this.queue = new PriorityQueue<>();
        }

        boolean integrate(Integer element){
            this.queue.add(element);
            if(this.queue.size() > this.maxSize){
                this.queue.poll();
            }
            return true;
        }

        TopNTracker combine(TopNTracker other){
            this.queue.addAll(other.queue);
            while(this.queue.size() > this.maxSize){
                this.queue.poll();
            }
            return this;
        }

        /*

        1. if you want reverse order,

        queue.stream()
             .sorted(Comparator.reverseOrder())
             .allMatch(downstream::push);


        2. workaround for gatherers chaining

        void finish(Gatherer.Downstream<? super T> downstream) {
            var shouldContinue = !downstream.isRejecting();
            while (!this.queue.isEmpty() && shouldContinue) {
                shouldContinue =  downstream.push(this.queue.poll());
            }
        }

        */

        void finish(Gatherer.Downstream<? super Integer> downstream){
            while (!this.queue.isEmpty() && !downstream.isRejecting()){
                downstream.push(this.queue.poll());
            }
        }

    }



}
