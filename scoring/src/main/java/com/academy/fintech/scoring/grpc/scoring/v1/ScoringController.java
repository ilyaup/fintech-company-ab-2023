package com.academy.fintech.scoring.grpc.scoring.v1;

import com.academy.fintech.scoring.ScoringRequest;
import com.academy.fintech.scoring.ScoringResponse;
import com.academy.fintech.scoring.ScoringServiceGrpc;
import com.academy.fintech.scoring.core.service.scoring.ScoringService;
import com.academy.fintech.scoring.public_interface.dto.ClientDto;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;

import java.math.BigDecimal;

@Slf4j
@GRpcService
@RequiredArgsConstructor
public class ScoringController extends ScoringServiceGrpc.ScoringServiceImplBase {

    private final ScoringService scoringService;

    @Override
    public void score(ScoringRequest request, StreamObserver<ScoringResponse> responseObserver) {
        log.info("Got an scoring request: " + request);

        responseObserver.onNext(ScoringResponse.newBuilder()
                .setScore(scoringService.score(new BigDecimal(request.getDisbursementAmount()), ClientDto.builder()
                        .id(request.getClientId())
                        .salary(new BigDecimal(request.getSalary()))
                        .build()))
                .build());
        responseObserver.onCompleted();
    }

}
