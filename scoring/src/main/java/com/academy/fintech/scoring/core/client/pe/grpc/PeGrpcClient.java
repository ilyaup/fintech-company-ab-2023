package com.academy.fintech.scoring.core.client.pe.grpc;

import com.academy.fintech.overdue_info_service.OverdueInfoRequest;
import com.academy.fintech.overdue_info_service.OverdueInfoResponse;
import com.academy.fintech.overdue_info_service.OverdueInfoServiceGrpc;
import com.academy.fintech.overdue_info_service.OverdueInfoServiceGrpc.OverdueInfoServiceBlockingStub;
import com.academy.fintech.schedule_for_scoring.ScheduleForScoringCreationRequest;
import com.academy.fintech.schedule_for_scoring.ScheduleForScoringCreationResponse;
import com.academy.fintech.schedule_for_scoring.ScheduleForScoringServiceGrpc;
import com.academy.fintech.schedule_for_scoring.ScheduleForScoringServiceGrpc.ScheduleForScoringServiceBlockingStub;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import org.springframework.stereotype.Component;

@Component
public class PeGrpcClient {

    private final ScheduleForScoringServiceBlockingStub scheduleStub;
    private final OverdueInfoServiceBlockingStub overdueInfoStub;

    public PeGrpcClient(PeGrpcClientProperty property) {
        Channel channel = ManagedChannelBuilder.forAddress(property.host(), property.port()).usePlaintext().build();
        this.scheduleStub = ScheduleForScoringServiceGrpc.newBlockingStub(channel);
        this.overdueInfoStub = OverdueInfoServiceGrpc.newBlockingStub(channel);
    }

    public ScheduleForScoringCreationResponse createSchedule(ScheduleForScoringCreationRequest request) {
        return scheduleStub.create(request);
    }

    public OverdueInfoResponse getOverdueInfo(OverdueInfoRequest request) {
        return overdueInfoStub.get(request);
    }
}
