package nl.shashi.playground.jms.config;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.jdbc.lock.DefaultLockRepository;
import org.springframework.integration.jdbc.lock.JdbcLockRegistry;
import org.springframework.integration.jdbc.lock.LockRepository;
import org.springframework.integration.leader.Candidate;
import org.springframework.integration.leader.event.DefaultLeaderEventPublisher;
import org.springframework.integration.support.leader.LockRegistryLeaderInitiator;
import org.springframework.integration.support.locks.LockRegistry;

import javax.sql.DataSource;

/**
 * Isolation Level in JMS is not supported so leader election must read from jpa jpaTransactionManager.
 */
@Configuration
public class LeaderConfiguration {

    @Bean
    public LockRegistryLeaderInitiator leaderInitiator(LockRegistry locks, Candidate candidate, ApplicationEventPublisher applicationEventPublisher) {
        var initiator = new LockRegistryLeaderInitiator(locks, candidate);
        initiator.setLeaderEventPublisher(new DefaultLeaderEventPublisher(applicationEventPublisher));
        return initiator;
    }

    @Bean
    public LockRegistry lockRegistry(LockRepository lockRepository) {
        return new JdbcLockRegistry(lockRepository);

    }

    @Bean
    public LockRepository lockRepository(DataSource dataSource) {
        var defaultLockRepository = new DefaultLockRepository(dataSource);
        defaultLockRepository.setPrefix("APPS_LEADER_");
        return new LockRepositoryDelegate(defaultLockRepository);

    }

}
