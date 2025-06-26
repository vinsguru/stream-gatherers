package com.vinsguru.sec06;

import com.vinsguru.sec06.externalservice.Client;
import com.vinsguru.sec06.util.GatherersUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.IntStream;

/**
 * Emit results in the order they complete
 * */
public class Lec02ExecuteConcurrent {

    private static final Logger log = LoggerFactory.getLogger(Lec02ExecuteConcurrent.class);

    public static void main(String[] args) {

        IntStream.rangeClosed(1, 100)
                 .boxed()
                 .gather(GatherersUtil.executeConcurrent(Client::getProduct, 10))
                 .forEach(log::info);

    }

}
