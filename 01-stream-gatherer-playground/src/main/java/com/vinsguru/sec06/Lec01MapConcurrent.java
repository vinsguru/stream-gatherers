package com.vinsguru.sec06;

import com.vinsguru.sec06.externalservice.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Gatherers;
import java.util.stream.IntStream;

// ensure that external service is up and running
public class Lec01MapConcurrent {

    private static final Logger log = LoggerFactory.getLogger(Lec01MapConcurrent.class);

    public static void main(String[] args) {

        IntStream.rangeClosed(1, 50)
                .boxed()
                .gather(Gatherers.mapConcurrent(
                        10,
                        Client::getProduct
                ))
                .forEach(log::info);

    }

}
