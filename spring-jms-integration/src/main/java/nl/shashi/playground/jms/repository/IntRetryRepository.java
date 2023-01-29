package nl.shashi.playground.jms.repository;

import nl.shashi.playground.jms.domain.InitIdClass;
import nl.shashi.playground.jms.domain.IntRetryMetaData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IntRetryRepository extends JpaRepository<IntRetryMetaData, InitIdClass> {
}
