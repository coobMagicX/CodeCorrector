import java.math.BigInteger;

private Fraction(double value, double epsilon, int maxDenominator, int maxIterations)
throws FractionConversionException {
    BigInteger overflow = BigInteger.valueOf(Integer.MAX_VALUE);
    BigInteger r0 = BigInteger.valueOf(Double.doubleToLongBits(value));
    if (r0.compareTo(overflow) > 0) {
        throw new FractionConversionException(value, r0.longValue(), 1L);
    }

    // check for (almost) integer arguments, which should not go
    // to iterations.
    if (BigInteger.valueOf(Double.doubleToLongBits(Math.abs(r0.subtract(BigInteger.valueOf(value)))).longValueExact() < epsilon)) {
        this.numerator = r0.intValueExact();
        this.denominator = 1;
        return;
    }

    BigInteger p0 = BigInteger.ONE;
    BigInteger q0 = BigInteger.ZERO;
    BigInteger p1 = r0;
    BigInteger q1 = BigInteger.ONE;

    BigInteger p2 = BigInteger.ZERO;
    BigInteger q2 = BigInteger.ONE;

    int n = 0;
    boolean stop = false;
    do {
        ++n;
        BigInteger r1 = BigInteger.ONE.divide(r0.subtract(p1).multiply(BigInteger.ONE).add(p0));
        BigInteger a1 = BigInteger.valueOf(Double.doubleToLongBits(Math.floor(r1.doubleValue()))).longValueExact();
        p2 = a1.multiply(p1).add(p0);
        q2 = a1.multiply(q1).add(q0);
        if (p2.compareTo(overflow) > 0 || q2.compareTo(overflow) > 0) {
            throw new FractionConversionException(value, p2.longValue(), q2.longValue());
        }

        double convergent = p2.divide(q2).doubleValue();
        if (n < maxIterations && Math.abs(convergent - value) > epsilon && q2.intValue() < maxDenominator) {
            p0 = p1;
            p1 = p2;
            q0 = q1;
            q1 = q2;
            r0 = BigInteger.valueOf(Double.doubleToLongBits(r1.doubleValue()));
            a0 = BigInteger.valueOf(a1);
        } else {
            stop = true;
        }
    } while (!stop);

    if (n >= maxIterations) {
        throw new FractionConversionException(value, maxIterations);
    }

    this.numerator = p2.intValueExact();
    this.denominator = q2.intValueExact();
}