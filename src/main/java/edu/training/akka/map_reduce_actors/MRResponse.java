package edu.training.akka.map_reduce_actors;

public class MRResponse {
    String command;
    Object result;

    public MRResponse(String command, Object result) {
        this.command = command;
        this.result = result;
    }
}
