public double evaluate(double x, double epsilon, int maxIterations) {
    final double small = 1e-50;
    double hPrev = getA(0, x);

    // Adjust hPrev if it is too close to zero
    if (Math.abs(hPrev) < small) {
        hPrev = small;
    }

    int n = 1;
    double dPrev = 1.0; // making sure initial value doesn't contribute zero influence
    double p0 = 1.0;
    double q1 = 1.0;
    double cPrev = hPrev;
    double hN = hPrev;

    while (n < maxIterations) {
        final double a = getA(n, x);
        final double b = getB(n, x);

        double cN = a * hPrev + b * p0;
        double q2 = a * q1 + b * dPrev;

        // Early check for infinities or NaNs
        if (Double.isInfinite(cN) || Double.isInfinite(q2) || Double.isNaN(cN) || Double.isNaN(q2)) {
            throw new ConvergenceException(LocalizedFormats.CONTINUED_FRACTION_INFINITY_DIVERGENCE, x);
        }

        final double deltaN = cN / q2;

        // Update convergence value, hN
        hN = cPrev * deltaN;

        if (Double.isInfinite(hN) || Double.isNaN(hN)) {
            throw new ConvergenceException(LocalizedFormats.CONTINUED_FRACTION_INFINITY_DIVERGENCE, x);
        }

        // Check for convergence within tolerance epsilon
        if (Math.abs(deltaN - 1.0) < epsilon) {
            break;
        }

        // Update values for next iteration
        dPrev = q1;
        cPrev = cN / q2;
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
