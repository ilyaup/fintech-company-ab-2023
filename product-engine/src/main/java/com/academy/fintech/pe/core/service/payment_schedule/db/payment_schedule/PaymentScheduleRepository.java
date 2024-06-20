package com.academy.fintech.pe.core.service.payment_schedule.db.payment_schedule;

import com.academy.fintech.pe.core.service.agreement.db.agreement.Agreement;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentScheduleRepository extends JpaRepository<PaymentSchedule, Long> {
    PaymentSchedule getPaymentScheduleById(int id);

    List<PaymentSchedule> getPaymentSchedulesByAgreement(Agreement agreement);
}
