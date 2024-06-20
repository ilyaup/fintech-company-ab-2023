package com.academy.fintech.scoring.core.service.scoring;

import com.academy.fintech.scoring.core.client.pe.PeClientService;
import com.academy.fintech.scoring.public_interface.dto.ClientDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ScoringService {

    private static final int NO_PENALTY_DAYS = 0;
    private static final int PENALTY_DAYS_1 = 7;

    private static final BigDecimal SALARY_TO_PAYMENT_ALLOWED_RATIO = new BigDecimal("3");

    private final PeClientService peClientService;

    public int score(BigDecimal disbursementAmount, ClientDto client) {
        int score = 0;
        if (client.salary().compareTo(calculateRequiredSalary(disbursementAmount)) >= 0) {
            score++;
        }
        long overdueDays = peClientService.getOverdueDays(client.id());
        score += scoreOverdueDays(overdueDays);
        return score;
    }

    private BigDecimal calculateRequiredSalary(BigDecimal disbursementAmount) {
        return peClientService.getPeriodPayment(disbursementAmount)
                .multiply(SALARY_TO_PAYMENT_ALLOWED_RATIO);
    }

    private int scoreOverdueDays(long overdueDays) {
        if (overdueDays <= NO_PENALTY_DAYS) {
            return 1;
        } else if (overdueDays <= PENALTY_DAYS_1) {
            return 0;
        } else {
            return -1;
        }
    }

}
