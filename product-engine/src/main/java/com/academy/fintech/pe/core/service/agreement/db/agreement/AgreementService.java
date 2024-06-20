package com.academy.fintech.pe.core.service.agreement.db.agreement;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service which works with {@link AgreementRepository}.
 */
@Service
@RequiredArgsConstructor
public class AgreementService {

    private final AgreementRepository agreementRepository;

    /**
     * Saves an agreement to the {@link AgreementRepository}.
     *
     * @param agreement to save in the repository.
     * @return the agreement id.
     */
    public int create(Agreement agreement) {
        return agreementRepository.save(agreement).getId();
    }
}
