package com.academy.fintech.pe.public_interface.scheduled_payment.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record PaymentDto(
        int id,
        int paymentScheduleId,
        String status,
        LocalDate paymentDate,
        BigDecimal periodPayment,
        BigDecimal interestPayment,
        BigDecimal principalPayment,
        int periodNumber
) {
}
