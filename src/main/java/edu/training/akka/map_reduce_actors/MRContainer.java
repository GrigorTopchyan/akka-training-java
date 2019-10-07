package edu.training.akka.map_reduce_actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;

public class MRContainer extends AbstractActor {
    private final ActorRef [] buckets = new ActorRef[16];

    public MRContainer() {
        for (int i = 0; i < buckets.length; i++){
            buckets[i] = getContext().actorOf(MRBucket.props(),"MRBuket-" + i);
        }
    }

    public static Props props(){
        return Props.create(MRContainer.class);
    }

    private void onMessage(MRRequest message){
        String command = message.command;
        String key = message.key;
        int hash = key.hashCode() % 16;

        switch (command){
            case "put":
            case "remove":
                buckets[hash].tell(message,getSelf());
                break;
            case "get":
                MRRequest nextGet = new MRRequest("get",key,getSender());
                buckets[hash].tell(nextGet,getSelf());
                break;
            case "get/result":
                ActorRef originalSender = message.originalSender;
                String value = message.value;
                MRRequest responseGet = new MRRequest("get/result",key,value);
                originalSender.tell(responseGet,getSelf());
                break;
        }
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(MRRequest.class,this::onMessage)
                .build();
    }
}
