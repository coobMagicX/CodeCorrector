public double evaluate(double x, double epsilon, int maxIterations) {
    final double small = 1e-50;
    double hPrev = getA(0, x);

    // Adjust initial value if too small to avoid division by zero
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
        
        // Prevent overflow and underflow by scaling
        if (Double.isInfinite(cN) || Double.isInfinite(q2)) {
            double scaleFactor = Math.max(Math.abs(a), Math.abs(b));
            if (scaleFactor == 0) { // Can't scale
                throw new ConvergenceException(LocalizedFormats.CONTINUED_FRACTION_INFINITY_DIVERGENCE, x);
            }
            cN /= scaleFactor;
            q2 /= scaleFactor;
        }

        double deltaN = cN / q2;

        if (Double.isInfinite(deltaN)) {
            throw new ConvergenceException(LocalizedFormats.CONTINUED_FRACTION_INFINITY_DIVERGENCE, x);
        }

        hN = cPrev * deltaN;

        if (Double.isNaN(hN)) {
            throw new ConvergenceException(LocalizedFormats.CONTINUED_FRACTION_NAN_DIVERGENCE, x);
        }

        // Check for convergence
        if (Math.abs(deltaN - 1.0) < epsilon) {
            break;
        }

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