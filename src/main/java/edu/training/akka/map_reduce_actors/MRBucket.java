package edu.training.akka.map_reduce_actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Function;

public class MRBucket<K,V,T> extends AbstractActor {
    private Map<K,V> data = new HashMap<>();

    public static Props props(){
        return Props.create(MRBucket.class);
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(MRRequest.class,this::OnRequest)
                .build();
    }

    private void OnRequest(MRRequest<K,V,T> message){
        K key = message.key;
        String command = message.command;

        switch (command){
            case "put":
                V value = message.value;
                data.put(key,value);
                break;
            case "get":
                ActorRef originalSender = message.originalSender;
                MRRequest resultMessage = new MRRequest<>("get/result",key,data.get(key),originalSender);
                getSender().tell(resultMessage,getSelf());
                break;
            case "remove":
                data.remove(key);
                break;
            case "map-reduce":
                Function<Map.Entry<K,V>,T> mapper = message.mapper;
                BinaryOperator<T> reducer = message.reducer;
                T response = data.entrySet()
                        .stream()
                        .map(mapper)
                        .reduce(message.initElem,reducer);
                MRResponse<K,V,T> mrResponse = new MRResponse<>("map-reduce/result",key,response,message.id);
                getSender().tell(mrResponse,getSelf());
                break;
        }
    }
}
