package com.academy.fintech.origination.grpc.application.v1;


import com.academy.fintech.application.ApplicationRequest;
import com.academy.fintech.application.ApplicationResponse;
import com.academy.fintech.application.ApplicationServiceGrpc;
import com.academy.fintech.application.CancelApplicationRequest;
import com.academy.fintech.application.CancelApplicationResponse;
import com.academy.fintech.origination.core.service.application.CancelApplicationService;
import com.academy.fintech.origination.core.service.application.CreateApplicationService;
import com.academy.fintech.origination.core.service.application.db.application.Application;
import com.academy.fintech.origination.core.service.application.exception.DuplicateApplicationRuntimeException;
import com.academy.fintech.origination.public_interface.application.ApplicationStatus;
import io.grpc.Metadata;
import io.grpc.Status;
import io.grpc.StatusException;
import io.grpc.stub.StreamObserver;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.lognet.springboot.grpc.GRpcService;

/**
 * gRPC service for creating and canceling {@link Application}s.
 */
@Slf4j
@GRpcService
@RequiredArgsConstructor
public class ApplicationController extends ApplicationServiceGrpc.ApplicationServiceImplBase {

    private final CreateApplicationService createApplicationService;
    private final ApplicationRequestMapper applicationRequestMapper;
    private final CancelApplicationService cancelApplicationService;

    /**
     * Method which creates application via {@link CreateApplicationService}. Also, it catches
     * {@link DuplicateApplicationRuntimeException} and throws {@link StatusException} with
     * {@link Status#ALREADY_EXISTS} and with id of already existing application in trailer. Passes any other throwable
     * on.
     *
     * @param request          contains information about application and client.
     * @param responseObserver accepts response or error.
     */
    @Override
    public void create(ApplicationRequest request, StreamObserver<ApplicationResponse> responseObserver) {
        log.info("Got an application request: {}", request);

        try {
            responseObserver.onNext(
                    ApplicationResponse.newBuilder()
                            .setApplicationId(createApplicationService
                                    .createApplication(applicationRequestMapper
                                            .mapRequestToDto(request)))
                            .build()
            );
            responseObserver.onCompleted();
        } catch (DuplicateApplicationRuntimeException e) {
            log.info("Duplicate on create: " + e.getMessage());
            Metadata metadata = new Metadata();
            metadata.put(Metadata.Key.of("application_id", Metadata.ASCII_STRING_MARSHALLER),
                    e.getApplicationId());
            responseObserver.onError(new StatusException(Status.ALREADY_EXISTS, metadata));
        } catch (Throwable e) {
            log.info("Throwable on create: " + e.getMessage() + " " + e.getCause());
            responseObserver.onError(e);
        }
    }

    /**
     * Cancels applications which has id that is in {@link CancelApplicationRequest}, if application exits and has
     * {@link ApplicationStatus#NEW}. Uses {@link CreateApplicationService} for canceling.
     *
     * @param request          contains application id.
     * @param responseObserver accepts response.
     */
    @Override
    public void cancel(CancelApplicationRequest request, StreamObserver<CancelApplicationResponse> responseObserver) {
        log.info("Got a cancellation request: {}", request);

        responseObserver.onNext(CancelApplicationResponse.newBuilder()
                .setCanceled(cancelApplicationService.cancelApplication(request.getApplicationId()))
                .build()
        );
        responseObserver.onCompleted();
    }
}
