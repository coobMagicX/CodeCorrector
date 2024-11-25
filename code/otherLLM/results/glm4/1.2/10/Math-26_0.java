import java.math.BigDecimal;
import java.math.MathContext;

private Fraction(double value, double epsilon, int maxDenominator, int maxIterations)
throws FractionConversionException {
    // Use BigDecimal to handle large numbers without overflow
    BigDecimal r0 = new BigDecimal(Double.toString(value));
    BigDecimal a0 = r0.setScale(0, BigDecimal.ROUND_DOWN).toBigIntegerExact();
    
    if (a0.compareTo(BigDecimal.valueOf(Integer.MAX_VALUE)) > 0) {
        throw new FractionConversionException(value, a0, BigDecimal.ONE);
    }

    // check for (almost) integer arguments, which should not go
    // to iterations.
    if (r0.subtract(a0).abs().compareTo(new BigDecimal(epsilon)) < 0) {
        this.numerator = a0.intValue();
        this.denominator = 1;
        return;
    }

    BigDecimal p0 = BigDecimal.ONE;
    BigDecimal q0 = BigDecimal.ZERO;
    BigDecimal p1 = a0;
    BigDecimal q1 = BigDecimal.ONE;

    BigDecimal p2 = BigDecimal.ZERO;
    BigDecimal q2 = BigDecimal.ONE;

    int n = 0;
    boolean stop = false;
    do {
        ++n;
        BigDecimal r1 = BigDecimal.ONE.divide(r0.subtract(a0));
        BigDecimal a1 = r1.setScale(0, BigDecimal.ROUND_DOWN).toBigIntegerExact();
        p2 = a1.multiply(p1).add(p0);
        q2 = a1.multiply(q1).add(q0);
        
        if (p2.compareTo(BigDecimal.valueOf(Integer.MAX_VALUE)) > 0 || q2.compareTo(BigDecimal.valueOf(Integer.MAX_VALUE)) > 0) {
            throw new FractionConversionException(value, p2, q2);
        }

        BigDecimal convergent = p2.divide(q2, new MathContext(maxIterations));
        if (n < maxIterations && r1.subtract(convergent).abs().compareTo(new BigDecimal(epsilon)) > 0 && q2.compareTo(BigDecimal.valueOf(maxDenominator)) < 0) {
            p0 = p1;
            p1 = p2;
            q0 = q1;
            q1 = q2;
            a0 = a1;
            r0 = r1;
        } else {
            stop = true;
        }
    } while (!stop);

    if (n >= maxIterations) {
        throw new FractionConversionException(value, n);
    }

    if (q2.compareTo(BigDecimal.valueOf(maxDenominator)) < 0) {
        this.numerator = p2.intValue();
        this.denominator = q2.intValue();
    } else {
        this.numerator = p1.intValue();
        this.denominator = q1.intValue();
    }
}

// Note: BigDecimal is used to handle large numbers and avoid overflow.
// The conversion from BigDecimal to int is safe as it compares against the maximum value of an int, which should not be exceeded by the values being stored.