package integration;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = com.academy.fintech.origination.Application.class, properties = {
        "spring.datasource.url=jdbc:tc:postgresql:///origination",
        "spring.datasource.driver-class-name=org.testcontainers.jdbc.ContainerDatabaseDriver",
        "scheduler.enabled=true"
})
@ExtendWith(MockitoExtension.class)
public class ApplicationToScoringServiceTest {

}
