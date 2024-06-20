package unit.calculation;

import com.academy.fintech.pe.core.calculation.payment_schedule.LoanFunctions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

//Expected values are gotten from EXCEL
public class LoanFunctionsTest {

    public static final BigDecimal rate = LoanFunctions.monthRateFromYearRate(new BigDecimal("0.08"));

    public static final int n = 12;

    public static final BigDecimal pv = new BigDecimal(500_000);

    @Test
    void testConvertingRate_1() {
        assertEquals(new BigDecimal("0.006666666667"),
                LoanFunctions.monthRateFromYearRate(new BigDecimal("0.08")));
    }

    @Test
    void test_pmt_1() {
        System.out.println(LoanFunctions.calculatePmt(rate, n, pv));
        assertEquals(new BigDecimal("-43494.21"), LoanFunctions.calculatePmt(rate, n, pv));
    }

    @Test
    void test_ipmt_1() {
        assertEquals(new BigDecimal("-3333.33"), LoanFunctions.calculateIpmt(rate, 1, n, pv));
    }

    @Test
    void test_ipmt_2() {
        assertEquals(new BigDecimal("-3065.59"), LoanFunctions.calculateIpmt(rate, 2, n, pv));
    }

    @Test
    void test_ppmt_1() {
        assertEquals(new BigDecimal("-40969.47"), LoanFunctions.calculatePpmt(rate, 4, n, pv));
    }
}
