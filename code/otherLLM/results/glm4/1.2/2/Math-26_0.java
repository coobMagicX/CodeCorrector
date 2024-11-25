private Fraction(double value, double epsilon, int maxDenominator, int maxIterations)
throws FractionConversionException {
    long overflow = Long.MAX_VALUE / 2; // To prevent overflow when casting to long
    double r0 = value;
    long a0 = (long) FastMath.floor(r0);
    
    if (a0 > overflow || a0 < -overflow) { // Check for positive and negative overflows
        throw new FractionConversionException(value, a0, 1L);
    }

    if (FastMath.abs(a0 - value) < epsilon) {
        this.numerator = (int) a0;
        this.denominator = 1;
        return;
    }

    long p0 = 1;
    long q0 = 0;
    long p1 = a0;
    long q1 = 1;

    int n = 0;
    boolean stop = false;
    do {
        ++n;
        double r1 = 1.0 / (r0 - a0);
        long a1 = (long) FastMath.floor(r1);

        // Check for potential overflow before casting
        if (a1 > overflow || a1 < -overflow) {
            throw new FractionConversionException(value, a1, 1L);
        }

        p2 = (a1 * p1) + p0;
        q2 = (a1 * q1) + q0;

        // Check for potential overflow before casting
        if (p2 > overflow || q2 > overflow || p2 < -overflow || q2 < -overflow) {
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

    // Normalize the fraction to prevent overflow when casting
    long gcd = GcdUtil.gcd((int) Math.abs(p2), (int) Math.abs(q2));
    this.numerator = (int) ((p2 / gcd) % Integer.MAX_VALUE);
    this.denominator = (int) ((q2 / gcd) % Integer.MAX_VALUE);

    if (this.denominator == 0) {
        throw new FractionConversionException(value, -1L); // Use a constant or an appropriate value
    }
}