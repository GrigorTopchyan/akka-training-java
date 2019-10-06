package edu.training.akka.map_with_actor;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;

public class Container extends AbstractActor {
    private final ActorRef [] buckets = new ActorRef[16];

    public Container() {
        for (int i = 0; i < buckets.length; i++){
            buckets[i] = getContext().actorOf(Bucket.props(),"Buket-" + i);
        }
    }

    public static Props props(){
        return Props.create(Container.class);
    }

    private void onMessage(MapMessage message){
        String command = message.command;
        String key = message.key;
        int hash = key.hashCode() % 16;

        switch (command){
            case "put":
            case "remove":
                buckets[hash].tell(message,getSelf());
                break;
            case "get":
                MapMessage nextGet = new MapMessage("get",key,getSender());
                buckets[hash].tell(nextGet,getSelf());
                break;
            case "get/result":
                ActorRef originalSender = message.originalSender;
                String value = message.value;
                MapMessage responseGet = new MapMessage("get/result",key,value);
                originalSender.tell(responseGet,getSelf());
                break;
        }
    }


    @Override
    public Receive createReceive() {
        return null;
    }
}
