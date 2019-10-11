package edu.training.akka.map_reduce_actors;

public class MRResponse<K,V,T> {
    String command;
    T result;
    K key;
    long id;

    public MRResponse(String command,K key, T result,long id) {
        this.id = id;
        this.command = command;
        this.key = key;
        this.result = result;
    }

    public MRResponse(String command, T result) {
        this.command = command;
        this.result = result;
    }

    public MRResponse(String command, K key, T result) {
        this.command = command;
        this.key = key;
        this.result = result;
    }
}
