package nl.shashi.playground.jms.config;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.integration.jdbc.lock.DefaultLockRepository;
import org.springframework.integration.jdbc.lock.LockRepository;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

@Transactional(transactionManager = "jpaTransactionManager")
public class LockRepositoryDelegate implements LockRepository, InitializingBean {
    private final DefaultLockRepository defaultLockRepository;

    public LockRepositoryDelegate(DefaultLockRepository defaultLockRepository) {
        this.defaultLockRepository = defaultLockRepository;
    }

    @Override
    public void afterPropertiesSet() {
        defaultLockRepository.afterPropertiesSet();
    }

    @Override
    public boolean isAcquired(String lock) {
        return defaultLockRepository.isAcquired(lock);
    }

    @Override
    public void delete(String lock) {
        defaultLockRepository.delete(lock);
    }

    @Override
    public void deleteExpired() {
        defaultLockRepository.deleteExpired();
    }

    @Transactional(transactionManager = "jpaTransactionManager", isolation = Isolation.SERIALIZABLE)
    @Override
    public boolean acquire(String lock) {
        return defaultLockRepository.acquire(lock);
    }

    @Override
    public boolean renew(String lock) {
        return defaultLockRepository.renew(lock);
    }

    @Override
    public void close() {
        defaultLockRepository.close();
    }
}
