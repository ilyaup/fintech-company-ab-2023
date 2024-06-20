package com.academy.fintech.pe.core.service.disbursement;

import com.academy.fintech.disbursement.DisbursementRequest;
import com.academy.fintech.disbursement.DisbursementResponse;
import com.academy.fintech.pe.core.service.agreement.AgreementActivationService;
import com.academy.fintech.pe.core.service.agreement.db.agreement.Agreement;
import com.academy.fintech.pe.core.service.agreement.db.agreement.AgreementRepository;
import com.academy.fintech.pe.core.service.payment_schedule.db.payment_schedule.PaymentSchedule;
import com.academy.fintech.pe.core.service.payment_schedule.db.payment_schedule.PaymentScheduleService;
import com.academy.fintech.pe.core.service.payment_schedule.db.scheduled_payment.ScheduledPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

/**
 * Service which activates the given agreement.
 */
@Service
@RequiredArgsConstructor
public class DisbursementService {

    private final PaymentScheduleService paymentScheduleService;
    private final ScheduledPaymentService scheduledPaymentService;
    private final AgreementRepository agreementRepository;
    private final AgreementActivationService agreementActivationService;

    /**
     * Firstly, creates the payment schedule, using {@link PaymentScheduleService}, basing on the agreement, which id is
     * in {@link DisbursementRequest}. Version of this schedule is 1, as this method activates this agreement, therefore
     * this is the first schedule for the given agreement. Then, creates scheduled payments via
     * {@link ScheduledPaymentService#createScheduledPaymentsForPaymentSchedule(PaymentSchedule, LocalDate)}, passing
     * previously created schedule and {@code LocalDate.now()} as arguments. Then, it changes the {@link Agreement}
     * fields in repository through {@link AgreementActivationService} to actualize the agreement datatable.
     *
     * @param request contains the disbursement date and agreement's id.
     * @return {@link DisbursementResponse}, which contains payment schedule's id, agreement's id and payment schedule's
     * id.
     */
    public DisbursementResponse activate(DisbursementRequest request) {
        LocalDate disbursementDate = LocalDate.parse(request.getDisbursementDate());

        PaymentSchedule paymentSchedule = new PaymentSchedule();
        Agreement agreement = agreementRepository.getAgreementById(request.getAgreementId());
        paymentSchedule.setAgreement(agreement);
        paymentSchedule.setVersion(1); // we activate disbursement, so this is the first version of schedule

        paymentScheduleService.createSchedule(paymentSchedule);

        scheduledPaymentService.createScheduledPaymentsForPaymentSchedule(paymentSchedule, disbursementDate);

        agreementActivationService.activate(agreement.getId(),
                disbursementDate,
                disbursementDate.plusMonths(1));

        return DisbursementResponse.newBuilder()
                .setPaymentScheduleId(paymentSchedule.getId())
                .setAgreementNumber(paymentSchedule.getAgreement().getId())
                .setVersion(paymentSchedule.getVersion())
                .build();
    }
}
