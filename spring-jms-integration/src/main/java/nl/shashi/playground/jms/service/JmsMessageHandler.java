package nl.shashi.playground.jms.service;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;

/**
 * The type Jms message handler.
 */
@Service
public class JmsMessageHandler implements MessageHandler {

    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
        String data = (String) message.getPayload();
        System.out.println( "Received Messages:"+data);
    }

}
