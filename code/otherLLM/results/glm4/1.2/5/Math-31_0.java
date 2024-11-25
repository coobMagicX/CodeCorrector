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

        double cN = a * hPrev + b * p0;
        double q2 = a * q1 + b * dPrev;
        
        // Check for potential overflow before computation
        if (Double.isInfinite(a) || Double.isInfinite(b) || Double.isInfinite(cN) || Double.isInfinite(q2)) {
            throw new ConvergenceException(LocalizedFormats.CONTINUED_FRACTION_INFINITY_DIVERGENCE, x);
        }

        double cPrevCheck = cPrev;
        cPrev = cN;
        
        // Check for precision issues before updating hN
        if (Double.isNaN(cN / q2)) {
            throw new ConvergenceException(LocalizedFormats.CONTINUED_FRACTION_NAN_DIVERGENCE, x);
        }
        hN = cPrevCheck * (cN / q2);

        if (FastMath.abs(hN) > Double.MAX_VALUE || FastMath.abs(hN) < Double.MIN_NORMAL) {
            throw new ConvergenceException(LocalizedFormats.CONTINUED_FRACTION_OVERFLOW_DIVERGENCE, x);
        }

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