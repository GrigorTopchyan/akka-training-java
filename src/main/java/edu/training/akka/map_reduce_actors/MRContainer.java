package edu.training.akka.map_reduce_actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.Props;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.BinaryOperator;

public class MRContainer<K,V,T> extends AbstractActor {
    private final ActorRef [] buckets = new ActorRef[16];
    private long lastId = 0;
    private final Map<Long,T> results = new HashMap<>();
    private final Map<Long, BinaryOperator<T>> reducers = new HashMap<>();
    private final Map<Long,Integer> counts = new HashMap<>();
    private final Map<Long,ActorRef> callbacks = new HashMap<>();


    public MRContainer() {
        for (int i = 0; i < buckets.length; i++){
            buckets[i] = getContext().actorOf(MRBucket.props(),"MRBuket-" + i);
        }
    }

    public static Props props(){
        return Props.create(MRContainer.class);
    }

    private void onMessage(MRRequest<K,V,T> message){
        String command = message.command;
        K key = message.key;
        int hash = Optional.ofNullable(key)
                .map(k-> k.hashCode() % 16)
                .orElse(0);

        switch (command){
            case "put":
            case "remove":
                buckets[hash].tell(message,getSelf());
                break;
            case "get":
                MRRequest<K,V,T> nextGet = new MRRequest<>("get",key,getSender());
                buckets[hash].tell(nextGet,getSelf());
                break;
            case "get/result":
                ActorRef originalSender = message.originalSender;
                V result = message.value;
                MRResponse<K,V,T> responseGet = new MRResponse ("get/result",key,result);
                originalSender.tell(responseGet,getSelf());
                break;
            case "map-reduce":
                message.id = ++lastId;
                Arrays.stream(buckets)
                        .forEach(bucket -> bucket.tell(message,getSelf()));
                results.put(lastId,message.initElem);
                reducers.put(lastId,message.reducer);
                counts.put(lastId,0);
                callbacks.put(lastId,getSender());
                break;
        }
    }

    private void onResponse(MRResponse<K,V,T> response){
        String command = response.command;
        K key = response.key;
        int hash = Optional.ofNullable(key)
                .map(k-> k.hashCode() % 16)
                .orElse(0);
        switch (command) {
            case "map-reduce/result":
                long id = response.id;
                counts.merge(id, 1, Integer::sum);
                results.merge(id, response.result, reducers.get(id));
                if (counts.get(id) == 16) {
                    callbacks.get(id).tell(new MRResponse<>("map-reduce/result", results.get(id)), getSelf());
                    counts.remove(id);
                    reducers.remove(id);
                    results.remove(id);
                    callbacks.remove(id);
                }
                break;
        }
    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(MRRequest.class,this::onMessage)
                .match(MRResponse.class,this::onResponse)
                .build();
    }
}
