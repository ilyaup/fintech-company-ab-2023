package com.academy.fintech.pe.core.service.payment_schedule.db.scheduled_payment;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ScheduledPaymentServiceTest {

    // @Mock
    // private PaymentScheduleRepository paymentScheduleRepository;

    // @InjectMocks
    // private ScheduledPaymentService scheduledPaymentService;

    // private ScheduledPayment createPayment(int id, String status) {
    //     return ScheduledPayment.builder()
    //         .paymentSchedule(PaymentSchedule.builder().id(id).build())
    //         .status(status).id(id).build();
    // }

    // @Test
    // void test1_GetOverduePaymentsByClientId() {
    //     String clientId = "client_1";
    //     List<PaymentSchedule> schedules = new ArrayList<>();
    //     schedules.add(PaymentSchedule.builder().ScheduledPayments(List.of(
    //             createPayment(0, "OVERDUE"),
    //             createPayment(1, "FUTURE"),
    //             createPayment(2, "OVERDUE"),
    //             createPayment(3, "OVERDUE"))).build());
    //     schedules.add(PaymentSchedule.builder().ScheduledPayments(List.of(
    //             createPayment(4, "OVERDUE"),
    //             createPayment(5, "FUTURE"),
    //             createPayment(6, "OVERDUE"))).build());

    //     when(paymentScheduleRepository.getPaymentSchedulesByClientId(clientId)).thenReturn(schedules);

    //     assertEquals(5, scheduledPaymentService.getOverduePaymentsByClientId(clientId).size());
    // }
}
