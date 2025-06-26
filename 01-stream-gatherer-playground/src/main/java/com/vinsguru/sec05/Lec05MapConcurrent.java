package com.vinsguru.sec05;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.stream.Gatherers;
import java.util.stream.IntStream;

/*
* Use mapConcurrent gatherer for I/O tasks in the stream pipeline
* */
public class Lec05MapConcurrent {

    private static final Logger log = LoggerFactory.getLogger(Lec05MapConcurrent.class);

    public static void main(String[] args) {

        IntStream.rangeClosed(1, 5)
                .boxed()
                .peek(item -> log.info("peek: {}", item))
                .gather(Gatherers.mapConcurrent(
                        50,
                        Lec05MapConcurrent::timeConsumingIOTask
                ))
                .forEach(item -> log.info("{}", item));

    }

    private static Integer timeConsumingIOTask(Integer input){
        try {
            Thread.sleep(Duration.ofSeconds(5));
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        log.info("task done: {}", input);
        return input;
    }

}
