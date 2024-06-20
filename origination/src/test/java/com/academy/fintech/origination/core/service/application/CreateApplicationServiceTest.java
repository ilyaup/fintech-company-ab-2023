package com.academy.fintech.origination.core.service.application;

import com.academy.fintech.origination.core.service.application.db.application.ApplicationService;
import com.academy.fintech.origination.core.service.application.db.client.ClientService;
import com.academy.fintech.origination.core.service.application.exception.DuplicateApplicationRuntimeException;
import com.academy.fintech.origination.public_interface.application.ApplicationStatus;
import com.academy.fintech.origination.public_interface.application.dto.ApplicationDto;
import com.academy.fintech.origination.public_interface.application.dto.ApplicationRequestDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CreateApplicationServiceTest {

    private final ApplicationService applicationService = mock(ApplicationService.class);
    private final ClientService clientService = mock(ClientService.class);

    private CreateApplicationService createApplicationService;

    @BeforeEach
    void setUp() {
        createApplicationService =
                new CreateApplicationService(applicationService, clientService);
    }

    @Test
    void createApplicationNoDuplicateTest() {
        String clientId = "client_1";
        String applicationId = "application_1";
        ApplicationRequestDto requestDto = ApplicationRequestDto.builder()
                .firstName("kek")
                .lastName("kekovich")
                .email("kek@yandex.ru")
                .salary(200_000)
                .amount(490_000)
                .build();
        ApplicationDto applicationDto = ApplicationDto.builder()
                .clientId(clientId)
                .requestedDisbursementAmount(new BigDecimal(requestDto.amount()).setScale(2,
                        RoundingMode.HALF_UP))
                .applicationStatus(ApplicationStatus.NEW)
                .build();

        when(clientService.getClientIdOrCreateClient(any())).thenReturn(clientId);

        when(applicationService.saveApplication(applicationDto)).thenReturn(applicationId);

        when(applicationService.getExistingSameNewApplicationId(applicationDto)).thenReturn(Optional.empty());

        assertEquals(applicationId, createApplicationService.createApplication(requestDto));
    }

    @Test
    void createApplicationDuplicateExceptionTest() {
        String clientId = "client_1";
        String applicationId = "application_1";
        ApplicationRequestDto requestDto = ApplicationRequestDto.builder()
                .firstName("kek")
                .lastName("kekovich")
                .email("kek@yandex.ru")
                .salary(200_000)
                .amount(490_000)
                .build();
        ApplicationDto applicationDto = ApplicationDto.builder()
                .clientId(clientId)
                .requestedDisbursementAmount(new BigDecimal(requestDto.amount()).setScale(2, RoundingMode.HALF_UP))
                .applicationStatus(ApplicationStatus.NEW)
                .build();

        when(clientService.getClientIdOrCreateClient(any())).thenReturn(clientId);

        when(applicationService.getExistingSameNewApplicationId(applicationDto)).thenReturn(
                Optional.of(applicationId)
        );

        DuplicateApplicationRuntimeException exception = assertThrows(DuplicateApplicationRuntimeException.class,
                () -> createApplicationService.createApplication(requestDto));

        assertEquals(exception.getApplicationId(), applicationId);
    }
}