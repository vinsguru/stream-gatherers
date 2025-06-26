package com.vinsguru.sec06;

import com.vinsguru.sec06.externalservice.Client;
import com.vinsguru.sec06.util.GatherersUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.IntStream;

/*
 * Nested Concurrency using SubTaskExecutor implementation
 * */
public class Lec05SubTaskExecutor {

    private static final Logger log = LoggerFactory.getLogger(Lec05SubTaskExecutor.class);

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
                 .forEach(pa -> log.info("{}", pa));

    }

}
