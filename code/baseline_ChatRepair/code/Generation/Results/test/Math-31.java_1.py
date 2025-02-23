public double evaluate(double x, double epsilon, int maxIterations) {
    final double small = 1e-50;
    double hPrev = getA(0, x);

    // Using a small value to manipulate zero handling.
    if (Math.abs(hPrev) < small) {
        hPrev = small;
    }

    int n = 1;
    double dPrev = 0.0;
    double p0 = 1.0;
    double q1 = 1.0;
    double cPrev = hPrev;
    double hN = hPrev;

    while (n < maxIterations) {
        final double a = getA(n, x);
        final double b = getB(n, x);

        double cN = a * hPrev + b * p0;
        double q2 = a * q1 + b * dPrev;

        // Reduce the risk of overflow or underflow during computation.
        if (Double.isInfinite(cN) || Double.isInfinite(q2)) {
            // Scale down if infinities encountered
            cN *= small;
            q2 *= small;
        }
        
        // Replace with small constant if approaching zero to avoid division by zero
        q2 = (Math.abs(q2) < small) ? small : q2;

        double deltaN = cN / q2;
        hN = hPrev * deltaN;

        if (Double.isInfinite(hN) || Double.isNaN(hN)) {
            throw new ConvergenceException(LocalizedFormats.CONTINUED_FRACTION_INFINITY_DIVERGENCE, x);
        }

        if (Math.abs(deltaN - 1.0) < epsilon) {
            break;
        }

        dPrev = q1;
        cPrev = cN / q2;
        p0 = hPrev;
        hPrev = hN;
        q1 = q2;

        n++;
    }

    if (n >= maxIterations) {
        throw new MaxCountExceededException(LocalizedFormats.NON_CONVERGENT_CONTINUED_FRACTION, maxIterations, x);
    }

    return hN;
}
