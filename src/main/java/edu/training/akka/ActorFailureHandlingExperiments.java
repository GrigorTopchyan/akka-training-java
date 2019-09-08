package edu.training.akka;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;

class SupervisingActor extends AbstractActor{
    static Props props() {
        return Props.create(SupervisingActor.class,SupervisingActor::new);
    }

    ActorRef child = getContext().actorOf(SuperVisedActor.props(),"supervised-actor");


    @Override
    public Receive createReceive() {
        return receiveBuilder().matchEquals("failChild", p ->{
            child.tell("fail",getSelf());
        }).build();
    }
}


class SuperVisedActor extends  AbstractActor {

    static Props props(){
        return Props.create(SuperVisedActor.class,SuperVisedActor::new);
    }


    @Override
    public void preStart() throws Exception, Exception {
        System.out.println("supervised actor started");
    }

    @Override
    public void postStop() throws Exception, Exception {
        System.out.println("Supervised actor stopped");
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().matchEquals("fail", p -> {
            System.out.println("Supervised actor fails now");
        }).build();
    }
}

public class ActorFailureHandlingExperiments {
}
