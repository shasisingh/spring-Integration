package nl.shashi.playground.jms.config;

import nl.shashi.playground.jms.service.handler.JmsMessageHandler;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.jms.JmsDestinationPollingSource;
import org.springframework.integration.jms.dsl.Jms;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.connection.JmsTransactionManager;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.ConnectionFactory;
import java.util.concurrent.TimeUnit;

import static org.springframework.integration.dsl.Pollers.fixedDelay;

@Configuration
public class JmsPollerConfiguration {

    public static final String LEADER_ROLE = "leader";

    @Value("${activemq.broker-urls:tcp://localhost:61616}")
    private String brokerUrl;

    @Bean
    public IntegrationFlow queueRetryHandler(MessageSource<Object> jmsRetrySource, JmsMessageHandler messageHandler,
            JmsTransactionManager jmsTransactionManager) {
        return IntegrationFlows
                .from(jmsRetrySource, configurer -> configurer.role(LEADER_ROLE)
                        .autoStartup(false)
                        .poller(
                                fixedDelay(1000, TimeUnit.MILLISECONDS)
                                .receiveTimeout(1000L)
                                .maxMessagesPerPoll(5)
                                .transactional(jmsTransactionManager)))
                .handle(messageHandler)
                .get();
    }



    @Bean
    public IntegrationFlow jmsBoundAdapter(ConnectionFactory connectionFactory, JmsTransactionManager jmsTransactionManager) {
        return IntegrationFlows
                .from(Jms.inboundAdapter(getJmsTemplate(connectionFactory,"closeAccountListQueue")),
                        configurer -> configurer.role(LEADER_ROLE)
                                .autoStartup(false)
                                .poller(
                                        Pollers.fixedDelay(10)
                                                .maxMessagesPerPoll(2)
                                                .transactional(jmsTransactionManager))
                )
                .handle(Jms.outboundAdapter(getJmsTemplate(connectionFactory,"closeAccountNotification"))
                )
                .get();
    }


    @Bean
    public MessageSource<Object> jmsRetrySource(JmsTemplate newAccountQueue) {
        var messageSource = new JmsDestinationPollingSource(newAccountQueue);
        messageSource.setDestination(newAccountQueue.getDefaultDestination());
        return messageSource;
    }

    @Bean
    public JmsTemplate newAccountQueue(ConnectionFactory connectionFactory) {
        return getJmsTemplate(connectionFactory, "newAccountQueue");
    }

    @Bean("jmsTransactionManager")
    public JmsTransactionManager jmsTransactionManager(ConnectionFactory connectionFactory) {
        return new JmsTransactionManager(connectionFactory);
    }

    @Bean
    public CachingConnectionFactory connectionFactory() {
        return new CachingConnectionFactory(new ActiveMQConnectionFactory(brokerUrl));
    }

    private JmsTemplate getJmsTemplate(ConnectionFactory connectionFactory, String startExecutionQueue) {
        var jmsTemplate = new JmsTemplate(connectionFactory);
        jmsTemplate.setSessionTransacted(true);
        jmsTemplate.setReceiveTimeout(1000L);
        jmsTemplate.setDefaultDestinationName(startExecutionQueue);
        return jmsTemplate;
    }


}
