public double evaluate(double x, double epsilon, int maxIterations) {
    final double small = 1e-50; // Avoid potential divide-by-zero
    double hPrev = getA(0, x);

    if (Math.abs(hPrev) < small) { // Use small if hPrev is zero or very close to zero
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

        if (Double.isInfinite(cN) || Double.isInfinite(q2) || cN == 0 || q2 == 0) {
            if (b != 0) {
                double scalingFactor = Math.max(Math.abs(a), Math.abs(b));
                cN = (cN / scalingFactor) * scalingFactor; // Re-normalize to manage scale
                q2 = (q2 / scalingFactor) * scalingFactor; // Re-normalize to manage scale
            } else {
                throw new ConvergenceException(LocalizedFormats.CONTINUED_FRACTION_INFINITY_DIVERGENCE, x);
            }
        }

        double deltaN = cN / q2;

        if (Math.abs(deltaN) < small || Double.isNaN(deltaN) || Double.isInfinite(deltaN)) {
            deltaN = small;
        }

        hN = cPrev * deltaN;

        if (Double.isNaN(hN) || Double.isInfinite(hN)) {
            throw new ConvergenceException(LocalizedFormats.CONTINUED_FRACTION_NAN_DIVERGENCE, x);
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
