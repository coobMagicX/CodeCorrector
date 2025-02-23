public double evaluate(double x, double epsilon, int maxIterations) {
    final double small = 1e-50;
    double hPrev = getA(0, x);

    // Use a small value as epsilon criteria for zero to avoid division by zero later on
    if (Math.abs(hPrev) < small) {
        hPrev = small;
    }

    int n = 1;
    double cPrev = hPrev;
    double dPrev = 0.0;
    double q1 = 1.0;
    double p0 = 1.0;
    double hN = hPrev;

    while (n < maxIterations) {
        final double a = getA(n, x);
        final double b = getB(n, x);
        
        double cN = a * hPrev + b * p0;
        double q2 = a * q1 + b * dPrev;

        // Check for divergence and scale down if needed
        if (Double.isInfinite(cN) || Double.isInfinite(q2)) {
            final double scaleFactor = 1e10;
            cN = (a * hPrev + (b / scaleFactor) * p0) / scaleFactor;
            q2 = (a * q1 + (b / scaleFactor) * dPrev) / scaleFactor;
        }

        if (q2 == 0.0) {
            q2 = small;
        }

        double deltaN = cN / q2;
        
        if (Double.isInfinite(deltaN) || Double.isNaN(deltaN)) {
            throw new ConvergenceException(LocalizedFormats.CONTINUED_FRACTION_INFINITY_DIVERGENCE, x);
        }

        hN = cPrev * deltaN;

        // Convergence check
        if (Math.abs(deltaN - 1.0) < epsilon) {
            break;
        }

        dPrev = q1;
        cPrev = hN;
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
