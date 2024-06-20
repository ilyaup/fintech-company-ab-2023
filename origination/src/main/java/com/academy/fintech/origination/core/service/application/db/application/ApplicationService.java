package com.academy.fintech.origination.core.service.application.db.application;


import com.academy.fintech.origination.core.service.application.db.client.ClientService;
import com.academy.fintech.origination.public_interface.application.ApplicationStatus;
import com.academy.fintech.origination.public_interface.application.dto.ApplicationDto;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * Service for working with {@link ApplicationRepository}
 */
@Service
@RequiredArgsConstructor
public class ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final ClientService clientService;

    /**
     * Saves application with information from {@link ApplicationDto}.
     *
     * @param applicationDto contains information for {@link Application}
     * @return application's id.
     */
    public String saveApplication(ApplicationDto applicationDto) {
        Application application = toDao(applicationDto);
        return applicationRepository.save(application).getId();
    }

    private Application toDao(ApplicationDto applicationDto) {
        return Application.builder()
                .client(clientService.getClientDaoById(applicationDto.clientId()))
                .requestedDisbursementAmount(applicationDto.requestedDisbursementAmount())
                .status(applicationDto.applicationStatus())
                .build();
    }

    private ApplicationDto toDto(Application application) {
        return ApplicationDto.builder()
                .clientId(application.getClient().getId())
                .requestedDisbursementAmount(application.getRequestedDisbursementAmount())
                .applicationStatus(application.getStatus())
                .build();
    }

    /**
     * Checks whether application with given id exists.
     *
     * @param applicationId application's id
     * @return boolean, whether application exists.
     */
    public boolean existsById(String applicationId) {
        return applicationRepository.existsById(applicationId);
    }

    public Optional<String> getExistingSameNewApplicationId(ApplicationDto applicationDto) {
        List<Application> applications = applicationRepository.getByClientAndAndRequestedDisbursementAmount(
                clientService.getClientDaoById(applicationDto.clientId()),
                applicationDto.requestedDisbursementAmount());
        return applications.stream()
                .filter(application -> application.getStatus().equals(ApplicationStatus.NEW))
                .map(Application::getId)
                .findFirst();
    }

    /**
     * Returns status of application with given id.
     *
     * @param applicationId application's id.
     * @return application's status.
     */
    public ApplicationStatus getStatusById(String applicationId) {
        return applicationRepository.getApplicationById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application with id = " + applicationId + " does not exist"))
                .getStatus();
    }

    /**
     * Changes application's status to {@link ApplicationStatus#CANCELLED}, if applications with given id exists.
     * Otherwise, throws {@link RuntimeException}. Always returns true (if exception wasn't thrown).
     *
     * @param applicationId application's id.
     * @return true.
     */
    @Transactional
    public boolean cancelApplicationById(String applicationId) {
        applicationRepository.getApplicationById(applicationId)
                .orElseThrow(() -> new RuntimeException("Application with id = " + applicationId + " does not exist"))
                .setStatus(ApplicationStatus.CANCELLED);
        ;
        return true;
    }

    @Transactional
    public ApplicationDto setToAnyNewApplicationScoringStatus() {
        Optional<Application> applicationOptional = applicationRepository.getApplicationByStatus(ApplicationStatus.NEW);
        applicationOptional.ifPresent(application -> application.setStatus(ApplicationStatus.SCORING));
        return applicationOptional.map(this::toDto).orElse(null);
    }
}
