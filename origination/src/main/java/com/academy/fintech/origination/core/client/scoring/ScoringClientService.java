package com.academy.fintech.origination.core.client.scoring;

import com.academy.fintech.origination.core.client.scoring.grpc.ScoringGrpcClient;
import com.academy.fintech.scoring.ScoringRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class ScoringClientService {

    private final ScoringGrpcClient scoringGrpcClient;

    public int score(BigDecimal disbursementAmount, String clientId, BigDecimal salary) {
        return scoringGrpcClient.score(ScoringRequest.newBuilder()
                        .setDisbursementAmount(disbursementAmount.toString())
                        .setClientId(clientId)
                        .setSalary(salary.toString()).build())
                .getScore();
    }
}
