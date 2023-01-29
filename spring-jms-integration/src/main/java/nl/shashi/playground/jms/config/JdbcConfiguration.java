package nl.shashi.playground.jms.config;

import nl.shashi.playground.jms.service.RetryFileHandler;
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

    @Bean
    public IntegrationFlow retryPoller(MessageSource<Object> jdbcRetryChannel, RetryFileHandler retryFileHandler) {
        return IntegrationFlows.from(jdbcRetryChannel,
                        configurer ->
                                configurer.role(LEADER_ROLE)
                                        .autoStartup(true)
                                        .poller(Pollers.fixedDelay(10, TimeUnit.SECONDS)
                                                .maxMessagesPerPoll(10)))
                .log()
                .handle(retryFileHandler).get();
    }

    @Bean
    public RetryFileHandler retryFileHandler(DataSource dataSource) {
        return new RetryFileHandler(dataSource);
    }

    @Bean
    public MessageSource<Object> jdbcRetryChannel(DataSource dataSource) {
        return new JdbcPollingChannelAdapter(dataSource, "SELECT * FROM INT_RETRY_METADATA");
    }


}
