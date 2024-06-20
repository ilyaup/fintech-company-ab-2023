package com.academy.fintech.origination.core.service.application;

import com.academy.fintech.origination.core.service.application.db.application.ApplicationService;
import com.academy.fintech.origination.public_interface.application.ApplicationStatus;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CancelApplicationServiceTest {

    @Test
    void cancelExistingNewApplicationTest() {
        final ApplicationService applicationService = mock(ApplicationService.class);
        final CancelApplicationService cancelApplicationService = new CancelApplicationService(applicationService);
        String applicationId = "application_1";

        when(applicationService.existsById(applicationId)).thenReturn(true);

        when(applicationService.getStatusById(applicationId)).thenReturn(ApplicationStatus.NEW);

        when(applicationService.cancelApplicationById(applicationId)).thenReturn(true);

        assertTrue(cancelApplicationService.cancelApplication(applicationId));
    }

    @Test
    void cancelNotExistingApplicationTest() {
        final ApplicationService applicationService = mock(ApplicationService.class);
        final CancelApplicationService cancelApplicationService = new CancelApplicationService(applicationService);
        String applicationId = "application_1";

        when(applicationService.existsById(applicationId)).thenReturn(false);

        when(applicationService.getStatusById(applicationId)).thenReturn(ApplicationStatus.NEW);

        when(applicationService.cancelApplicationById(applicationId)).thenReturn(true);

        assertFalse(cancelApplicationService.cancelApplication(applicationId));
    }

    @Test
    void cancelExistingScoringApplicationTest() {
        final ApplicationService applicationService = mock(ApplicationService.class);
        final CancelApplicationService cancelApplicationService = new CancelApplicationService(applicationService);
        String applicationId = "application_1";

        when(applicationService.existsById(applicationId)).thenReturn(true);

        when(applicationService.getStatusById(applicationId)).thenReturn(ApplicationStatus.SCORING);

        when(applicationService.cancelApplicationById(applicationId)).thenReturn(true);

        assertFalse(cancelApplicationService.cancelApplication(applicationId));
    }

    @Test
    void cancelExistingAcceptedApplicationTest() {
        final ApplicationService applicationService = mock(ApplicationService.class);
        final CancelApplicationService cancelApplicationService = new CancelApplicationService(applicationService);
        String applicationId = "application_1";

        when(applicationService.existsById(applicationId)).thenReturn(true);

        when(applicationService.getStatusById(applicationId)).thenReturn(ApplicationStatus.ACCEPTED);

        when(applicationService.cancelApplicationById(applicationId)).thenReturn(true);

        assertFalse(cancelApplicationService.cancelApplication(applicationId));
    }

    @Test
    void cancelExistingActiveApplicationTest() {
        final ApplicationService applicationService = mock(ApplicationService.class);
        final CancelApplicationService cancelApplicationService = new CancelApplicationService(applicationService);
        String applicationId = "application_1";

        when(applicationService.existsById(applicationId)).thenReturn(true);

        when(applicationService.getStatusById(applicationId)).thenReturn(ApplicationStatus.ACTIVE);

        when(applicationService.cancelApplicationById(applicationId)).thenReturn(true);

        assertFalse(cancelApplicationService.cancelApplication(applicationId));
    }

    @Test
    void cancelExistingClosedApplicationTest() {
        final ApplicationService applicationService = mock(ApplicationService.class);
        final CancelApplicationService cancelApplicationService = new CancelApplicationService(applicationService);
        String applicationId = "application_1";

        when(applicationService.existsById(applicationId)).thenReturn(true);

        when(applicationService.getStatusById(applicationId)).thenReturn(ApplicationStatus.CLOSED);

        when(applicationService.cancelApplicationById(applicationId)).thenReturn(true);

        assertFalse(cancelApplicationService.cancelApplication(applicationId));
    }

    @Test
    void cancelExistingCanceledApplicationTest() {
        final ApplicationService applicationService = mock(ApplicationService.class);
        final CancelApplicationService cancelApplicationService = new CancelApplicationService(applicationService);
        String applicationId = "application_1";

        when(applicationService.existsById(applicationId)).thenReturn(true);

        when(applicationService.getStatusById(applicationId)).thenReturn(ApplicationStatus.CANCELLED);

        when(applicationService.cancelApplicationById(applicationId)).thenReturn(true);

        assertFalse(cancelApplicationService.cancelApplication(applicationId));
    }
}