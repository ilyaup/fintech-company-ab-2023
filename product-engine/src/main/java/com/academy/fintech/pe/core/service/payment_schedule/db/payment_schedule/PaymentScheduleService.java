package com.academy.fintech.pe.core.service.payment_schedule.db.payment_schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service which works with {@link PaymentScheduleService}.
 */
@Service
@RequiredArgsConstructor
public class PaymentScheduleService {

    private final PaymentScheduleRepository paymentScheduleRepository;

    /**
     * Saves the given schedule to the repository via {@link PaymentScheduleRepository#save(Object)} and returns the
     * saved schedule.
     *
     * @param paymentSchedule is the schedule to save.
     * @return the saved schedule.
     */

    public PaymentSchedule createSchedule(PaymentSchedule paymentSchedule) {
        return paymentScheduleRepository.save(paymentSchedule);
    }
}
