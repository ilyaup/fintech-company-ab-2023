package com.academy.fintech.origination.core.client.scoring.grpc;

import com.academy.fintech.scoring.ScoringRequest;
import com.academy.fintech.scoring.ScoringResponse;
import com.academy.fintech.scoring.ScoringServiceGrpc;
import com.academy.fintech.scoring.ScoringServiceGrpc.ScoringServiceBlockingStub;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.stereotype.Component;

@Component
public class ScoringGrpcClient {

    private final ScoringServiceBlockingStub stub;

    public ScoringGrpcClient(ScoringGrpcClientProperty property) {
        Channel channel = ManagedChannelBuilder.forAddress(property.host(), property.port()).usePlaintext().build();
        this.stub = ScoringServiceGrpc.newBlockingStub(channel);
    }

    public ScoringResponse score(ScoringRequest request) {
        return stub.score(request);
    }
}
