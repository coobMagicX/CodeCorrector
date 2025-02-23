protected final double doSolve() {
    double x0 = getMin();
    double x1 = getMax();
    double f0 = computeObjectiveValue(x0);
    double f1 = computeObjectiveValue(x1);

    if (f0 == 0.0) return x0;
    if (f1 == 0.0) return x1;

    verifyBracketing(x0, x1);

    final double ftol = getFunctionValueAccuracy();
    final double atol = getAbsoluteAccuracy();
    final double rtol = getRelativeAccuracy();

    boolean inverted = false;
    double prevX = Double.NaN;

    while (true) {
        final double x = x1 - ((f1 * (x1 - x0)) / (f1 - f0));
        final double fx = computeObjectiveValue(x);

        if (fx == 0.0) return x;

        if (Math.abs(x - prevX) < atol + rtol * Math.abs(x)) {
            // Break if the change is too small to avoid infinite loop
            return x;
        }
        prevX = x;

        if (f1 * fx < 0) {
            x0 = x1;
            f0 = f1;
            inverted = !inverted;
        } else {
            switch (method) {
                case ILLINOIS:
                    f0 *= 0.5;
                    break;
                case PEGASUS:
                    f0 *= f1 / (f1 + fx);
                    break;
                default:
                    // can define other methods or error
            }
        }
        x1 = x;
        f1 = fx;

        if (Math.abs(f1) <= ftol || Math.abs(x1 - x0) < Math.max(rtol * Math.abs(x1), atol)) {
            if (allowed == AllowedSolution.ANY_SIDE || 
                (allowed == AllowedSolution.LEFT_SIDE && !inverted) ||
                (allowed == AllowedSolution.RIGHT_SIDE && inverted) ||
                (allowed == AllowedSolution.BELOW_SIDE && f1 <= 0) || 
                (allowed == AllowedSolution.ABOVE_SIDE && f1 >= 0)) {
                return x1;
            } else {
                return x0;  // Return a safe default, x0 or properly handle the other cases
            }
        }
    }
}
