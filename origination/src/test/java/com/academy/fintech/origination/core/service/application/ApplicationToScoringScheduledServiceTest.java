package com.academy.fintech.origination.core.service.application;

import com.academy.fintech.origination.core.service.application.db.application.ApplicationService;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


@ExtendWith(MockitoExtension.class)
public class ApplicationToScoringScheduledServiceTest {
    @Mock
    private ApplicationService applicationService;

    @InjectMocks
    private ApplicationToScoringScheduledService toScoringService;

    // @Test
    // void testSendAnyNewApplicationToScoring() {
    //     toScoringService.sendAnyNewApplicationToScoring();

    //     verify(applicationService.setToAnyNewApplicationScoringStatus(), times(1));
    // }
}
