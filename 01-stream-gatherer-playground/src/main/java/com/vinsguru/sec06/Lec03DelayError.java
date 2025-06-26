package com.vinsguru.sec06;

import com.vinsguru.sec06.externalservice.Client;
import com.vinsguru.sec06.util.GatherersUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Gatherers;
import java.util.stream.IntStream;

/*
 * Delay Errors - Emit successful results first and then throw exception at the end!
 * */
public class Lec03DelayError {

    private static final Logger log = LoggerFactory.getLogger(Lec03DelayError.class);

    public static void main(String[] args) {

        IntStream.rangeClosed(-3, 10)
                 .boxed()
                 .gather(GatherersUtil.executeConcurrentDelayError(Client::getProduct, 5))
                 .forEach(log::info);

    }

}
