package com.academy.fintech.scoring.core.serivce.scoring;

import com.academy.fintech.scoring.core.client.pe.PeClientService;
import com.academy.fintech.scoring.core.service.scoring.ScoringService;
import com.academy.fintech.scoring.public_interface.dto.ClientDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ScoringServiceTest {
    @InjectMocks
    private ScoringService scoringService;
    @Mock
    private PeClientService peClientService;

    private static ClientDto client;
    private static BigDecimal disbursementAmount;

    @BeforeAll
    static void setup() {
        client = ClientDto.builder()
                .id("client_1")
                .firstName("first")
                .lastName("second")
                .email("no@no.no")
                .salary(new BigDecimal("90"))
                .build();
        disbursementAmount = new BigDecimal("1000");
    }

    @Test
    void score_2_test0() {
        when(peClientService.getOverdueDays(anyString())).thenReturn(0L);
        when(peClientService.getPeriodPayment(disbursementAmount)).thenReturn(new BigDecimal("30"));

        assertEquals(2, scoringService.score(disbursementAmount, client));
    }

    @Test
    void score_2_test1() {
        when(peClientService.getOverdueDays(anyString())).thenReturn(0L);
        when(peClientService.getPeriodPayment(disbursementAmount)).thenReturn(new BigDecimal("20"));

        assertEquals(2, scoringService.score(disbursementAmount, client));
    }

    @Test
    void score_bigPeriodPayment_1_test0() {

        when(peClientService.getOverdueDays(anyString())).thenReturn(0L);
        when(peClientService.getPeriodPayment(disbursementAmount)).thenReturn(new BigDecimal("31"));

        assertEquals(1, scoringService.score(disbursementAmount, client));
    }

    @Test
    void score_overdue6_1_test0() {

        when(peClientService.getOverdueDays(anyString())).thenReturn(6L);
        when(peClientService.getPeriodPayment(disbursementAmount)).thenReturn(new BigDecimal("30"));

        assertEquals(1, scoringService.score(disbursementAmount, client));
    }

    @Test
    void score_overdue7_1_test0() {
        when(peClientService.getOverdueDays(anyString())).thenReturn(7L);
        when(peClientService.getPeriodPayment(disbursementAmount)).thenReturn(new BigDecimal("30"));

        assertEquals(1, scoringService.score(disbursementAmount, client));
    }

    @Test
    void score_overdue8_0_test0() {
        when(peClientService.getOverdueDays(anyString())).thenReturn(8L);
        when(peClientService.getPeriodPayment(disbursementAmount)).thenReturn(new BigDecimal("20"));

        assertEquals(0, scoringService.score(disbursementAmount, client));
    }

    @Test
    void score_overdue8_0_test1() {
        when(peClientService.getOverdueDays(anyString())).thenReturn(8L);
        when(peClientService.getPeriodPayment(disbursementAmount)).thenReturn(new BigDecimal("30"));

        assertEquals(0, scoringService.score(disbursementAmount, client));
    }

    @Test
    void score_bigPaymentAndOverdue14_minus1_test0() {
        when(peClientService.getOverdueDays(anyString())).thenReturn(14L);
        when(peClientService.getPeriodPayment(disbursementAmount)).thenReturn(new BigDecimal("4000"));

        assertEquals(-1, scoringService.score(disbursementAmount, client));
    }
}
