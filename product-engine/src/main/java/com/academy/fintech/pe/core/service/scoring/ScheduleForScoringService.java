package com.academy.fintech.pe.core.service.scoring;

import com.academy.fintech.pe.core.calculation.payment_schedule.LoanFunctions;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class ScheduleForScoringService {

    public BigDecimal calculatePeriodPayment(BigDecimal disbursementAmount) {
        return LoanFunctions.calculatePmt(
                LoanFunctions.monthRateFromYearRate(new BigDecimal("0.12")),
                12,
                disbursementAmount);
    }
}
