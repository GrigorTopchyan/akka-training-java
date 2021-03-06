package edu.training.akka;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.japi.pf.ReceiveBuilder;

/**
 * Created by grigort on 9/6/2019.
 */

class PrintActorRefActor extends AbstractActor {

    static Props props() {
        return Props.create(PrintActorRefActor.class, PrintActorRefActor::new);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().matchEquals("printf",
                p -> {
                    ActorRef secondRef = getContext().actorOf(Props.empty(), "second-actor");
                    System.out.println("Second: " + secondRef);
                }).build();
    }
}


public class ActorHierarchyExperiments {
    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create();
        ActorRef firstRef = system.actorOf(PrintActorRefActor.props(), "first-actor");
        System.out.println("First: " + firstRef);
        firstRef.tell("printit",ActorRef.noSender());
    }

}
