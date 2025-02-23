private Fraction(double value, double epsilon, int maxDenominator, int maxIterations)
    throws FractionConversionException {
    long overflow = Integer.MAX_VALUE;
    double r0 = value;
    long a0 = (long) Math.floor(r0);
    
    if (Math.abs(a0) > overflow) {
        throw new FractionConversionException(value, a0, 1L);
    }

    if (Math.abs(a0 - value) < epsilon) {
        this.numerator = (int) a0;
        this.denominator = 1;
        return;
    }

    long p0 = 1;
    long q0 = 0;
    long p1 = a0;
    long q1 = 1;

    long p2 = 0;
    long q2 = 1;

    int n = 0;
    boolean stop = false;
    do {
        ++n;
        double r1 = 1.0 / (r0 - a0);
        long a1 = (long) Math.floor(r1);
        
        if (checkMultiplicationOverflow(a1, p1) || checkMultiplicationOverflow(a1, q1)) {
            throw new FractionConversionException(value, a1 * p1, a1 * q1);
        }

        p2 = a1 * p1 + p0;
        q2 = a1 * q1 + q0;

        
        if (checkAdditionOverflow(a1 * p1, p0) || checkAdditionOverflow(a1 * q1, q0)) {
            throw new FractionConversionException(value, p2, q2);
        }

        if (Math.abs(p2) > overflow || Math.abs(q2) > overflow) {
            throw new FractionConversionException(value, p2, q2);
        }

        double convergent = (double)p2 / (double)q2;
        if (n < maxIterations && Math.abs(convergent - value) > epsilon && q2 < maxDenominator) {
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
        throw new FractionConversionException(value, maxIterations);
    }

    if (q2 < maxDenominator) {
        this.numerator = (int) p2;
        this.denominator = (int) q2;
    } else {
        this.numerator = (int) p1;
        this.denominator = (int) q1;
    }

}

private static boolean checkMultiplicationOverflow(long x, long y) {
    if (x > 0 && y > 0 && Long.MAX_VALUE / x < y) {
        return true;
    }
    if (x < 0 && y < 0 && Long.MAX_VALUE / x > y) {
        return true;
    }
    if (x > 0 && y < 0 && Long.MIN_VALUE / x > y) {
        return true