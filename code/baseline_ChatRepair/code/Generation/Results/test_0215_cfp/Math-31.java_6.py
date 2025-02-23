public double evaluate(double x, double epsilon, int maxIterations) {
    final double small = 1e-50;
    double hPrev = getA(0, x);

    // Protecting against zero-initialization that can induce infinite loops
    if (Math.abs(hPrev) < small) {
        hPrev = small;
    }

    int n = 1;
    double dPrev = 1.0; // Avoid division by zero in initial loop
    double p0 = 1.0;
    double q1 = 1.0;
    double cPrev = hPrev;
    double hN = hPrev;

    while (n < maxIterations) {
        final double a = getA(n, x);
        final double b = getB(n, x);

        double cN = a * hPrev + b * p0;
        double q2 = a * q1 + b * dPrev;

        // Critical step: check for large values and scale down if needed
        if (Double.isInfinite(cN) || Double.isInfinite(q2)) {
            double scaleFactor = 1d;
            double prevScaleFactor = 1d;
            final double scale = Math.max(Math.abs(a), Math.abs(b));
            
            while (scaleFactor * scale < 1e10) { // Prevent overflow
                prevScaleFactor = scaleFactor;
                scaleFactor *= scale;
                
                cN = (a * hPrev / prevScaleFactor) + (b * p0 / scaleFactor);
                q2 = (a * q1 / prevScaleFactor) + (b * dPrev / scaleFactor);
                      
                if (!Double.isInfinite(cN) && !Double.isInfinite(q2)) {
                    break;
                }
            }
        }

        double deltaN = cN / q2;
        hN = hPrev * deltaN;

        if (Double.isInfinite(hN) || Double.isNaN(hN)) {
            throw new ConvergenceException(LocalizedFormats.CONTINUED_FRACTION_INFINITY_DIVERGENCE, x);
        }

        if (Math.abs(deltaN - 1.0) < epsilon) {
            return hN;
        }

        dPrev = q1;
        cPrev = cN;
        p0 = hPrev;
        hPrev = cN;
        q1 = q2;
        n++;
    }

    throw new MaxCountExceededException(LocalizedFormats.NON_CONVERGENT_CONTINUED_FRACTION,
                                        maxIterations, x);
}
