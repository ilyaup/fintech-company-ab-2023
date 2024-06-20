package com.academy.fintech.scoring.core.client.pe.grpc;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "scoring.client.pe.grpc")
public record PeGrpcClientProperty(String host, int port) {
}
