package com.academy.fintech.origination.core.service.application.db.application;

import com.academy.fintech.origination.core.service.application.db.client.Client;
import com.academy.fintech.origination.public_interface.application.ApplicationStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ApplicationRepository extends JpaRepository<Application, Long> {
    boolean existsById(String id);

    Optional<Application> findById(String id);

    Optional<Application> getApplicationById(String id);

    List<Application> getByClientAndAndRequestedDisbursementAmount(Client client,
                                                                   BigDecimal requestedDisbursementAmount);

    Optional<Application> getApplicationByStatus(ApplicationStatus status);
}
