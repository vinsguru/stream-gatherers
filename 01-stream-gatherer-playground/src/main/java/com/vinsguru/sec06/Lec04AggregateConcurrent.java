package com.vinsguru.sec06;

import com.vinsguru.sec06.externalservice.Client;
import com.vinsguru.sec06.util.GatherersUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.IntStream;
import java.util.stream.Stream;

/*
 * Nested Concurrency - For each id, call product-service and rating-service concurrently
 * */
public class Lec04AggregateConcurrent {

    private static final Logger log = LoggerFactory.getLogger(Lec04AggregateConcurrent.class);

    record ProductAggregate(String name, int rating) {
    }

    public static void main(String[] args) {

        IntStream.rangeClosed(1, 100)
                 .boxed()
                 .gather(GatherersUtil.aggregateConcurrent(
                         Client::getProduct,
                         Client::getRating,
                         ProductAggregate::new
                 ))
                 .forEach(pa -> log.info("{}", pa));

    }

}
