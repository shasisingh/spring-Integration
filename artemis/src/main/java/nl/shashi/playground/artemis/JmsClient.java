package nl.shashi.playground.artemis;

import org.apache.activemq.artemis.jms.client.ActiveMQXAConnectionFactory;

import javax.jms.Connection;
import javax.jms.IllegalStateException;
import javax.jms.JMSException;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.jms.TextMessage;

/**
 * The type Jms client.
 */
public final class JmsClient {

    private JmsClient() {
    }

    /**
     * Send.
     *
     * @param message   the message
     * @param queueName the queue name
     * @throws JMSException the jms exception
     */
    public static void send(String message, String queueName) throws JMSException {

        try (var factory = new ActiveMQXAConnectionFactory("tcp://localhost:61616"); Connection connection = factory.createConnection()) {
            connection.start();
            try (var session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE)) {
                var dest = session.createQueue(queueName);
                MessageProducer producer = session.createProducer(dest);
                var textMessage = session.createTextMessage(message);
                textMessage.setJMSDestination(dest);
                producer.send(textMessage);
            }
        }
    }

    /**
     * Read string.
     *
     * @param destination the destination
     * @param timeout     the timeout
     * @return the string
     * @throws JMSException the jms exception
     */
    public static String read(final String destination, long timeout) throws JMSException {
        if (destination == null) {
            throw new UnsupportedOperationException("destination must be supplied.");
        }
        try (var factory = new ActiveMQXAConnectionFactory("tcp://localhost:61616"); Connection connection = factory.createConnection()) {
            connection.start();
            try (var session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE)) {
                var reply = session.createQueue(destination);
                var consumer = session.createConsumer(reply);
                var jmsMessage = consumer.receive(timeout);
                if (jmsMessage == null) {
                    throw new IllegalStateException("Received null message");
                }
                return ((TextMessage) jmsMessage).getText();

            }
        }
    }

}
