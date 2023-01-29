package nl.shashi.playground.jms.service.handler;

import lombok.extern.slf4j.Slf4j;
import nl.shashi.playground.jms.service.LeaderCandidate;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class JmsMessageHandler implements MessageHandler {

    private final LeaderCandidate leaderCandidate;

    public JmsMessageHandler(LeaderCandidate leaderCandidate) {
        this.leaderCandidate = leaderCandidate;
    }

    @Override
    public void handleMessage(Message<?> message) throws MessagingException {
        if (!leaderCandidate.isLeader()) {
            log.warn("Cannot handle the message because the current service is not the leader");
            throw new MessagingException("Cannot handle the message because the current service is not the leader");
        }

        String data = (String) message.getPayload();
        log.info("Received Messages from Queue : {} : {}","newAccountQueue", data);
    }

}
