package com.academy.fintech.pe.grpc.agreement;

import com.academy.fintech.agreement_creation.AgreementCreationRequest;
import com.academy.fintech.agreement_creation.AgreementCreationResponse;
import com.academy.fintech.agreement_creation.AgreementCreationServiceGrpc;
import com.academy.fintech.pe.core.service.agreement.AgreementCreationService;
import io.grpc.stub.StreamObserver;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;

/**
 * gRPC controller that allows other bank`s services to create agreements.
 *
 * @author Ilya Upcher
 */
@Slf4j
@GRpcService
public class AgreementCreationController extends AgreementCreationServiceGrpc.AgreementCreationServiceImplBase {

    private final AgreementCreationService agreementCreationService;

    public AgreementCreationController(AgreementCreationService agreementCreationService) {
        this.agreementCreationService = agreementCreationService;
    }


    /**
     * gRPC create method, which creates agreement via
     * {@link AgreementCreationService#create(AgreementCreationRequest)}, passing on the request to it, and accepting
     * an id of the created agreement.
     *
     * @param request          on creating new agreement, which contains information about agreement
     * @param responseObserver is not filled if {@link AgreementCreationService#create(AgreementCreationRequest)}
     *                         return null, in other case contains the agreement id.
     */
    @Override
    public void create(AgreementCreationRequest request, StreamObserver<AgreementCreationResponse> responseObserver) {
        log.info("AgreementCreationController got request: {}", request);
        Integer agreementId = agreementCreationService.create(request);
        AgreementCreationResponse response;
        if (agreementId == null) {
            response = AgreementCreationResponse.newBuilder().build();
        } else {
            response = AgreementCreationResponse.newBuilder()
                    .setId(agreementId)
                    .build();
        }
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
