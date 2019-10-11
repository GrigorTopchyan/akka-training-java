package edu.training.akka.map_reduce_actors;

import akka.actor.AbstractActor;
import akka.actor.Props;

public class Callback extends AbstractActor {

    public static Props props(){
        return Props.create(Callback.class);
    }

    private void onMessage(MRResponse message){
        String command = message.command;
        switch (command){
            case "get/result":
                System.out.println("Get result"  + " - " + message.result);
                break;
            case "map-reduce/result":
                System.out.println("Map Reduce result"  + " - " + message.result);
                break;
        }
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(MRResponse.class,this::onMessage)
                .build();
    }
}
