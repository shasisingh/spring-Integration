package nl.shashi.playground.jms.controler;

import nl.shashi.playground.jms.domain.IntRetryMetaData;
import nl.shashi.playground.jms.repository.IntRetryRepository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.util.stream.IntStream;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;

@RestController
@RequestMapping(path = "/api/v1/jms/retry")
public class RetryController {

    private final IntRetryRepository intRetryRepository;

    public RetryController(IntRetryRepository intRetryRepository) {
        this.intRetryRepository = intRetryRepository;
    }

    @PostMapping(value = "/create")
    @Transactional(transactionManager = "jpaTransactionManager",propagation = Propagation.REQUIRES_NEW)
    public String create(){
        IntStream.range(0,100)
                .forEach( v -> intRetryRepository.save( new IntRetryMetaData(randomAlphanumeric(2,10), UUID.randomUUID().toString(), randomAlphanumeric(2,10), randomAlphanumeric(5,10))));
        return "Data is created!";
    }
}
