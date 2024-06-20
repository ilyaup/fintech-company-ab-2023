package com.academy.fintech.scoring.configuration;

import com.academy.fintech.scoring.core.client.pe.grpc.PeGrpcClientProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({PeGrpcClientProperty.class})
public class ScoringConfiguration {
}
