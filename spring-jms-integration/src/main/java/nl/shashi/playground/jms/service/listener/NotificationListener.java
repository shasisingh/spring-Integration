package nl.shashi.playground.jms.service.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NotificationListener {

    @JmsListener(destination = "closeAccountNotification")
    public void listen(String message) {
        log.info("Received Message from closeAccountNotification queue, message = {}",message);
    }
}
