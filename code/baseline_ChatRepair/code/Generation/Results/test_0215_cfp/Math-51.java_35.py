protected final double doSolve() {
    // Initial bounds and function values
    double x0 = getMin();
    double x1 = getMax();
    double f0 = computeObjectiveValue(x0);
    double f1 = computeObjectiveValue(x1);

    if (f0 == 0.0) {
        return x0;
    }
    if (f1 == 0.0) {
        return x1;
    }

    verifyBracketing(x0, x1);

    final double ftol = getFunctionValueAccuracy();
    final double atol = getAbsoluteAccuracy();
    final double rtol = getRelativeAccuracy();

    boolean inverted = false;
    
    while (true) {
        double x = x1 - ((f1 * (x1 - x0)) / (f1 - f0));
        double fx = computeObjectiveValue(x);

        if (fx == 0.0) {
            return x;
        }

        // Midpoint convergence test to avoid too small updates.
        double midpoint = 0.5 * (x0 + x1);
        if (FastMath.abs(x - midpoint) < atol + rtol * FastMath.abs(x)) {   
            return x;  // return midpoint as an approximation
        }

        if (fx * f1 < 0) {
            x0 = x1;
            f0 = f1;
            inverted = !inverted;
        } else {
            if (method == Method.ILLINOIS || method == Method.PEGASUS) {
                f0 *= 0.5;  // This step may need adjustments for stability in some cases
            } else {
                // Throw exception or log if an unexpected method is encountered
                throw new MathInternalError();
            }
        }

        x1 = x;
        f1 = fx;

        if (FastMath.abs(f1) <= ftol) {
            if (inverted) {   // Possible corrections for side constraints
                return allowed == ConvergenceSide.RIGHT_SIDE ? x0 : x1;
            } else {
                return x1;
            }
        }

        if (FastMath.abs(x1 - x0) < FastMath.max(rtol * FastMath.abs(x1), atol)) {
            return x1; // updated to always return x1 as it is the latest approximation.
        }
    }
}
