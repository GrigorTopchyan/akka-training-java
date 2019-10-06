package edu.training.akka.sum_with_actors;

import akka.actor.ActorSystem;
import akka.testkit.TestKit;
import org.junit.Test;

public class TestSumActor {
    @Test
    public void testrangeSmallerThanThreashhold(){
        ActorSystem system = ActorSystem.create();
        TestKit probe = new TestKit(system);
    }
}
