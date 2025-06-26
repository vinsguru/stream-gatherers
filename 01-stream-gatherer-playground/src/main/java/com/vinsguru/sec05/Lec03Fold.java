package com.vinsguru.sec05;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.stream.Gatherers;
import java.util.stream.Stream;

public class Lec03Fold {

    private static final Logger log = LoggerFactory.getLogger(Lec03Fold.class);

    record BankAccount(int balance) {
    }

    public static void main(String[] args) {

        // positive: deposit, negative: withdraw
        Stream.of(10, -5, 15, 20, -25, 50, 45, -60)
                .gather(Gatherers.fold(
                        () -> new BankAccount(100),
                        ((bankAccount, transaction) -> new BankAccount(bankAccount.balance() + transaction))
                ))
                .forEach(bankAccount -> log.info("{}", bankAccount));

    }

}
