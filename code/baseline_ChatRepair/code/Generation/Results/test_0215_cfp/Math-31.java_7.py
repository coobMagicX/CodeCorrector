public double evaluate(double x, double epsilon, int maxIterations) {
    double small = 1e-50; // a small threshold value
    double hPrev = getA(0, x);

    // Initial value edge cases handling
    if (Math.abs(hPrev) <= small) {
        hPrev = small;
    }

    int n = 1;  // keep track of iteration steps
    double dPrev = 1.0;
    double p0 = 1.0;
    double q1 = 1.0;
    double cPrev = hPrev;
    double hN = hPrev;

    while (n < maxIterations) {
        final double a = getA(n, x);
        final double b = getB(n, x);

        double cN = a * hPrev + b * p0;
        double q2 = a * q1 + b * dPrev;

        // Scaling to avoid overflow and ensure numerical stability
        if (Double.isInfinite(cN) || Double.isInfinite(q2)) {
            double scalingFactor = Math.max(Math.abs(a), Math.abs(b));
            if (scalingFactor > small) { // Avoid division by very small values
                cN /= scalingFactor;
                q2 /= scalingFactor;
            }
        }

        if (Double.isInfinite(cN) || Double.isInfinite(q2) || cN == 0 || q2 == 0) {
            throw new ConvergenceException(LocalizedFormats.CONTINUED_FRACTION_INFINITY_DIVERGENCE, x);
        }

        double deltaN = cN / q2;
        hN = hPrev * deltaN;

        if (Math.abs(deltaN - 1.0) < epsilon) {
            break;
        }

        dPrev = q1;
        cPrev = cN;
        p0 = hPrev;
        hPrev = cN;
        q1 = q2;
        n++;
    }

    if (n >= maxIterations) {
        throw new MaxCountExceededException(LocalizedFormats.NON_CONVERGENT_CONTINUED_FRACTION,
                                            maxIterations, x);
    }

    return hN;
}
