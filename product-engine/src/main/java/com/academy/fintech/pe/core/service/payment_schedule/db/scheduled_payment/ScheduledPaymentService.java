package com.academy.fintech.pe.core.service.payment_schedule.db.scheduled_payment;

import com.academy.fintech.pe.core.calculation.payment_schedule.LoanFunctions;
import com.academy.fintech.pe.core.service.agreement.db.agreement.Agreement;
import com.academy.fintech.pe.core.service.agreement.db.agreement.AgreementRepository;
import com.academy.fintech.pe.core.service.payment_schedule.db.payment_schedule.PaymentSchedule;
import com.academy.fintech.pe.core.service.payment_schedule.db.payment_schedule.PaymentScheduleRepository;
import com.academy.fintech.pe.public_interface.scheduled_payment.dto.PaymentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service which works with {@link ScheduledPaymentRepository}.
 */
@Service
@RequiredArgsConstructor
public class ScheduledPaymentService {

    private final AgreementRepository agreementRepository;
    private final ScheduledPaymentRepository scheduledPaymentRepository;
    private final PaymentScheduleRepository paymentScheduleRepository;

    public List<PaymentDto> getOverduePaymentsByClientId(String clientId) {
        return agreementRepository.getAgreementsByClientId(clientId)
                .parallelStream()
                .flatMap(agreement -> paymentScheduleRepository
                        .getPaymentSchedulesByAgreement(agreement).stream())
                .flatMap(schedule -> schedule.getScheduledPayments().stream())
                .filter(payment -> payment.getStatus().equals("OVERDUE"))
                .map(daoPayment -> daoToDto(daoPayment))
                .collect(Collectors.toList());
    }

    private PaymentDto daoToDto(ScheduledPayment payment) {
        return PaymentDto.builder()
                .id(payment.getId())
                .paymentScheduleId(payment.getPaymentSchedule().getId())
                .status(payment.getStatus())
                .paymentDate(payment.getPaymentDate())
                .periodPayment(payment.getPeriodPayment())
                .interestPayment(payment.getInterestPayment())
                .principalPayment(payment.getPrincipalPayment())
                .periodNumber(payment.getPeriodNumber())
                .build();
    }

    /**
     * For the given {@link PaymentSchedule} and {@link LocalDate} of disbursement,
     * creates and saves to the repository
     * scheduled payments, calculating payment's amounts using
     * {@link LoanFunctions}.
     *
     * @param paymentSchedule  the schedule, basing on which payments are creating.
     * @param disbursementDate the date of disbursement.
     * @return list of created scheduled payments.
     */
    public List<ScheduledPayment> createScheduledPaymentsForPaymentSchedule(PaymentSchedule paymentSchedule,
                                                                            LocalDate disbursementDate) {
        Agreement agreement = agreementRepository.getAgreementById(paymentSchedule.getAgreement().getId());
        int term = agreement.getTerm();
        BigDecimal monthRate = LoanFunctions.monthRateFromYearRate(agreement.getInterest());
        BigDecimal principalAmount = agreement.getPrincipalAmount();
        for (int i = 1; i <= term; i++) {
            scheduledPaymentRepository.save(ScheduledPayment.builder()
                    .paymentSchedule(paymentSchedule)
                    .status("FUTURE")
                    .paymentDate(disbursementDate.plusMonths(i))
                    .periodPayment(LoanFunctions.calculatePmt(monthRate, term, principalAmount))
                    .interestPayment(LoanFunctions.calculateIpmt(monthRate, i, term, principalAmount))
                    .principalPayment(LoanFunctions.calculatePpmt(monthRate, i, term, principalAmount))
                    .periodNumber(i)
                    .build());
        }
        return scheduledPaymentRepository.getScheduledPaymentsByPaymentSchedule(paymentSchedule);
    }
}
