package com.academy.fintech.api.core.origination.client;

import com.academy.fintech.api.core.origination.client.grpc.OriginationGrpcClient;
import com.academy.fintech.api.public_interface.application.dto.ApplicationDto;
import com.academy.fintech.application.ApplicationRequest;
import com.academy.fintech.application.ApplicationResponse;
import io.grpc.Metadata;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * Service for working with {@link OriginationGrpcClient}.
 */
@Service
@RequiredArgsConstructor
public class OriginationClientService {

    private final OriginationGrpcClient originationGrpcClient;

    /**
     * If {@link Status#ALREADY_EXISTS}, then returns value of "application_id" key in trailers.
     */
    private final Map<Status, Function<StatusRuntimeException, String>> grpcStatusToReturnStringMapper
            = new HashMap<>();

    {
        grpcStatusToReturnStringMapper.put(Status.ALREADY_EXISTS, e -> {
            assert e.getTrailers() != null;
            return e.getTrailers().get(Metadata.Key.of("application_id", Metadata.ASCII_STRING_MARSHALLER));
        });
    }

    /**
     * Creates application via {@link OriginationGrpcClient#createApplication(ApplicationRequest)}. If created
     * successfully, then returns application's id. If
     * {@link OriginationGrpcClient#createApplication(ApplicationRequest)} threw {@link StatusRuntimeException},
     * then, returns String basing on {@see grpcStatusToReturnStringMapper}, if that map has required
     * {@link Status#ALREADY_EXISTS}, otherwise throws given {@link StatusRuntimeException}.
     *
     * @param applicationDto application to create.
     * @return id of created application or some other string from {@see grpcStatusToReturnStringMapper} if
     * {@link OriginationGrpcClient#createApplication(ApplicationRequest)} threw {@link StatusRuntimeException}.
     */
    public String createApplication(ApplicationDto applicationDto) {
        ApplicationRequest request = mapDtoToRequest(applicationDto);

        try {
            ApplicationResponse response = originationGrpcClient.createApplication(request);
            return response.getApplicationId();
        } catch (StatusRuntimeException e) {
            if (grpcStatusToReturnStringMapper.containsKey(e.getStatus())) {
                return grpcStatusToReturnStringMapper.get(e.getStatus()).apply(e);
            } else {
                throw e;
            }
        }
    }


    private static ApplicationRequest mapDtoToRequest(ApplicationDto applicationDto) {
        return ApplicationRequest.newBuilder()
                .setFirstName(applicationDto.firstName())
                .setLastName(applicationDto.lastName())
                .setEmail(applicationDto.email())
                .setSalary(applicationDto.salary())
                .setDisbursementAmount(applicationDto.amount())
                .build();
    }

}
