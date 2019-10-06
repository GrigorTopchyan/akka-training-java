package edu.training.akka.map_with_actor;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;

import java.util.HashMap;
import java.util.Map;

public class Bucket extends AbstractActor {
    private Map<String,String > data = new HashMap<>();

    public static Props props(){
        return Props.create(Bucket.class);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(MapMessage.class,this::OnMessage)
                .build();
    }

    private void OnMessage(MapMessage message){
        String key = message.key;
        String command = message.command;

        switch (command){
            case "put":
                String value = message.value;
                data.put(key,value);
                break;
            case "get":
                ActorRef originalSender = message.originalSender;
                MapMessage resultMessage = new MapMessage("get/result",key,data.get(key),originalSender);
                getSender().tell(resultMessage,getSelf());
                break;
            case "remove":
                data.remove(key);
                break;

        }
    }

}
