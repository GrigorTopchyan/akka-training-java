package edu.training.akka.sum_with_actors;

import akka.actor.Actor;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;

public class SumApp {
    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create("system");
        ActorRef callBack  = system.actorOf(Callback.props());
        ActorRef actorRef = system.actorOf(SumActor.props(callBack));
        actorRef.tell(new SumActor.SumRequest(0,100),ActorRef.noSender());
    }
}
