package com.academy.fintech.origination.core.service.application;

import com.academy.fintech.origination.core.client.scoring.ScoringClientService;
import com.academy.fintech.origination.core.service.application.db.application.ApplicationService;
import com.academy.fintech.origination.core.service.application.db.client.ClientService;
import com.academy.fintech.origination.core.service.client.EmailSender;
import com.academy.fintech.origination.public_interface.application.dto.ApplicationDto;
import com.academy.fintech.origination.public_interface.client.dto.ClientDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class ApplicationToScoringScheduledService {

    private final ApplicationService applicationService;
    private final EmailSender emailSender;
    private final ScoringClientService scoringClientService;
    private final ClientService clientService;

    @Scheduled(fixedDelayString = "${fixedDelay.in.milliseconds}")
    public void sendAnyNewApplicationToScoring() {
        log.info("Looking for new applications...");
        ApplicationDto application = applicationService.setToAnyNewApplicationScoringStatus();

        if (application == null) {
            return;
        }

        final ClientDto client = clientService.getClientById(application.clientId());

        int score = scoringClientService.score(
                application.requestedDisbursementAmount(),
                application.clientId(),
                client.salary());

        emailSender.sendEmail(client.email(), Integer.toString(score));
    }
}
