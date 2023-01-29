package nl.shashi.playground.jms;

import nl.shashi.playground.artemis.RunArtemis;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.integration.config.EnableIntegration;


@SpringBootApplication
@EnableIntegration
public class JmsApplication {

    public static void main(String[] args) throws Exception {
//        RunArtemis.kick();
        SpringApplication.run(JmsApplication.class, args);
    }
}
