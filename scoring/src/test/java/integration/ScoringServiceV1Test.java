package integration;

import com.academy.fintech.overdue_info_service.OverdueInfoResponse;
import com.academy.fintech.schedule_for_scoring.ScheduleForScoringCreationResponse;
import com.academy.fintech.scoring.Application;
import com.academy.fintech.scoring.ScoringRequest;
import com.academy.fintech.scoring.ScoringServiceGrpc;
import com.academy.fintech.scoring.ScoringServiceGrpc.ScoringServiceBlockingStub;
import com.academy.fintech.scoring.core.client.pe.grpc.PeGrpcClient;
import io.grpc.Channel;
import io.grpc.ManagedChannelBuilder;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, classes = Application.class)
public class ScoringServiceV1Test {

    @MockBean
    private PeGrpcClient peGrpcClient;

    static private ScoringServiceBlockingStub stub;

    @BeforeAll
    static void setup() {
        Channel channel = ManagedChannelBuilder.forAddress("localhost", 9096).usePlaintext().build();
        stub = ScoringServiceGrpc.newBlockingStub(channel);
    }

    @Test
    void contextLoads() {

    }

    @Test
    void test1_score2() {
        when(peGrpcClient.createSchedule(any())).thenReturn(ScheduleForScoringCreationResponse.newBuilder()
                .setPeriodPayment("30")
                .build());
        when(peGrpcClient.getOverdueInfo(any())).thenReturn(OverdueInfoResponse.newBuilder()
                .setOverdueDays(0)
                .build());

        int actualScore = stub.score(ScoringRequest.newBuilder()
                        .setClientId("client_1")
                        .setDisbursementAmount("1000")
                        .setSalary("90")
                        .build())
                .getScore();

        assertEquals(2, actualScore);
    }

    @Test
    void test2_lowSalary_score1() {
        when(peGrpcClient.createSchedule(any())).thenReturn(ScheduleForScoringCreationResponse.newBuilder()
                .setPeriodPayment("30")
                .build());
        when(peGrpcClient.getOverdueInfo(any())).thenReturn(OverdueInfoResponse.newBuilder()
                .setOverdueDays(0)
                .build());

        int actualScore = stub.score(ScoringRequest.newBuilder()
                        .setClientId("client_1")
                        .setDisbursementAmount("1000")
                        .setSalary("30")
                        .build())
                .getScore();

        assertEquals(1, actualScore);
    }
}
