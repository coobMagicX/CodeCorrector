public double evaluate(double x, double epsilon, int maxIterations) {
    final double small = 1e-50;
    double hPrev = getA(0, x);

    // use the value of small as epsilon criteria for zero checks
    if (Precision.equals(hPrev, 0.0, small)) {
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

        // Ensure that the denominator does not become too small or too large to avoid overflow/underflow
        if (FastMath.abs(a) > 1e50 || FastMath.abs(b) > 1e50) {
            throw new ConvergenceException(LocalizedFormats.CONTINUED_FRACTION_OVERFLOW, x);
        }

        double cN = a * hPrev + b * p0;
        double q2 = a * q1 + b * dPrev;

        if (Double.isInfinite(cN) || Double.isInfinite(q2)) {
            double scaleFactor = 1d;
            double lastScaleFactor = 1d;
            final int maxPower = 5;
            final double scale = FastMath.max(FastMath.abs(a), FastMath.abs(b));
            if (scale <= 0) {  // Can't scale
                throw new ConvergenceException(LocalizedFormats.CONTINUED_FRACTION_INFINITY_DIVERGENCE, x);
            }
            for (int i = 0; i < maxPower; i++) {
                lastScaleFactor = scaleFactor;
                scaleFactor *= scale;
                cN = hPrev / lastScaleFactor + (b / scaleFactor * p0);
                q2 = q1 / lastScaleFactor + (b / scaleFactor * dPrev);
                if (!(Double.isInfinite(cN) || Double.isInfinite(q2))) {
                    break;
                }
            }
        }

        final double deltaN = cN / q2 / cPrev;
        hN = cPrev * deltaN;

        // Check for infinity and NaN to throw appropriate exceptions
        if (Double.isInfinite(hN)) {
            throw new ConvergenceException(LocalizedFormats.CONTINUED_FRACTION_INFINITY_DIVERGENCE, x);
        }
        if (Double.isNaN(hN)) {
            throw new ConvergenceException(LocalizedFormats.CONTINUED_FRACTION_NAN_DIVERGENCE, x);
        }

        // Check for convergence
        if (FastMath.abs(deltaN - 1.0) < epsilon) {
            break;
        }

        dPrev = q1;
        cPrev = cN;
        p0 = hPrev;

        // Increment counter to check for maximum iterations
        n++;
    }

    if (n >= maxIterations) {
        throw new MaxCountExceededException(LocalizedFormats.NON_CONVERGENT_CONTINUED_FRACTION, maxIterations, x);
    }

    return hN;
}