package edu.training.akka.map_reduce_actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.Function;

public class MRBucket extends AbstractActor {
    private Map<String,String > data = new HashMap<>();

    public static Props props(){
        return Props.create(MRBucket.class);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(MRRequest.class,this::OnMessage)
                .build();
    }

    private void OnMessage(MRRequest message){
        String key = message.key;
        String command = message.command;

        switch (command){
            case "put":
                String value = message.value;
                data.put(key,value);
                break;
            case "get":
                ActorRef originalSender = message.originalSender;
                MRRequest resultMessage = new MRRequest("get/result",key,data.get(key),originalSender);
                getSender().tell(resultMessage,getSelf());
                break;
            case "remove":
                data.remove(key);
                break;
            case "map-reduce":
                Function mapper = message.mapper;
                BiFunction reducer = message.reducer;
                Object response = message.initElem;
                for (Map.Entry entry : data.entrySet()){
                    response = reducer.apply(response,mapper.apply(entry));
                }
                MRResponse mrResponse = new MRResponse("map-reduce/result",response);
                getSender().tell(mrResponse,getSelf());
                break;
        }
    }
}
