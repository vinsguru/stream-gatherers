package com.vinsguru.sec01;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Function;
import java.util.stream.Gatherer;
import java.util.stream.Stream;

/*
 *  Assignment - implement flatMap using custom gatherer
 * */
public class Lec05FlatMap {

    private static final Logger log = LoggerFactory.getLogger(Lec05FlatMap.class);

    public static void main(String[] args) {

        Stream.of(1, 2, 3)
              // .flatMap(Lec05FlatMap::toLetters)
              .gather(flatMap(Lec05FlatMap::toLetters))
              .limit(3)
              .forEach(item -> log.info("received: {}", item));

    }

    private static Stream<String> toLetters(Integer input) {
        return switch (input) {
            case 1 -> Stream.of("a", "b");
            case 2 -> Stream.of("c", "d", "e");
            case null, default -> Stream.empty();
        };
    }

    private static <T, R> Gatherer<T, Void, R> flatMap(Function<T, Stream<R>> function) {
        return Gatherer.of(
                Gatherer.Integrator.ofGreedy(
                        (state, element, downstream) -> function.apply(element).allMatch(downstream::push)
                )
        );
    }


}
