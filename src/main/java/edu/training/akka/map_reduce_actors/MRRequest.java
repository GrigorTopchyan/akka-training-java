package edu.training.akka.map_reduce_actors;

import akka.actor.ActorRef;

import java.util.function.BiFunction;
import java.util.function.Function;

class MRRequest {
    String command;
    String key;
    String value;
    ActorRef originalSender;
    Function mapper;
    BiFunction reducer;
    Object initElem;


    MRRequest(String command, String key) {
        this.command = command;
        this.key = key;
    }

    MRRequest(String command, String key, String value, ActorRef originalSender) {
        this.command = command;
        this.key = key;
        this.value = value;
        this.originalSender = originalSender;
    }

    MRRequest(String command, String key, String value) {
        this.command = command;
        this.key = key;
        this.value = value;
    }


    public MRRequest(String command, String key, ActorRef originalSender) {
        this.command = command;
        this.key = key;
        this.originalSender = originalSender;
    }
}