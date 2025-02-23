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
        final double x = x1 - (f1 * (x1 - x0)) / (f1 - f0);
        final double fx = computeObjectiveValue(x);

        if (fx == 0.0) {
            return x;
        }

        if (f1 * fx < 0) {  // Change in sign, fall through.
            x0 = x1;
            f0 = f1;
            inverted = !inverted;
        } else {
            if (Math.abs(x1 - x) < FastMath.max(rtol * FastMath.abs(x1), atol)) {
                return x1;  // No progress, likely a repeat of previous bound, terminate.
            }
            switch (method) {
            case ILLINOIS:
                f0 *= 0.5;
                break;
            case PEGASUS:
                f0 *= f1 / (f1 + fx);
                break;
            default:
                break;  // No change to f0, risk getting stuck in infinite loop, possibly reconsider how f0 is changed.
            }
        }

        x1 = x;  // Always update x1 to the newly computed mid point.
        f1 = fx;  // Always update f1 to the newly computed function value.

        // Terminate if function value is close enough to zero.
        if (Math.abs(f1) <= ftol) {
            return x1;
        }

        // Terminate if change is less than tolerance.
        if (Math.abs(x1 - x0) < Math.max(rtol * Math.abs(x1), atol)) {
            return inverted ? x0 : x1;
        }
    }
}
