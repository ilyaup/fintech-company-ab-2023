package com.academy.fintech.origination.grpc.application.v1;

import com.academy.fintech.application.ApplicationRequest;
import com.academy.fintech.origination.public_interface.application.dto.ApplicationRequestDto;
import org.springframework.stereotype.Component;

/**
 * Component to convert {@link ApplicationRequest} to {@link ApplicationRequestDto}.
 */

@Component
public class ApplicationRequestMapper {

    /**
     * Maps {@link ApplicationRequest} to {@link ApplicationRequestDto}.
     *
     * @param applicationRequest request to convert.
     * @return resulting DTO.
     */
    public ApplicationRequestDto mapRequestToDto(ApplicationRequest applicationRequest) {
        return ApplicationRequestDto.builder()
                .firstName(applicationRequest.getFirstName())
                .lastName(applicationRequest.getLastName())
                .email(applicationRequest.getEmail())
                .salary(applicationRequest.getSalary())
                .amount(applicationRequest.getDisbursementAmount())
                .build();
    }
}
