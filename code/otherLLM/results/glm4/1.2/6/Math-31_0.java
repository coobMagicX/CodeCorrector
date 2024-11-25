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

        // Check for potential overflow or loss of precision
        if ((a > 0 && b > 0) && (Math.log(a + b) > Double.MAX_LONG_BITS * Math.log(2))) {
            throw new ConvergenceException(LocalizedFormats.CONTINUED_FRACTION_OVERFLOW, x);
        }

        double cN = a * hPrev + b * p0;
        double q2 = a * q1 + b * dPrev;

        if (Double.isInfinite(cN) || Double.isInfinite(q2)) {
            // Attempt to scale down values that cause overflow or precision loss
            double scaleFactor = 1d;
            double lastScaleFactor = 1d;
            final int maxPower = 5;
            final double scale = FastMath.max(a, b);
            if (scale <= 0) {  // Can't scale
                throw new ConvergenceException(LocalizedFormats.CONTINUED_FRACTION_INFINITY_DIVERGENCE, x);
            }
            for (int i = 0; i < maxPower; i++) {
                lastScaleFactor = scaleFactor;
                scaleFactor *= scale;
                if (a != 0 && a > b) {
                    cN /= lastScaleFactor;
                    q2 /= lastScaleFactor;
                } else if (b != 0) {
                    cN = (cPrev * a / lastScaleFactor) + (p0 / lastScaleFactor);
                    q2 = ((cN * a) / lastScaleFactor) + (dPrev / lastScaleFactor);
                }
                if (!Double.isInfinite(cN) && !Double.isInfinite(q2)) {
                    break;
                }
            }
        }

        final double deltaN = cN / q2 / cPrev;
        hN = cPrev * deltaN;

        if (Double.isInfinite(hN) || Double.isNaN(hN)) {
            throw new ConvergenceException(LocalizedFormats.CONTINUED_FRACTION_INFINITY_DIVERGENCE, x);
        }

        if (FastMath.abs(deltaN - 1.0) < epsilon) {
            break;
        }

        dPrev = q1;
        cPrev = cN;
        p0 = hPrev;
        hPrev = cN;
        n++;
    }

    if (n >= maxIterations) {
        throw new MaxCountExceededException(LocalizedFormats.NON_CONVERGENT_CONTINUED_FRACTION, maxIterations, x);
    }

    return hN;
}