package edu.training.akka.sum_with_actors;

import akka.actor.AbstractActor;
import akka.actor.ActorRef;
import akka.actor.PoisonPill;
import akka.actor.Props;

import java.util.stream.IntStream;

import java.util.ArrayList;
import java.util.List;

public class SumActor extends AbstractActor {
    private final ActorRef master;
    private List<Integer> state = new ArrayList<>(2);
    private ActorRef slave1;
    private ActorRef slave2;


    public SumActor(ActorRef master) {
        this.master = master;
    }

    public static Props props(ActorRef master){
        return Props.create(SumActor.class,master);
    }

    private void onResult(SumResponse response){
        this.state.add(response.value);
        if (state.size() == 2){
            master.tell(new SumResponse(this.state.get(0) + this.state.get(1)),getSelf());
            slave1.tell(PoisonPill.getInstance(),getSelf());
            slave2.tell(PoisonPill.getInstance(),getSelf());

        }
    }

    private void onRequest(SumRequest sumRequest){
        final int from = sumRequest.from;
        final int to = sumRequest.to;
        if (to - from > 10){
            slave1 = getContext().actorOf(SumActor.props(getSelf()));
            slave1.tell( new SumRequest(from,(from + to) >>> 1),getSelf());
            slave2 = getContext().actorOf(SumActor.props(getSelf()));
            slave2.tell( new SumRequest((from + to) >>> 1,to),getSelf());
        }else {
            getSender().tell(new SumResponse(this.calc(sumRequest)),getSelf());
        }
    }

    private int calc(SumRequest sumRequest) {
        return IntStream.range(sumRequest.from,sumRequest.to)
                .filter(el -> el % 3 == 0 && el % 7 == 0)
                .sum();

    }

    @Override
    public Receive createReceive() {
        return receiveBuilder()
                .match(SumResponse.class,this::onResult)
                .match(SumRequest.class,this::onRequest)
                .build();
    }

    static class SumResponse {
        private final int value;

        SumResponse(int value) {
            this.value = value;
        }

        @Override
        public String toString() {
            return "SumResponse{" +
                    "value=" + value +
                    '}';
        }
    }

    static class SumRequest{
        private final int from;
        private final int to;

        SumRequest(int from, int to) {
            this.from = from;
            this.to = to;
        }
    }
}
