package com.academy.fintech.origination.core.service.application;

import com.academy.fintech.origination.core.service.application.db.application.Application;
import com.academy.fintech.origination.core.service.application.db.application.ApplicationService;
import com.academy.fintech.origination.public_interface.application.ApplicationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service for canceling {@link Application}s.
 */
@Service
@RequiredArgsConstructor
public class CancelApplicationService {

    private final ApplicationService applicationService;

    /**
     * If application exists and has {@link ApplicationStatus#NEW}, then tries to cancel via
     * {@link ApplicationService#cancelApplicationById(String)}.
     *
     * @param applicationId to cancel.
     * @return true if all requirements are satisfied and canceled successfully. Otherwise, returns false.
     */
    public boolean cancelApplication(String applicationId) {
        if (applicationService.existsById(applicationId)
                && applicationService.getStatusById(applicationId).equals(ApplicationStatus.NEW)) {
            return applicationService.cancelApplicationById(applicationId);
        } else {
            return false;
        }
    }
}
