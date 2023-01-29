package nl.shashi.playground.jms.config;

import nl.shashi.playground.jms.service.handler.RetryFileHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.core.MessageSource;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.dsl.Pollers;
import org.springframework.integration.jdbc.JdbcPollingChannelAdapter;

import javax.sql.DataSource;
import java.util.concurrent.TimeUnit;

@Configuration
public class JdbcConfiguration {

    public static final String LEADER_ROLE = "leader";
    private static final int FIX_DELAY_IN_SEC = 10;
    private static final int MAX_MESSAGES_PER_POLL = 10;

    @Bean
    public IntegrationFlow jdbcPoller(MessageSource<Object> jdbcRetryChannel, RetryFileHandler retryFileHandler) {
        return IntegrationFlows.from(jdbcRetryChannel,
                        configurer ->
                                configurer.role(LEADER_ROLE)
                                        .autoStartup(false)
                                        .poller(Pollers.fixedDelay(FIX_DELAY_IN_SEC, TimeUnit.SECONDS)
                                                .maxMessagesPerPoll(MAX_MESSAGES_PER_POLL)))
                .handle(retryFileHandler).get();
    }


    @Bean
    public MessageSource<Object> jdbcRetryChannel(DataSource dataSource) {
        return new JdbcPollingChannelAdapter(dataSource, "SELECT * FROM INT_RETRY_METADATA");
    }


}
