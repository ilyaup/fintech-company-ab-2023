package integration;

import com.academy.fintech.application.ApplicationRequest;
import com.academy.fintech.application.ApplicationResponse;
import com.academy.fintech.application.ApplicationServiceGrpc;
import com.academy.fintech.origination.core.service.application.db.application.Application;
import com.academy.fintech.origination.core.service.application.db.application.ApplicationRepository;
import com.academy.fintech.origination.core.service.application.db.client.Client;
import com.academy.fintech.origination.core.service.application.db.client.ClientRepository;
import com.academy.fintech.origination.public_interface.application.ApplicationStatus;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Metadata;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = com.academy.fintech.origination.Application.class,
        properties = {
                "spring.datasource.url=jdbc:tc:postgresql:///origination",
                "spring.datasource.driver-class-name=org.testcontainers.jdbc.ContainerDatabaseDriver",
                "scheduler.enabled=false"
        })
@RequiredArgsConstructor
public class ApplicationServiceV1CreateTest {

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
    void create_userExists_App() {
        Client client = clientRepository.findById("client_1").orElseThrow(); // походу надо регать если юзера нет
        BigDecimal requestedDisbursementAmount = new BigDecimal("490000").setScale(2, RoundingMode.HALF_UP);
        Application expectedApplication = Application.builder()
                .client(client)
                .id("application_1")
                .requestedDisbursementAmount(requestedDisbursementAmount)
                .status(ApplicationStatus.NEW)
                .build();
        ApplicationRequest request = ApplicationRequest.newBuilder()
                .setFirstName(client.getFirstName())
                .setLastName(client.getLastName())
                .setEmail(client.getEmail())
                .setSalary(client.getSalary().intValue())
                .setDisbursementAmount(requestedDisbursementAmount.intValue())
                .build();
        ApplicationResponse response = stub.create(request);

        Application actualApplication = applicationRepository.getApplicationById(response.getApplicationId())
                .orElseThrow();

        assertEquals(expectedApplication.toString(), actualApplication.toString());

        assertEquals(ApplicationStatus.NEW, actualApplication.getStatus());

    }

    @Test
    void create_userDoesNotExist_appCreated() {
        Client expectedClient = Client.builder()
                .id("client_2")
                .firstName("omega")
                .lastName("beta")
                .email("beta@gmail.com")
                .salary(new BigDecimal("150000").setScale(2, RoundingMode.HALF_UP))
                .build();
        BigDecimal requestedDisbursementAmount = new BigDecimal("311000").setScale(2, RoundingMode.HALF_UP);
        Application expectedApplication = Application.builder()
                .client(expectedClient)
                .id("application_2")
                .requestedDisbursementAmount(requestedDisbursementAmount)
                .status(ApplicationStatus.NEW)
                .build();
        ApplicationRequest request = ApplicationRequest.newBuilder()
                .setFirstName(expectedClient.getFirstName())
                .setLastName(expectedClient.getLastName())
                .setEmail(expectedClient.getEmail())
                .setSalary(expectedClient.getSalary().intValue())
                .setDisbursementAmount(requestedDisbursementAmount.intValue())
                .build();
        ApplicationResponse response = stub.create(request);

        Application actualApplication = applicationRepository.getApplicationById(response.getApplicationId())
                .orElseThrow();

        assertEquals(expectedApplication.toString(), actualApplication.toString());

        assertEquals(ApplicationStatus.NEW, actualApplication.getStatus());
    }

    @Test
    void create_doubleRequest_appIdInTrailers() {
        Client client = Client.builder()
                .firstName("omega")
                .lastName("beta")
                .email("beta@gmail.com")
                .salary(new BigDecimal("150000").setScale(2, RoundingMode.HALF_UP))
                .build();
        BigDecimal requestedDisbursementAmount = new BigDecimal("311222").setScale(2, RoundingMode.HALF_UP);
        ApplicationRequest request = ApplicationRequest.newBuilder()
                .setFirstName(client.getFirstName())
                .setLastName(client.getLastName())
                .setEmail(client.getEmail())
                .setSalary(client.getSalary().intValue())
                .setDisbursementAmount(requestedDisbursementAmount.intValue())
                .build();

        ApplicationResponse response1 = stub.create(request);

        StatusRuntimeException e = assertThrows(StatusRuntimeException.class, () -> stub.create(request));

        assertEquals(Status.ALREADY_EXISTS, e.getStatus());

        assertNotNull(e.getTrailers());

        assertEquals(response1.getApplicationId(), e
                .getTrailers()
                .get(Metadata.Key.of("application_id", Metadata.ASCII_STRING_MARSHALLER)));

        assertEquals(1, applicationRepository.getByClientAndAndRequestedDisbursementAmount(
                clientRepository.findClientByEmail(client.getEmail()).orElseThrow(),
                requestedDisbursementAmount).size());
    }
}
