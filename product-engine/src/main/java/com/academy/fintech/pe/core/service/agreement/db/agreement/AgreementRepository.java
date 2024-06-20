package com.academy.fintech.pe.core.service.agreement.db.agreement;


import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AgreementRepository extends JpaRepository<Agreement, Long> {
    Agreement getAgreementById(int id);

    List<Agreement> getAgreementsByClientId(String clientId);
}
