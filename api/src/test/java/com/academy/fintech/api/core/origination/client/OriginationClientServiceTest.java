package com.academy.fintech.api.core.origination.client;

import com.academy.fintech.api.core.origination.client.grpc.OriginationGrpcClient;
import com.academy.fintech.api.public_interface.application.dto.ApplicationDto;
import com.academy.fintech.application.ApplicationRequest;
import com.academy.fintech.application.ApplicationResponse;
import io.grpc.Metadata;
import io.grpc.Status;
import io.grpc.StatusRuntimeException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OriginationClientServiceTest {

    @Mock
    private OriginationGrpcClient originationGrpcClient;

    @InjectMocks
    private OriginationClientService originationClientService;


    @Test
    void createApplication_noStatusRuntimeException_returnApplicationId() {
        String applicationId = "application_1";
        ApplicationDto applicationDto = ApplicationDto.builder()
                .firstName("Ivan")
                .lastName("Ivanov")
                .email("ivan@yandex.ru")
                .salary(30_000)
                .amount(90_000)
                .build();
        ApplicationRequest applicationRequest = ApplicationRequest.newBuilder()
                .setFirstName(applicationDto.firstName())
                .setLastName(applicationDto.lastName())
                .setEmail(applicationDto.email())
                .setSalary(applicationDto.salary())
                .setDisbursementAmount(applicationDto.amount())
                .build();

        when(originationGrpcClient.createApplication(applicationRequest)).thenReturn(ApplicationResponse.newBuilder()
                .setApplicationId(applicationId)
                .build());

        assertEquals(applicationId, originationClientService.createApplication(applicationDto));
    }

    @Test
    void createApplication_StatusRuntimeException_returnApplicationId() {
        String applicationId = "application_1";
        ApplicationDto applicationDto = ApplicationDto.builder()
                .firstName("Ivan")
                .lastName("Ivanov")
                .email("ivan@yandex.ru")
                .salary(30_000)
                .amount(90_000)
                .build();
        ApplicationRequest applicationRequest = ApplicationRequest.newBuilder()
                .setFirstName(applicationDto.firstName())
                .setLastName(applicationDto.lastName())
                .setEmail(applicationDto.email())
                .setSalary(applicationDto.salary())
                .setDisbursementAmount(applicationDto.amount())
                .build();
        Metadata metadata = new Metadata();
        metadata.put(Metadata.Key.of("application_id", Metadata.ASCII_STRING_MARSHALLER), applicationId);
        StatusRuntimeException sre = new StatusRuntimeException(Status.ALREADY_EXISTS, metadata);

        when(originationGrpcClient.createApplication(applicationRequest)).thenThrow(sre);

        assertEquals(applicationId, originationClientService.createApplication(applicationDto));
    }
}