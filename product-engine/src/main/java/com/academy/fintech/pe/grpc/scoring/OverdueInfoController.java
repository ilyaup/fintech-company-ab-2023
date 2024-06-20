package com.academy.fintech.pe.grpc.scoring;

import com.academy.fintech.overdue_info_service.OverdueInfoRequest;
import com.academy.fintech.overdue_info_service.OverdueInfoResponse;
import com.academy.fintech.overdue_info_service.OverdueInfoServiceGrpc;
import com.academy.fintech.pe.core.service.payment_schedule.GetClientsOverdueService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import org.lognet.springboot.grpc.GRpcService;

import java.time.LocalDate;

@GRpcService
@RequiredArgsConstructor
public class OverdueInfoController extends OverdueInfoServiceGrpc.OverdueInfoServiceImplBase {

    private final GetClientsOverdueService getClientsOverdueService;

    @Override
    public void get(OverdueInfoRequest request, StreamObserver<OverdueInfoResponse> responseObserver) {
        responseObserver.onNext(OverdueInfoResponse.newBuilder()
                .setOverdueDays(getClientsOverdueService
                        .getDaysOfMostOverduePaymentByClientId(request.getClientId(), LocalDate.now()))
                .build());
    }

}
