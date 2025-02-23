protected final double doSolve() {
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
        final double x = x1 - ((f1 * (x1 - x0)) / (f1 - f0));
        final double fx = computeObjectiveValue(x);

        if (fx == 0.0) {
            return x;
        }

        if (f1 * fx < 0) {
            x0 = x1;
            f0 = f1;
            inverted = !inverted;
        } else {
            // This segment ensures there is some update to f0 to avoid repetition
            f0 *= fx / (f0 + fx);
        }

        x1 = x;
        f1 = fx;

        if (Math.abs(f1) <= ftol) {
            return x1;
        }

        if (Math.abs(x1 - x0) < Math.max(rtol * Math.abs(x1), atol)) {
            return x1;
        }

        // Ensure there's always a slight shift in endpoints to avoid infinite loops
        if (Math.abs(x1 - x0) < 1E-10 * Math.abs(x1)) {
            x0 -= 1E-10 * Math.abs(x0); // Always adjust x0 slightly
            f0 = computeObjectiveValue(x0);

            if (Math.abs(f0) <= ftol) {
                return x0;
            }
        }
    }
}
