package nl.shashi.playground.artemis;

import javax.jms.JMSException;
import java.util.stream.IntStream;

public class WriteMessage {

    public static void main(String[] args) {
//        sendMessages(100, "", Queue.closeAccountListQueue);
        sendMessages(100, "", Queue.newAccountQueue);
    }

    private static void sendMessages(int numberOfMessages, String message, Queue queue) {

        IntStream.range(1, numberOfMessages + 1).forEach(counter -> {
            try {
                if (message.isBlank()) {
                    JmsClient.send(counter + "==>" + CustomDataFaker.starWars(), queue.name());
                } else {
                    JmsClient.send(counter + "==>" + message, queue.name());
                }
            } catch (JMSException e) {
                throw new RuntimeException(e);
            }
        });

    }

    public enum Queue {
        closeAccountListQueue,
        newAccountQueue,
        closeAccountNotification
    }

}
