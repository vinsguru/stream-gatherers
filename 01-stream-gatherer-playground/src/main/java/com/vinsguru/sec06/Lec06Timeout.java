package com.vinsguru.sec06;

import com.vinsguru.sec06.externalservice.Client;
import com.vinsguru.sec06.util.GatherersUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;
import java.util.stream.Gatherer;
import java.util.stream.IntStream;

/*
 * Timeout Pattern - Not Recommended - Potential Issue!!
 * */
public class Lec06Timeout {

    private static final Logger log = LoggerFactory.getLogger(Lec06Timeout.class);

    record ProductAggregate(String name, int rating) {
    }

    public static void main(String[] args) {

        IntStream.rangeClosed(1, 100)
                 .boxed()
                 .gather(GatherersUtil.aggregateConcurrent((subTaskExecutor, id) -> {
                     var product = subTaskExecutor.execute(() -> Client.getProduct(id));
                     var rating = subTaskExecutor.execute(() -> Client.getRating(id));
                     return new ProductAggregate(product.get(), rating.get());
                 }))
                 .gather(timeout(Duration.ofMillis(500)))
                 .forEach(pa -> log.info("{}", pa));

    }

    private static <T> Gatherer<T, ?, T> timeout(Duration duration) {
        return Gatherer.ofSequential(
                () -> Instant.now().plus(duration), // deadline
                Gatherer.Integrator.of((deadline, element, downstream) -> {
                    return Instant.now().isBefore(deadline) && downstream.push(element);
                })
        );
    }

}
