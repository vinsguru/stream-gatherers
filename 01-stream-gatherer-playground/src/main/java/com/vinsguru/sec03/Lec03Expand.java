package com.vinsguru.sec03;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Gatherer;
import java.util.stream.Stream;

public class Lec03Expand {

    private static final Logger log = LoggerFactory.getLogger(Lec03Expand.class);

    record Employee(String title,
                    List<Employee> directReports) {
    }

    public static void main(String[] args) {

        var accountant = new Employee("Accountant", List.of());
        var financeLead = new Employee("Finance Lead", List.of(accountant));
        var cfo = new Employee("CFO", List.of(financeLead));

        var srDeveloper = new Employee("Senior Developer", List.of());
        var jrDeveloper = new Employee("Junior Developer", List.of());
        var devManager = new Employee("Dev Manager", List.of(srDeveloper, jrDeveloper));

        var qaAnalyst = new Employee("QA analyst", List.of());
        var qaManager = new Employee("QA Manager", List.of(qaAnalyst));
        var cto = new Employee("CTO", List.of(devManager, qaManager));

        // root element
        var ceo = new Employee("CEO", List.of(cto, cfo));

        Stream.of(ceo)
              .gather(expand(e -> e.directReports().stream()))
              .map(Employee::title)
              .forEach(item -> log.info("{}", item));

    }

    private static <T> Gatherer<T, ?, T> expand(Function<T, Stream<T>> function) {
        return Gatherer.ofSequential(
                () -> new BreadthFirstExpander<T>(function),
                Gatherer.Integrator.ofGreedy(BreadthFirstExpander::integrate),
                BreadthFirstExpander::finish
        );
    }

    private static class BreadthFirstExpander<T> {

        private final Deque<T> deque;
        private final Function<T, Stream<T>> function;

        BreadthFirstExpander(Function<T, Stream<T>> function) {
            this.function = function;
            this.deque = new ArrayDeque<>();
        }

        boolean integrate(T element, Gatherer.Downstream<? super T> downstream) {
            return this.process(element, downstream);
        }

//        void finish(Gatherer.Downstream<? super T> downstream) {
//            while (!this.deque.isEmpty() && !downstream.isRejecting()) {
//                this.process(this.deque.removeFirst(), downstream);
//            }
//        }

        // workaround if you plan to chain
        void finish(Gatherer.Downstream<? super T> downstream) {
            var shouldContinue = !downstream.isRejecting();
            while (!this.deque.isEmpty() && shouldContinue) {
                shouldContinue = this.process(this.deque.removeFirst(), downstream);
            }
        }

        private boolean process(T element, Gatherer.Downstream<? super T> downstream) {
            var shouldContinue = downstream.push(element);
            if (shouldContinue) {
                var children = this.function.apply(element).toList();
                this.deque.addAll(children);
            }
            return shouldContinue;
        }

    }


}
