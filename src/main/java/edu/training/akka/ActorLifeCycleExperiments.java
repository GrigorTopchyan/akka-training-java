package edu.training.akka;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;

class StopStartActor1 extends AbstractActor {

    static Props props() {
        return Props.create(StopStartActor1.class, StopStartActor1::new);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .matchEquals("stop",
                        p -> getContext().stop(getSelf()))
                .build();
    }

    @Override
    public void preStart() throws Exception, Exception {
        System.out.println("First Actor started");
        getContext().actorOf(StopStartActor2.props(), "second");
    }

    @Override
    public void postStop() throws Exception, Exception {
        System.out.println("First Actor Stopped");
    }
}

class StopStartActor2 extends AbstractActor {

    static Props props() {
        return Props.create(StopStartActor2.class, StopStartActor2::new);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder().build();
    }

    @Override
    public void preStart() throws Exception, Exception {
        System.out.println("Second Actor started");
        super.preStart();
    }

    @Override
    public void postStop() throws Exception, Exception {
        System.out.println("Second Actor Stopped");
    }
}


public class ActorLifeCycleExperiments {
    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create();
        ActorRef firstActor = system.actorOf(StopStartActor1.props(),"first");
        firstActor.tell("stop",ActorRef.noSender());
    }
}
