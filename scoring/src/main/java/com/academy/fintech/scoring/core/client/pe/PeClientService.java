package com.academy.fintech.scoring.core.client.pe;

import com.academy.fintech.overdue_info_service.OverdueInfoRequest;
import com.academy.fintech.schedule_for_scoring.ScheduleForScoringCreationRequest;
import com.academy.fintech.scoring.core.client.pe.grpc.PeGrpcClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class PeClientService {

    private final PeGrpcClient peGrpcClient;

    public long getOverdueDays(String clientId) {
        return peGrpcClient.getOverdueInfo(OverdueInfoRequest.newBuilder()
                        .setClientId(clientId)
                        .build())
                .getOverdueDays();

    }

    public BigDecimal getPeriodPayment(BigDecimal disbursementAmount) {
        return new BigDecimal(peGrpcClient.createSchedule(ScheduleForScoringCreationRequest
                        .newBuilder()
                        .setDisbursementAmount(disbursementAmount.toString())
                        .build())
                .getPeriodPayment());
    }
}
