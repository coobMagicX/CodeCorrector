public double evaluate(double x, double epsilon, int maxIterations) {
    final double small = 1e-50;
    double hPrev = getA(0, x);

    // Adjust hPrev if it is too close to zero
    if (Math.abs(hPrev) < small) {
        hPrev = small;
    }

    int n = 1;
    double dPrev = 1.0; // setting initial value to avoid division by zero
    double p0 = 1.0;
    double q1 = 1.0;
    double cPrev = hPrev;
    double hN = hPrev;

    while (n < maxIterations) {
        final double a = getA(n, x);
        final double b = getB(n, x);

        double cN = a * hPrev + b * p0;
        double q2 = a * q1 + b * dPrev;

        // Check for infinite or subnormal values that might arise
        if (Double.isInfinite(cN) || Double.isInfinite(q2) || Double.isNaN(cN) || Double.isNaN(q2)) {
            // Rescale to avoid Infinity or NaN
            cN = Double.isInfinite(cN) || Double.isNaN(cN) ? small : cN;
            q2 = Double.isInfinite(q2) || Double.isNaN(q2) ? small : q2;
        }

        double deltaN = cN / q2;
        hN = cPrev * deltaN;

        if (Double.isNaN(hN) || Double.isInfinite(hN)) {
            // If the series diverges or oscillates, fallback safe values and reattempt
            cN = small;
            q2 = small;
            deltaN = cN / q2;
            hN = cPrev * deltaN;
        }

        if (Math.abs(deltaN - 1.0) < epsilon) {  // Convergence achieved
            break;
        }

        // Update variables for next iteration
        dPrev = q1;
        cPrev = hN;
        p0 = hPrev;
        hPrev = cN;
        q1 = q2;
        n++;
    }

    if (n >= maxIterations) {
        throw new MaxCountExceededException(LocalizedFormats.NON_CONVERGENT_CONTINUED_FRACTION, maxIterations, x);
    }

    return hN;
}
