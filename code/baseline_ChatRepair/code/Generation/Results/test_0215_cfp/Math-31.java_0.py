public double evaluate(double x, double epsilon, int maxIterations) {
    final double small = 1e-50;
    double hPrev = getA(0, x);

    // Handle the small value explicitly to avoid division by zero.
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
        if (Double.isInfinite(cN) || Double.isInfinite(q2) || Math.abs(q2) < small) {
            // Scale the terms if overflow/underflow is likely
            double scaleFactor = Math.max(Math.abs(a), Math.abs(b));
            if (scaleFactor != 0) {
                // Reducing excessive scale when values become unmanageably large or small.
                cN /= scaleFactor;
                q2 /= scaleFactor;
            }
            if (Math.abs(q2) < small) {
                q2 = small;
            }
        }

        final double deltaN = (q2 != 0) ? cN / q2 / cPrev : 0;
        hN = cPrev * deltaN;

        if (Double.isInfinite(hN) || Double.isNaN(hN)) {
            throw new ConvergenceException(LocalizedFormats.CONTINUED_FRACTION_INFINITY_DIVERGENCE, x);
        }

        if (Math.abs(deltaN - 1.0) < epsilon) {
            break;
        }

        dPrev = q1;
        cPrev = (q2 != 0) ? cN / q2 : small;
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
