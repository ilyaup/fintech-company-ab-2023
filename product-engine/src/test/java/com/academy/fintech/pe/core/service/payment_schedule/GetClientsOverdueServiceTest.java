package com.academy.fintech.pe.core.service.payment_schedule;

import com.academy.fintech.pe.core.service.payment_schedule.db.scheduled_payment.ScheduledPaymentService;
import com.academy.fintech.pe.public_interface.scheduled_payment.dto.PaymentDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetClientsOverdueServiceTest {

    @Mock
    private ScheduledPaymentService scheduledPaymentService;

    @InjectMocks
    private GetClientsOverdueService getClientsOverdueService;

    @Test
    void test1_getDaysOfMostOverduePaymentByClientId() {
        LocalDate currentDate = LocalDate.of(2023, 11, 30);
        List<PaymentDto> payments = new ArrayList<>();
        payments.add(PaymentDto.builder().paymentDate(LocalDate.of(2024, 1, 1)).build());
        payments.add(PaymentDto.builder().paymentDate(LocalDate.of(2024, 4, 3)).build());
        payments.add(PaymentDto.builder().paymentDate(LocalDate.of(2023, 11, 20)).build());
        payments.add(PaymentDto.builder().paymentDate(LocalDate.of(2023, 11, 19)).build());

        when(scheduledPaymentService.getOverduePaymentsByClientId(anyString())).thenReturn(payments);

        assertEquals(11L,
                getClientsOverdueService.getDaysOfMostOverduePaymentByClientId("any", currentDate));
    }

    @Test
    void test2_getDaysOfMostOverduePaymentByClientId() {
        LocalDate currentDate = LocalDate.of(2023, 11, 30);
        List<PaymentDto> payments = new ArrayList<>();
        payments.add(PaymentDto.builder().paymentDate(LocalDate.of(2024, 1, 1)).build());
        payments.add(PaymentDto.builder().paymentDate(LocalDate.of(2024, 4, 3)).build());
        payments.add(PaymentDto.builder().paymentDate(LocalDate.of(2023, 11, 20)).build());
        payments.add(PaymentDto.builder().paymentDate(LocalDate.of(2023, 10, 19)).build());

        when(scheduledPaymentService.getOverduePaymentsByClientId(anyString())).thenReturn(payments);

        assertEquals(ChronoUnit.DAYS.between(
                        LocalDate.of(2023, 10, 19),
                        currentDate),
                getClientsOverdueService.getDaysOfMostOverduePaymentByClientId("any", currentDate));
    }

    @Test
    void test2_emptyPayments_getDaysOfMostOverduePaymentByClientId() {
        LocalDate currentDate = LocalDate.of(2023, 11, 30);
        List<PaymentDto> payments = new ArrayList<>();

        when(scheduledPaymentService.getOverduePaymentsByClientId(anyString())).thenReturn(payments);

        assertEquals(0L, getClientsOverdueService.getDaysOfMostOverduePaymentByClientId("any", currentDate));
    }

    @Test
    void test2_notOverduePayments_getDaysOfMostOverduePaymentByClientId() {
        LocalDate currentDate = LocalDate.of(2023, 11, 30);
        List<PaymentDto> payments = new ArrayList<>();
        payments.add(PaymentDto.builder().paymentDate(LocalDate.of(2024, 1, 1)).build());
        payments.add(PaymentDto.builder().paymentDate(LocalDate.of(2024, 4, 3)).build());
        payments.add(PaymentDto.builder().paymentDate(LocalDate.of(2024, 11, 20)).build());
        payments.add(PaymentDto.builder().paymentDate(LocalDate.of(2024, 10, 19)).build());

        when(scheduledPaymentService.getOverduePaymentsByClientId(anyString())).thenReturn(payments);

        assertEquals(0L, getClientsOverdueService.getDaysOfMostOverduePaymentByClientId("any", currentDate));
    }
}
