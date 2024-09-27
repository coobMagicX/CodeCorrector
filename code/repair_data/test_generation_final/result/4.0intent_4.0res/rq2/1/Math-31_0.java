public double evaluate(double x, double epsilon, int maxIterations) {
    final double small = 1e-50;
    double hPrev = getA(0, x);

    // Use the value of small as epsilon criteria for zero checks
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

        double cN = a * hPrev + b * p0;
        double q2 = a * q1 + b * dPrev;

        if (Double.isInfinite(cN) || Double.isInfinite(q2) || Math.abs(q2) < small) {
            double scaleFactor = 1d;
            double lastScaleFactor = 1d;
            final int maxPower = 5;
            final double scale = Math.max(Math.abs(a), Math.abs(b));
            if (scale <= small) {  // Can't scale
                throw new ConvergenceException(LocalizedFormats.CONTINUED_FRACTION_INFINITY_DIVERGENCE, x);
            }
            for (int i = 0; i < maxPower; i++) {
                lastScaleFactor = scaleFactor;
                scaleFactor *= scale;
                cN = (a / scaleFactor * hPrev) + (b / scaleFactor * p0);
                q2 = (a / scaleFactor * q1) + (b / scaleFactor * dPrev);
                if (!(Double.isInfinite(cN) || Double.isInfinite(q2) || Math.abs(q2) < small)) {
                    break;
                }
            }
        }

        final double deltaN = cN / q2 / cPrev;
        hN = cPrev * deltaN;

        if (Double.isInfinite(hN)) {
            throw new ConvergenceException(LocalizedFormats.CONTINUED_FRACTION_INFINITY_DIVERGENCE, x);
        }
        if (Double.isNaN(hN)) {
            throw new ConvergenceException(LocalizedFormats.CONTINUED_FRACTION_NAN_DIVERGENCE, x);
        }

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