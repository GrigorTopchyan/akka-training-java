package edu.training.akka.map_with_actor;

import akka.actor.ActorRef;

class MapMessage {
    String command;
    String key;
    String value;
    ActorRef originalSender;

    MapMessage(String command, String key) {
        this.command = command;
        this.key = key;
    }

    MapMessage(String command, String key, String value, ActorRef originalSender) {
        this.command = command;
        this.key = key;
        this.value = value;
        this.originalSender = originalSender;
    }

    MapMessage(String command, String key, String value) {
        this.command = command;
        this.key = key;
        this.value = value;
    }


    public MapMessage(String command, String key, ActorRef originalSender) {
        this.command = command;
        this.key = key;
        this.originalSender = originalSender;
    }
}