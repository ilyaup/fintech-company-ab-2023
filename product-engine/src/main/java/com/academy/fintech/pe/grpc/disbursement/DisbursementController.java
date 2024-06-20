package com.academy.fintech.pe.grpc.disbursement;

import com.academy.fintech.disbursement.DisbursementRequest;
import com.academy.fintech.disbursement.DisbursementResponse;
import com.academy.fintech.disbursement.DisbursementServiceGrpc;
import com.academy.fintech.pe.core.service.disbursement.DisbursementService;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;

/**
 * gRPC controller that allows other bank`s services to activate the agreement, when the loan of this agreement has been
 * disbursed.
 */
@Slf4j
@GRpcService
@RequiredArgsConstructor
public class DisbursementController extends DisbursementServiceGrpc.DisbursementServiceImplBase {

    private final DisbursementService disbursementService;

    /**
     * gRPC activate method, which activates agreement via {@link DisbursementService#activate(DisbursementRequest)},
     * passing on the request to it.
     *
     * @param request          contains information for an agreement activation.
     * @param responseObserver is assigned by the returned {@link DisbursementResponse} from
     *                         {@link DisbursementService#activate(DisbursementRequest)}
     */
    @Override
    public void activate(DisbursementRequest request, StreamObserver<DisbursementResponse> responseObserver) {
        log.info("DisbursementController got request: {}", request);
        responseObserver.onNext(
                disbursementService.activate(request)
        );
        responseObserver.onCompleted();
    }
}
