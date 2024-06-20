package com.academy.fintech.origination.core.service.application.db.application;

import com.academy.fintech.origination.core.service.application.db.client.Client;
import com.academy.fintech.origination.core.service.application.db.client.ClientService;
import com.academy.fintech.origination.public_interface.application.ApplicationStatus;
import com.academy.fintech.origination.public_interface.application.dto.ApplicationDto;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ApplicationServiceTest {

    @Test
    void getExistingSameNewApplicationIdTestEmpty() {
        ApplicationRepository applicationRepository = mock(ApplicationRepository.class);
        ClientService clientService = mock(ClientService.class);

        String clientId = "client_1";
        ApplicationDto applicationDto = ApplicationDto.builder()
                .clientId(clientId)
                .requestedDisbursementAmount(new BigDecimal("490000"))
                .build();
        Client client = Client.builder()
                .firstName("kek")
                .lastName("kekovich")
                .email("kek@yandex.ru")
                .salary(new BigDecimal("200000"))
                .applications(new ArrayList<>())
                .build();

        ApplicationService applicationService = new ApplicationService(applicationRepository, clientService);

        when(clientService.getClientDaoById(clientId)).thenReturn(client);

        when(applicationRepository.getByClientAndAndRequestedDisbursementAmount(client,
                applicationDto.requestedDisbursementAmount()))
                .thenReturn(new ArrayList<>());

        assertEquals(Optional.empty(),
                applicationService.getExistingSameNewApplicationId(applicationDto));
    }

    @Test
    void getExistingSameNewApplicationIdTestNoNew() {
        ApplicationRepository applicationRepository = mock(ApplicationRepository.class);
        ClientService clientService = mock(ClientService.class);

        String clientId = "client_1";
        BigDecimal requestedDisbursement = new BigDecimal("490000");
        ApplicationDto applicationDto = ApplicationDto.builder()
                .clientId(clientId)
                .requestedDisbursementAmount(new BigDecimal("490000"))
                .build();
        Client client = Client.builder()
                .firstName("kek")
                .lastName("kekovich")
                .email("kek@yandex.ru")
                .salary(new BigDecimal("200000"))
                .applications(List.of())
                .build();

        Application app1 = Application.builder()
                .id("1")
                .requestedDisbursementAmount(requestedDisbursement)
                .client(client)
                .status(ApplicationStatus.SCORING)
                .build();

        Application app2 = Application.builder()
                .id("2")
                .requestedDisbursementAmount(requestedDisbursement)
                .client(client)
                .status(ApplicationStatus.CANCELLED)
                .build();

        ApplicationService applicationService = new ApplicationService(applicationRepository, clientService);

        when(clientService.getClientDaoById(clientId)).thenReturn(client);

        when(applicationRepository.getByClientAndAndRequestedDisbursementAmount(client,
                applicationDto.requestedDisbursementAmount()))
                .thenReturn(List.of(app1, app2));

        assertEquals(Optional.empty(),
                applicationService.getExistingSameNewApplicationId(applicationDto));
    }

    @Test
    void getExistingSameNewApplicationIdTestOneNew() {
        ApplicationRepository applicationRepository = mock(ApplicationRepository.class);
        ClientService clientService = mock(ClientService.class);

        String clientId = "client_1";
        BigDecimal requestedDisbursement = new BigDecimal("490000");
        ApplicationDto applicationDto = ApplicationDto.builder()
                .clientId(clientId)
                .requestedDisbursementAmount(new BigDecimal("490000"))
                .build();
        Client client = Client.builder()
                .firstName("kek")
                .lastName("kekovich")
                .email("kek@yandex.ru")
                .salary(new BigDecimal("200000"))
                .applications(List.of())
                .build();

        Application app1 = Application.builder()
                .id("1")
                .requestedDisbursementAmount(requestedDisbursement)
                .client(client)
                .status(ApplicationStatus.NEW)
                .build();

        Application app2 = Application.builder()
                .id("2")
                .requestedDisbursementAmount(requestedDisbursement)
                .client(client)
                .status(ApplicationStatus.CANCELLED)
                .build();

        ApplicationService applicationService = new ApplicationService(applicationRepository, clientService);

        when(clientService.getClientDaoById(clientId)).thenReturn(client);

        when(applicationRepository.getByClientAndAndRequestedDisbursementAmount(client,
                applicationDto.requestedDisbursementAmount()))
                .thenReturn(List.of(app1, app2));

        assertEquals(Optional.of(app1.getId()),
                applicationService.getExistingSameNewApplicationId(applicationDto));
    }
}