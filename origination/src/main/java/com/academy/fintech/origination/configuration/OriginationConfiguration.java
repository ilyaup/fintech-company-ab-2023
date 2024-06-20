package com.academy.fintech.origination.configuration;

import com.academy.fintech.origination.core.client.scoring.grpc.ScoringGrpcClientProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({ScoringGrpcClientProperty.class})
public class OriginationConfiguration {
}
