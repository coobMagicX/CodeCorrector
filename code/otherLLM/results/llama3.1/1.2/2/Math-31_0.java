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

        // Numerical stability check: avoid zero division
        if (a == 0.0 && b != 0.0) {
            throw new ConvergenceException(LocalizedFormats.ZERO_DIVISION, x);
        }

        double cN = a * hPrev + b * p0;
        double q2 = a * q1 + b * dPrev;

        // Scaling factor optimization: re-examine and optimize the scaling factor calculation logic
        double scaleFactor = 1d;
        if (FastMath.abs(a) > FastMath.abs(b)) {
            scaleFactor = b / a;
        } else {
            scaleFactor = a / b;
        }

        for (int i = 0; i < 5; i++) { // maxPower is not used, assuming you meant to use this value
            double lastScaleFactor = scaleFactor;
            scaleFactor *= FastMath.max(FastMath.abs(a), FastMath.abs(b));
            if (a != 0.0 && a > b) {
                cN = hPrev / lastScaleFactor + (b / scaleFactor * p0);
                q2 = q1 / lastScaleFactor + (b / scaleFactor * dPrev);
            } else if (b != 0) {
                cN = (a / scaleFactor * hPrev) + p0 / lastScaleFactor;
                q2 = (a / scaleFactor * q1) + dPrev / lastScaleFactor;
            }
            if (!(Double.isInfinite(cN) || Double.isInfinite(q2))) {
                break;
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

        // Epsilon check: avoid unnecessary iterations
        if (FastMath.abs(deltaN - 1.0) < epsilon) {
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