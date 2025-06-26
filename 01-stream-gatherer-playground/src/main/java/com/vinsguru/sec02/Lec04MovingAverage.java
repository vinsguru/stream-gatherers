package com.vinsguru.sec02;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.stream.Gatherer;
import java.util.stream.IntStream;

public class Lec04MovingAverage {

    private static final Logger log = LoggerFactory.getLogger(Lec04MovingAverage.class);

    public static void main(String[] args) {

        IntStream.rangeClosed(1, 10)
                .boxed()
                .gather(movingAverage(3))
                .forEach(item -> log.info("{}", item));

    }

    private static <T extends Number> Gatherer<T, ?, Double> movingAverage(int windowSize){
        return Gatherer.ofSequential(
                () -> new MovingAverageCalculator<T>(windowSize),
                Gatherer.Integrator.ofGreedy((state, element, downstream) -> downstream.push(state.findAverage(element)))
        );
    }

    private static class MovingAverageCalculator<T extends Number> {

        private final int windowSize;
        private final Deque<T> deque;
        private double sum;

        MovingAverageCalculator(int windowSize){
            this.windowSize = windowSize;
            this.deque = new ArrayDeque<>();
            this.sum = 0;
        }

        double findAverage(T element){
            this.deque.addLast(element);
            this.sum = this.sum + element.doubleValue();
            if(this.deque.size() > this.windowSize){
                this.sum = this.sum - this.deque.removeFirst().doubleValue();
            }
            return this.sum / this.deque.size();
        }

    }

}
