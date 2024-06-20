package com.academy.fintech.origination.core.service.application;

import com.academy.fintech.origination.core.service.application.db.application.Application;
import com.academy.fintech.origination.core.service.application.db.application.ApplicationService;
import com.academy.fintech.origination.core.service.application.db.client.ClientService;
import com.academy.fintech.origination.core.service.application.exception.DuplicateApplicationRuntimeException;
import com.academy.fintech.origination.public_interface.application.ApplicationStatus;
import com.academy.fintech.origination.public_interface.application.dto.ApplicationDto;
import com.academy.fintech.origination.public_interface.application.dto.ApplicationRequestDto;
import com.academy.fintech.origination.public_interface.client.dto.ClientDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Service for creating {@link Application}s.
 */
@Service
@RequiredArgsConstructor
public class CreateApplicationService {

    private final ApplicationService applicationService;

    private final ClientService clientService;

    /**
     * Firstly, gets client id from {@link ClientService#getClientIdOrCreateClient(ClientDto)}. Then checks whether
     * this request is duplicate. It considers duplicate if there is application with the same client, disbursement and
     * is still new. If it is duplicate, then throws {@link DuplicateApplicationRuntimeException}. Otherwise, builds
     * application and saves it using {@link ApplicationService#saveApplication(ApplicationDto)} and returns application
     * id.
     *
     * @param requestDto contains information for creating application.
     * @return id of created application.
     * @throws DuplicateApplicationRuntimeException if request is duplicate.
     */
    public String createApplication(ApplicationRequestDto requestDto) throws DuplicateApplicationRuntimeException {
        ClientDto clientDto = clientByApplicationRequest(requestDto);

        String clientId = clientService.getClientIdOrCreateClient(clientDto);

        ApplicationDto applicationDto = ApplicationDto.builder()
                .clientId(clientId)
                .requestedDisbursementAmount(new BigDecimal(requestDto.amount())
                        .setScale(2, RoundingMode.HALF_UP))
                .applicationStatus(ApplicationStatus.NEW)
                .build();

        checkDuplicate(applicationDto); // may throw DuplicateApplicationRuntimeException

        return applicationService.saveApplication(applicationDto);
    }


    private void checkDuplicate(ApplicationDto applicationDto) throws DuplicateApplicationRuntimeException {
        applicationService.getExistingSameNewApplicationId(applicationDto)
                .ifPresent(applicationId -> {
                    throw new DuplicateApplicationRuntimeException(applicationId);
                });
    }

    private ClientDto clientByApplicationRequest(ApplicationRequestDto requestDto) {
        return ClientDto.builder()
                .firstName(requestDto.firstName())
                .lastName(requestDto.lastName())
                .email(requestDto.email())
                .salary(new BigDecimal(requestDto.salary()).setScale(2, RoundingMode.HALF_UP))
                .build();
    }
}
