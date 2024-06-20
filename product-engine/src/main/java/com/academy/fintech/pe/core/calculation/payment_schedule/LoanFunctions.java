package com.academy.fintech.pe.core.calculation.payment_schedule;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * This class consists of static functions that replicate EXCEL analogs, with the difference that functions of this
 * class do not accept whether is loan is overdue and expected leftover sum as arguments.
 *
 * @author Ilya Upcher
 */

public class LoanFunctions {

    private static final BigDecimal ONE = BigDecimal.ONE;

    private static BigDecimal notRoundedPmt(BigDecimal rate, int n, BigDecimal pv) {
        BigDecimal k = rate.multiply(ONE.add(rate).pow(n))
                .divide(ONE.add(rate).pow(n).subtract(ONE), 12, RoundingMode.HALF_UP);
        return k.multiply(pv).negate();
    }

    private static BigDecimal notRoundedIpmt(BigDecimal rate, int per, int n, BigDecimal pv) {
        BigDecimal c = notRoundedPmt(rate, n, pv);
        return c.multiply(ONE.add(rate).pow(per - 1).subtract(ONE)).divide(rate, 12, RoundingMode.HALF_UP)
                .add(pv.multiply(ONE.add(rate).pow(per - 1)))
                .multiply(rate)
                .negate();
    }

    private static BigDecimal notRoundedPpmt(BigDecimal rate, int per, int n, BigDecimal pv) {
        return notRoundedPmt(rate, n, pv).subtract(notRoundedIpmt(rate, per, n, pv));
    }

    /**
     * Analog of the PMT function in EXCEL.
     *
     * @param rate the interest rate of the loan.
     * @param n    the term.
     * @param pv   the principal amount of the loan.
     * @return the payment per payment period for the loan, rounded to scale 2 with {@link RoundingMode#HALF_UP}.
     */
    public static BigDecimal calculatePmt(BigDecimal rate, int n, BigDecimal pv) {
        return round(notRoundedPmt(rate, n, pv));
    }

    /**
     * Analog of the IPMT function in EXCEL.
     *
     * @param rate the interest rate of the loan.
     * @param per  the payment rate
     * @param n    the term.
     * @param pv   the principal amount of the loan.
     * @return the interest payment for given period number for the loan, rounded to scale 2 with
     * {@link RoundingMode#HALF_UP}.
     */

    public static BigDecimal calculateIpmt(BigDecimal rate, int per, int n, BigDecimal pv) {
        return round(notRoundedIpmt(rate, per, n, pv));
    }

    /**
     * Analog of the PPMT function in EXCEL.
     *
     * @param rate the interest rate of the loan.
     * @param per  the payment rate
     * @param n    the term.
     * @param pv   the principal amount of the loan.
     * @return the payment on the principal for given period number for the loan, rounded to scale 2 with
     * {@link RoundingMode#HALF_UP}.
     */
    public static BigDecimal calculatePpmt(BigDecimal rate, int per, int n, BigDecimal pv) {
        return round(notRoundedPpmt(rate, per, n, pv));
    }

    /**
     * Calculates monthly interest rate basing on yearly interest rate.
     *
     * @param yearRate yearly interest rate.
     * @return monthly interest rate.
     */
    public static BigDecimal monthRateFromYearRate(BigDecimal yearRate) {
        return yearRate.divide(new BigDecimal(12), 12, RoundingMode.HALF_UP);
    }

    private static BigDecimal round(BigDecimal bigDecimal) {
        return bigDecimal.setScale(2, RoundingMode.HALF_UP);
    }
}
