package com.academy.fintech.pe.grpc.scoring;

import com.academy.fintech.pe.core.service.scoring.ScheduleForScoringService;
import com.academy.fintech.schedule_for_scoring.ScheduleForScoringCreationRequest;
import com.academy.fintech.schedule_for_scoring.ScheduleForScoringCreationResponse;
import com.academy.fintech.schedule_for_scoring.ScheduleForScoringServiceGrpc;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.lognet.springboot.grpc.GRpcService;

import java.math.BigDecimal;

@GRpcService
@RequiredArgsConstructor
public class ScheduleForScoringController extends ScheduleForScoringServiceGrpc.ScheduleForScoringServiceImplBase {
    private final ScheduleForScoringService scheduleForScoringService;

    @Override
    public void create(ScheduleForScoringCreationRequest request,
                       StreamObserver<ScheduleForScoringCreationResponse> responseObserver) {
        responseObserver.onNext(ScheduleForScoringCreationResponse.newBuilder()
                .setPeriodPayment(scheduleForScoringService
                        .calculatePeriodPayment(new BigDecimal(request.getDisbursementAmount()))
                        .toString())
                .build());
        responseObserver.onCompleted();
    }

}
