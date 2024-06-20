package integration;

import com.academy.fintech.application.ApplicationRequest;
import com.academy.fintech.application.ApplicationResponse;
import com.academy.fintech.application.ApplicationServiceGrpc;
import com.academy.fintech.application.CancelApplicationRequest;
import com.academy.fintech.application.CancelApplicationResponse;
import com.academy.fintech.origination.core.service.application.db.application.Application;
import com.academy.fintech.origination.core.service.application.db.application.ApplicationRepository;
import com.academy.fintech.origination.core.service.application.db.client.Client;
import com.academy.fintech.origination.core.service.application.db.client.ClientRepository;
import com.academy.fintech.origination.public_interface.application.ApplicationStatus;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = com.academy.fintech.origination.Application.class,
        properties = {
                "spring.datasource.url=jdbc:tc:postgresql:///origination",
                "spring.datasource.driver-class-name=org.testcontainers.jdbc.ContainerDatabaseDriver",
                "scheduler.enabled=false"
        })
@RequiredArgsConstructor
public class ApplicationServiceV1CancelTest {
    public static ApplicationServiceGrpc.ApplicationServiceBlockingStub stub;

    @Autowired
    private ApplicationRepository applicationRepository;

    @Autowired
    private ClientRepository clientRepository;

    @BeforeAll
    public static void setup() {
        Channel channel = ManagedChannelBuilder.forAddress("localhost", 9094).usePlaintext().build();
        stub = ApplicationServiceGrpc.newBlockingStub(channel);
    }

    @Test
    void contextLoads() {
    }

    @Test
    void cancel_newApp_canceled() {
        Client client = clientRepository.findById("client_1").orElseThrow(); // походу надо регать если юзера нет
        BigDecimal requestedDisbursementAmount = new BigDecimal("490000").setScale(2, RoundingMode.HALF_UP);
        ApplicationRequest createRequest = ApplicationRequest.newBuilder()
                .setFirstName(client.getFirstName())
                .setLastName(client.getLastName())
                .setEmail(client.getEmail())
                .setSalary(client.getSalary().intValue())
                .setDisbursementAmount(requestedDisbursementAmount.intValue())
                .build();
        ApplicationResponse createResponse = stub.create(createRequest);

        CancelApplicationRequest cancelRequest = CancelApplicationRequest.newBuilder()
                .setApplicationId(createResponse.getApplicationId())
                .build();

        CancelApplicationResponse cancelResponse = stub.cancel(cancelRequest);

        Application application = applicationRepository.getApplicationById(createResponse.getApplicationId())
                .orElseThrow();

        assertTrue(cancelResponse.getCanceled());

        assertEquals(ApplicationStatus.CANCELLED, application.getStatus());
    }

    @Test
    void cancel_activeApp_notCanceled() {
        Client client = clientRepository.findById("client_1").orElseThrow(); // походу надо регать если юзера нет
        BigDecimal requestedDisbursementAmount = new BigDecimal("490000").setScale(2, RoundingMode.HALF_UP);
        ApplicationRequest createRequest = ApplicationRequest.newBuilder()
                .setFirstName(client.getFirstName())
                .setLastName(client.getLastName())
                .setEmail(client.getEmail())
                .setSalary(client.getSalary().intValue())
                .setDisbursementAmount(requestedDisbursementAmount.intValue())
                .build();

        ApplicationResponse createResponse = stub.create(createRequest);
        Application tempApplication = applicationRepository.getApplicationById(createResponse.getApplicationId())
                .orElseThrow();
        tempApplication.setStatus(ApplicationStatus.ACTIVE);
        applicationRepository.save(tempApplication);

        CancelApplicationRequest cancelRequest = CancelApplicationRequest.newBuilder()
                .setApplicationId(createResponse.getApplicationId())
                .build();

        CancelApplicationResponse cancelResponse = stub.cancel(cancelRequest);

        Application application = applicationRepository.getApplicationById(createResponse.getApplicationId())
                .orElseThrow();

        assertFalse(cancelResponse.getCanceled());

        assertEquals(ApplicationStatus.ACTIVE, application.getStatus());
    }

    @Test
    void cancel_notExisting_notCanceled() {
        String applicationId = "application_123";
        CancelApplicationRequest cancelRequest = CancelApplicationRequest.newBuilder()
                .setApplicationId(applicationId)
                .build();

        CancelApplicationResponse cancelResponse = stub.cancel(cancelRequest);

        assertFalse(cancelResponse.getCanceled());
    }
}
