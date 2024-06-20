package com.academy.fintech.origination.public_interface.application.dto;

import lombok.Builder;

@Builder
public record ApplicationRequestDto(
        String firstName,
        String lastName,
        String email,
        int salary,
        int amount
) {
}
