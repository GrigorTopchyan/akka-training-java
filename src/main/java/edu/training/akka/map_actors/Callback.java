package edu.training.akka.map_actors;

import akka.actor.AbstractActor;
import akka.actor.Props;

public class Callback extends AbstractActor {

    public static Props props(){
        return Props.create(Callback.class);
    }

    private void onMessage(MapMessage message){
        String command = message.command;
        switch (command){
            case "get/result":
                System.out.println(message.key  + " - " + message.value);
                break;
        }
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(MapMessage.class,this::onMessage)
                .build();
    }
}
