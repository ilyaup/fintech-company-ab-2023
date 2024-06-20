package com.academy.fintech.origination.public_interface.application.dto;

import com.academy.fintech.origination.public_interface.application.ApplicationStatus;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ApplicationDto(
        String clientId,
        BigDecimal requestedDisbursementAmount,
        ApplicationStatus applicationStatus
) {
}
