package com.academy.fintech.pe.core.service.payment_schedule.db.scheduled_payment;

import com.academy.fintech.pe.core.service.payment_schedule.db.payment_schedule.PaymentSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduledPaymentRepository extends JpaRepository<ScheduledPayment, Long> {
    List<ScheduledPayment> getScheduledPaymentsByPaymentSchedule(PaymentSchedule paymentSchedule);

    ScheduledPayment getScheduledPaymentByPaymentScheduleAndPeriodNumber(PaymentSchedule paymentSchedule,
                                                                         int periodNumber);

}
