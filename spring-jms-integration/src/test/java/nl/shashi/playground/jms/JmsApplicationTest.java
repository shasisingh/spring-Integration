package nl.shashi.playground.jms;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class JmsApplicationTest {

    @Autowired
    ApplicationContext context;

    @Test
    void testContextLoads() {
        assertEquals("jms-spring-integration", context.getId());
    }
}
