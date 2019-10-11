package edu.training.akka.map_reduce_actors;

import akka.actor.ActorRef;

import java.util.Map;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Function;

class MRRequest<K,V,T> {
    long id;
    String command;
    K key;
    V value;
    ActorRef originalSender;
    Function<Map.Entry<K,V>,T> mapper;
    BinaryOperator<T> reducer;
    T initElem;


    MRRequest(String command,Function<Map.Entry<K,V>,T> mapper,BinaryOperator<T> reducer,T initElem,ActorRef originalSender) {
        this.command = command;
        this.mapper = mapper;
        this.reducer = reducer;
        this.initElem = initElem;
        this.originalSender = originalSender;
    }

    MRRequest(String command,Function<Map.Entry<K,V>,T> mapper,BinaryOperator<T> reducer,T initElem,K key) {
        this.command = command;
        this.mapper = mapper;
        this.reducer = reducer;
        this.initElem = initElem;
        this.key = key;
    }

    MRRequest(String command, K key) {
        this.command = command;
        this.key = key;
    }

    MRRequest(String command, K key, V value, ActorRef originalSender) {
        this.command = command;
        this.key = key;
        this.value = value;
        this.originalSender = originalSender;
    }

    MRRequest(String command, K key, V value) {
        this.command = command;
        this.key = key;
        this.value = value;
    }


    public MRRequest(String command, K key, ActorRef originalSender) {
        this.command = command;
        this.key = key;
        this.originalSender = originalSender;
    }
}