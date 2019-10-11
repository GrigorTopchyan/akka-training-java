package edu.training.akka.map_reduce_actors;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;

import java.util.Map;
import java.util.function.BinaryOperator;
import java.util.function.Function;

public class App {
    public static void main(String[] args) {
        ActorSystem system = ActorSystem.create();
        ActorRef callback = system.actorOf(Callback.props());
        ActorRef container = system.actorOf(MRContainer.props());
        container.tell(new MRRequest<String,String,Integer>("put","A","11"),ActorRef.noSender());
        container.tell(new MRRequest<String,String,Integer>("put","B","22"),ActorRef.noSender());
        container.tell(new MRRequest<String,String,Integer>("put","C","33"),ActorRef.noSender());

        container.tell(new MRRequest<String,String,Integer>("get","A",callback),callback);
        BinaryOperator<Integer> reducer = Integer::sum;
        Function<Map.Entry<String,String>,Integer> mapper = en -> en.getValue().length();
        container.tell(new MRRequest<>("map-reduce",mapper,reducer,0,callback),callback);
    }
}
