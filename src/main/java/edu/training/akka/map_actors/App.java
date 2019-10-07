package edu.training.akka.map_actors;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

public class App  {
    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create();
        ActorRef callback = system.actorOf(Callback.props(),"Callback");
        ActorRef container = system.actorOf(Container.props());
        container.tell(new MapMessage("put","A","1"),callback);
        container.tell(new MapMessage("put","B","2"),callback);
        container.tell(new MapMessage("put","C","3"),callback);

        container.tell(new MapMessage("get","A",callback),callback);
        container.tell(new MapMessage("get","B",callback),callback);
        container.tell(new MapMessage("get","C",callback),callback);

        container.tell(new MapMessage("remove","B"),callback);
        container.tell(new MapMessage("get","B",callback),callback);

    }
}
