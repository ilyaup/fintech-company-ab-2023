package com.academy.fintech.origination.core.client.scoring.grpc;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "origination.client.scoring.grpc")
public record ScoringGrpcClientProperty(String host, int port) {
}
