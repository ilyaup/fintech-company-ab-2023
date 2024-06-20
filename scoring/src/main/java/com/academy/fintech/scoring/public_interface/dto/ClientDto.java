package com.academy.fintech.scoring.public_interface.dto;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record ClientDto(
        String id,
        String firstName,
        String lastName,
        String email,
        BigDecimal salary
) {
}
