package edu.training.akka.sum_with_actors;

import akka.actor.AbstractActor;
import akka.actor.Props;

public class Callback extends AbstractActor {

    public static Props props(){
        return Props.create(Callback.class);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(SumActor.SumResponse.class, System.out::println)
                .build();
    }
}
