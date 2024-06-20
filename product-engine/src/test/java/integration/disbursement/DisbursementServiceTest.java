package integration.disbursement;

import com.academy.fintech.agreement_creation.AgreementCreationRequest;
import com.academy.fintech.agreement_creation.AgreementCreationResponse;
import com.academy.fintech.agreement_creation.AgreementCreationServiceGrpc;
import com.academy.fintech.disbursement.DisbursementRequest;
import com.academy.fintech.disbursement.DisbursementResponse;
import com.academy.fintech.disbursement.DisbursementServiceGrpc;
import com.academy.fintech.pe.Application;
import com.academy.fintech.pe.core.service.agreement.db.agreement.Agreement;
import com.academy.fintech.pe.core.service.agreement.db.agreement.AgreementRepository;
import com.academy.fintech.pe.core.service.payment_schedule.db.payment_schedule.PaymentSchedule;
import com.academy.fintech.pe.core.service.payment_schedule.db.payment_schedule.PaymentScheduleRepository;
import com.academy.fintech.pe.core.service.payment_schedule.db.scheduled_payment.ScheduledPayment;
import com.academy.fintech.pe.core.service.payment_schedule.db.scheduled_payment.ScheduledPaymentRepository;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT,
        classes = Application.class,
        properties = {
                "spring.datasource.url=jdbc:tc:postgresql:///pe",
                "spring.datasource.driver-class-name=org.testcontainers.jdbc.ContainerDatabaseDriver"
        })
@RequiredArgsConstructor
public class DisbursementServiceTest {

    private static AgreementCreationServiceGrpc.AgreementCreationServiceBlockingStub agreementCreationStub;
    private static DisbursementServiceGrpc.DisbursementServiceBlockingStub disbursementStub;

    @Autowired
    private AgreementRepository agreementRepository;

    @Autowired
    private PaymentScheduleRepository paymentScheduleRepository;

    @Autowired
    private ScheduledPaymentRepository scheduledPaymentRepository;

    @BeforeAll
    public static void setup() {
        Channel channel = ManagedChannelBuilder.forAddress("localhost", 9095).usePlaintext().build();
        agreementCreationStub = AgreementCreationServiceGrpc.newBlockingStub(channel);

        disbursementStub = DisbursementServiceGrpc.newBlockingStub(channel);
    }

    @Test
    void contextLoads() {
    }

    @Test
    void testActivate1() {
        LocalDate disbursementDate = LocalDate.now();
        AgreementCreationRequest agreementCreationRequest = AgreementCreationRequest
                .newBuilder()
                .setProductCode(1)
                .setClientId("1")
                .setTerm(12)
                .setDisbursementAmount("490000")
                .setInterest("0.08")
                .build();
        AgreementCreationResponse agreementCreationResponse = agreementCreationStub.create(agreementCreationRequest);
        Agreement agreement = agreementRepository.getAgreementById(agreementCreationResponse.getId());
        DisbursementRequest request = DisbursementRequest.newBuilder()
                .setAgreementId(agreement.getId())
                .setDisbursementDate(disbursementDate.toString())
                .build();
        DisbursementResponse response = disbursementStub.activate(request);

        Agreement expectedAgreement = Agreement.builder()
                .id(agreement.getId())
                .term(agreement.getTerm())
                .nextPaymentDate(disbursementDate.plusMonths(1))
                .disbursementDate(disbursementDate)
                .product(agreement.getProduct())
                .originationAmount(agreement.getOriginationAmount())
                .principalAmount(agreement.getPrincipalAmount())
                .interest(agreement.getInterest())
                .clientId(agreement.getClientId())
                .status("ACTIVE")
                .build();

        ScheduledPayment expectedPayment = ScheduledPayment.builder()
                .id(9) // doesn't matter
                .paymentSchedule(PaymentSchedule.builder()
                        .id(1) // doesn't matter
                        .agreement(expectedAgreement)
                        .ScheduledPayments(null) // doesn't matter
                        .version(1)
                        .build())
                .status("FUTURE")
                .paymentDate(disbursementDate.plusMonths(9))
                .periodPayment(new BigDecimal("-43494.21"))
                .interestPayment(new BigDecimal("-1140.77"))
                .principalPayment(new BigDecimal("-42353.44"))
                .periodNumber(9)
                .build();

        ScheduledPayment actualPayment =
                scheduledPaymentRepository.getScheduledPaymentByPaymentScheduleAndPeriodNumber(
                        paymentScheduleRepository.getPaymentScheduleById(response.getPaymentScheduleId()),
                        9);
        System.out.println(expectedPayment);
        System.out.println(actualPayment);
        assertEquals(expectedPayment, actualPayment);
    }
}
