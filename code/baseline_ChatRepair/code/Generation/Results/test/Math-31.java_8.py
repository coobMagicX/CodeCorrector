public double evaluate(double x, double epsilon, int maxIterations) {
    final double small = 1e-50;
    double hPrev = getA(0, x);

    // use the value of small as epsilon criteria for zero checks
    if (Math.abs(hPrev) < small) {
        hPrev = small;
    }

    int n = 1;
    double dPrev = 0.0;
    double cPrev = hPrev;
    double q1 = 1.0;
    double p0 = 1.0;
    double hN = hPrev;

    while (n < maxIterations) {
        final double a = getA(n, x);
        final double b = getB(n, x);
        double cN = a * hPrev + b * p0;
        double q2 = a * q1 + b * dPrev;

        // Avoiding overflows/underflows
        if (Double.isInfinite(cN) || Double.isInfinite(q2)) {
            double scaleFactor = 1.0;
            final double maxScaleFactor = 1e10;
            boolean rescaled = false;

            while (scaleFactor < maxScaleFactor) {
                scaleFactor *= 100;
                cN = a * hPrev + b / scaleFactor * p0;
                q2 = a * q1 + b / scaleFactor * dPrev;
                if (!Double.isInfinite(cN) && !Double.isInfinite(q2)) {
                    rescaled = true;
                    break;
                }
            }
            
            if (!rescaled) {
                throw new ConvergenceException(LocalizedFormats.CONTINUED_FRACTION_INFINITY_DIVERGENCE, x);
            }
        }

        double deltaN = cN / q2;
        hN = cPrev * deltaN;

        if (Double.isInfinite(hN) || Double.isNaN(hN)) {
            throw new ConvergenceException(LocalizedFormats.CONTINUED_FRACTION_INFINITY_DIVERGENCE, x);
        }

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
        throw new MaxCountExceededException(LocalizedFormats.NON_CONVERGENT_CONTINUED_FRACTION, maxIterations, x);
    }

    return hN;
}
