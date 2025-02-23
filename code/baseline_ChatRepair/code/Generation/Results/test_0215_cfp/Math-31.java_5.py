public double evaluate(double x, double epsilon, int maxIterations) {
    final double small = 1e-50; // Small threshold to avoid divisions by zero
    double hPrev = getA(0, x);

    if (Math.abs(hPrev) < small) {
        hPrev = small; // Initialize hPrev if it results in too small or exact zero value
    }

    int n = 1;
    double dPrev = 0.0;
    double p0 = 1.0;
    double q1 = 1.0;
    double cPrev = hPrev;

    while (n < maxIterations) {
        final double a = getA(n, x);
        final double b = getB(n, x);
        
        double cN = a * hPrev + b * p0;
        double q2 = a * q1 + b * dPrev;

        if (Double.isInfinite(cN) || Double.isInfinite(q2) || Math.abs(cN) > 1e100 || Math.abs(q2) > 1e100) { 
            // If values start to blow up, rescale them
            double scaleFactor = Math.max(Math.abs(cN), Math.abs(q2));
            while (Math.abs(cN) > 1e100 || Math.abs(q2) > 1e100){
                cN /= scaleFactor;
                q2 /= scaleFactor;
            }
        }

        double deltaN = cN / q2;

        if (Math.abs(deltaN) < small || Double.isNaN(deltaN) || Double.isInfinity(deltaN)) {
            deltaN = small;
        }

        double hN = cPrev * deltaN;

        if (Math.abs(hN) < small || Double.isNaN(hN) || Double.isInfinite(hN)) {
            throw new ConvergenceException(LocalizedFormats.CONTINUED_FRACTION_INFINITY_DIVERGENCE, x);
        }
        
        if (Math.abs(deltaN - 1.0) < epsilon) { // Check for convergence
            return hN;
        }
        
        dPrev = q1;
        cPrev = hN;
        p0 = hPrev;
        hPrev = cN;
        q1 = q2;
        
        n++;
    }

    throw new MaxCountExceededException(LocalizedFormats.NON_CONVERGENT_CONTINUED_FRACTION, maxIterations, x);
}
