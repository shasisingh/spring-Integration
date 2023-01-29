package nl.shashi.playground.jms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.channel.PublishSubscribeChannel;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageHeaders;

import java.util.Optional;
import java.util.UUID;

/**
 *  this class help us to solve issue when you shut down you application
 *  Error solution: Error creating bean with name 'errorChannel': Singleton bean creation not allowed while
 */

@Configuration
public class ErrorChannelConfig {


    @Bean
    public IntegrationFlow errorHandlingFlow(PublishSubscribeChannel errorChannel) {
        return IntegrationFlows.from(errorChannel)
                .handle(this::errorHandler)
                .get();
    }

    @Bean
    public PublishSubscribeChannel errorChannel(){
        return new PublishSubscribeChannel(false);
    }

    private void errorHandler(Message<?> message) {

        String uuid = Optional.ofNullable(message)
                .map(Message::getHeaders)
                .map(MessageHeaders::getId)
                .map(UUID::toString)
                .orElse("fileUUID could not be found");

        System.out.println("Payload"+ uuid);

        String payload = Optional.ofNullable(message)
                .map(Message::getPayload)
                .map(Object::toString)
                .orElse("no payload");

        System.out.println("Payload"+ payload);
    }

}
