private Fraction(double value, double epsilon, int maxDenominator, int maxIterations)
throws FractionConversionException {
    long overflow = Long.MAX_VALUE; // Change to Long.MAX_VALUE for correct check on long overflow
    double r0 = value;
    long a0 = (long) FastMath.floor(r0);
    if (a0 > overflow || a0 < -overflow) { // Check both for positive and negative overflows
        throw new FractionConversionException(value, a0, 1L); // Pass the correct type of 'a0' as it is long
    }

    // check for (almost) integer arguments, which should not go to iterations.
    if (FastMath.abs(a0 - value) < epsilon) {
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
        long a1 = (long) FastMath.floor(r1);
        p2 = (a1 * p1) + p0;
        q2 = (a1 * q1) + q0;

        // Check for overflow in both numerator and denominator
        if ((p2 > overflow || p2 < -overflow) || (q2 > overflow || q2 < -overflow)) {
            throw new FractionConversionException(value, p2, q2);
        }

        double convergent = (double) p2 / (double) q2;
        if (n < maxIterations && FastMath.abs(convergent - value) > epsilon && q2 <= maxDenominator) {
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

    // Adjust for cases where long values exceed int range
    this.numerator = (int) p2;
    this.denominator = (int) q2;
}