package com.academy.fintech.origination.public_interface.client.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ClientDto(
        String firstName,
        String lastName,
        String email,
        BigDecimal salary
) {
}
