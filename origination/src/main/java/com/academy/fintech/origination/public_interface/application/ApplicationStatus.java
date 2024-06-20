package com.academy.fintech.origination.public_interface.application;

import jakarta.persistence.Table;

@Table(name = "application_status")
public enum ApplicationStatus {
    NEW, SCORING, ACCEPTED, ACTIVE, CLOSED, CANCELLED;
}
