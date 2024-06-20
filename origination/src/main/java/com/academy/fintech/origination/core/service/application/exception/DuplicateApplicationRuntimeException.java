package com.academy.fintech.origination.core.service.application.exception;

public class DuplicateApplicationRuntimeException extends RuntimeException {
    private final String applicationId;

    public DuplicateApplicationRuntimeException(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getApplicationId() {
        return applicationId;
    }
}
