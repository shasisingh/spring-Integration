package nl.shashi.playground.artemis;

import org.apache.activemq.artemis.core.server.embedded.EmbeddedActiveMQ;

/**
 * The type Run artemis.
 */
public class RunArtemis {

    public static void main(String[] args) throws Exception {
        invoke();
    }

    private static void invoke() throws Exception {
        var embedded = new EmbeddedActiveMQ();
        embedded.start();
    }

    public static void kick() throws Exception {
        System.out.println("********** Starting Artemis in Planet Mars.");
        invoke();
        System.out.println(" ********** Started Artemis........");
    }
}
