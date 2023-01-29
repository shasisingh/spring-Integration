package nl.shashi.playground.artemis;

import javax.jms.JMSException;
import java.util.stream.IntStream;

/**
 * The type Write message.
 */
public class WriteMessage {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     * @throws JMSException the jms exception
     */
    public static void main(String[] args) throws JMSException {

        IntStream.range(1, 100).forEach(counter -> {
            try {
                JmsClient.send(counter + "==>" + testMessage(), "closeAccountListQueue");
            } catch (JMSException e) {
                throw new RuntimeException(e);
            }
        });

        IntStream.range(1, 100).forEach(counter -> {
            try {
                JmsClient.send(counter + "==>" + testMessage(), "newAccountQueue");
            } catch (JMSException e) {
                throw new RuntimeException(e);
            }
        });


    }

    private static String testMessage() {
        return "{1:F01BICFOOYYAXXX8683497519}";
    }

}
