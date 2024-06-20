package com.academy.fintech.pe.core.service.agreement;

import com.academy.fintech.pe.core.service.agreement.db.agreement.Agreement;
import com.academy.fintech.pe.core.service.agreement.db.agreement.AgreementRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class AgreementActivationService {

    private final AgreementRepository agreementRepository;

    @Transactional
    public void activate(int agreementId, LocalDate disbursementDate, LocalDate nextPaymentDate) {
        Agreement agreement = agreementRepository.getAgreementById(agreementId);
        agreement.setDisbursementDate(disbursementDate);
        agreement.setNextPaymentDate(nextPaymentDate);
        agreement.setStatus("ACTIVE");
    }

}
