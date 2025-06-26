package com.vinsguru.sec05;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Gatherers;
import java.util.stream.IntStream;

public class Lec01WindowFixed {

    private static final Logger log = LoggerFactory.getLogger(Lec01WindowFixed.class);

    public static void main(String[] args) {

        IntStream.rangeClosed(1, 10)
                 .boxed()
                 .gather(Gatherers.windowFixed(3))
                 .forEach(item -> log.info("{}", item));

    }

}
