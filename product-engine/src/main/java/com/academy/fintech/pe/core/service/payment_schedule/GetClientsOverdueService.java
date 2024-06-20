package com.academy.fintech.pe.core.service.payment_schedule;

import com.academy.fintech.pe.core.service.payment_schedule.db.scheduled_payment.ScheduledPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

import static java.lang.Math.max;

@Service
@RequiredArgsConstructor
public class GetClientsOverdueService {

    private final ScheduledPaymentService scheduledPaymentService;

    public long getDaysOfMostOverduePaymentByClientId(String clientId, LocalDate currentDate) {
        return scheduledPaymentService.getOverduePaymentsByClientId(clientId)
                .parallelStream()
                .map(paymentDto -> paymentDto.paymentDate())
                .map(paymentDate -> ChronoUnit.DAYS.between(paymentDate, currentDate))
                .reduce(0L, (a, b) -> max(a, b))
                .longValue();
    }
}
