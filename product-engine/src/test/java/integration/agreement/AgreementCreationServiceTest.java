package integration.agreement;

import com.academy.fintech.agreement_creation.AgreementCreationRequest;
import com.academy.fintech.agreement_creation.AgreementCreationResponse;
import com.academy.fintech.agreement_creation.AgreementCreationServiceGrpc;
import com.academy.fintech.pe.Application;
import com.academy.fintech.pe.core.service.agreement.db.agreement.Agreement;
import com.academy.fintech.pe.core.service.agreement.db.agreement.AgreementRepository;
import com.academy.fintech.pe.core.service.agreement.db.product.Product;
import com.academy.fintech.pe.core.service.agreement.db.product.ProductRepository;
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

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = Application.class,
        properties = {
                "spring.datasource.url=jdbc:tc:postgresql:///pe",
                "spring.datasource.driver-class-name=org.testcontainers.jdbc.ContainerDatabaseDriver"
        })
@RequiredArgsConstructor
public class AgreementCreationServiceTest {

    private static AgreementCreationServiceGrpc.AgreementCreationServiceBlockingStub stub;

    @Autowired
    private AgreementRepository agreementRepository;

    @Autowired
    private ProductRepository productRepository;

    @BeforeAll
    public static void setup() {
        Channel channel = ManagedChannelBuilder.forAddress("localhost", 9095).usePlaintext().build();
        stub = AgreementCreationServiceGrpc.newBlockingStub(channel);
    }

    public static AgreementCreationResponse createAgreement(AgreementCreationRequest request) {
        return stub.create(request);
    }

    @Test
    void contextLoads() {
    }

    @Test
    void testCreate1() {
        Product product = productRepository.getProductByCode(1);
        Agreement expectedAgreement = Agreement.builder()
                .id(1)
                .product(product)
                .clientId("1")
                .term(3)
                .principalAmount(new BigDecimal("70000").add(product.getMaxOriginationAmount()))
                .originationAmount(product.getMaxOriginationAmount())
                .interest(new BigDecimal("0.12").setScale(2, RoundingMode.HALF_UP))
                .status("NEW")
                .disbursementDate(null)
                .nextPaymentDate(null)
                .build();
        AgreementCreationRequest request = AgreementCreationRequest
                .newBuilder()
                .setProductCode(1)
                .setClientId("1")
                .setTerm(3)
                .setDisbursementAmount("70000")
                .setInterest("0.12")
                .build();
        AgreementCreationResponse response = createAgreement(request);
        Agreement actualAgreement = agreementRepository.getAgreementById(response.getId());
        assertEquals(expectedAgreement, actualAgreement);
    }

    @Test
    void testCreate2InvalidInterest() {
        AgreementCreationRequest request = AgreementCreationRequest
                .newBuilder()
                .setProductCode(1)
                .setClientId("1")
                .setTerm(3)
                .setDisbursementAmount("70000")
                .setInterest("0.155")
                .build();
        AgreementCreationResponse response = createAgreement(request);
        assertFalse(response.hasId());
    }

    @Test
    void testCreate3InvalidTerm() {
        AgreementCreationRequest request = AgreementCreationRequest
                .newBuilder()
                .setProductCode(1)
                .setClientId("1")
                .setTerm(1)
                .setDisbursementAmount("70000")
                .setInterest("0.12")
                .build();
        AgreementCreationResponse response = createAgreement(request);
        assertFalse(response.hasId());
    }

    @Test
    void testCreate4InvalidPrincipalAmount() {
        AgreementCreationRequest request = AgreementCreationRequest
                .newBuilder()
                .setProductCode(1)
                .setClientId("1")
                .setTerm(1)
                .setDisbursementAmount("700000000")
                .setInterest("0.12")
                .build();
        AgreementCreationResponse response = createAgreement(request);
        assertFalse(response.hasId());
    }
}
