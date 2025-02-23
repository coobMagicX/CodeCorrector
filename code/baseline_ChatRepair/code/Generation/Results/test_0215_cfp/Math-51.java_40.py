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

    while (true) {
        if (computeEvaluations() > getMaxEvaluations()) { // Adding evaluation count check
            throw new TooManyEvaluationsException(getMaxEvaluations());
        }

        double x = x1 - (f1 * (x1 - x0)) / (f1 - f0);
        double fx = computeObjectiveValue(x);
        if (fx == 0.0) return x;

        if (f1 * fx < 0) {
            x0 = x1;
            f0 = f1;
            inverted = !inverted;
        } else {
            if (method == Method.BISECTION) { // Fallback to bisection
                x = 0.5 * (x0 + x1);
                fx = computeObjectiveValue(x);
            } else {
                f0 *= (method == Method.PEGASUS ? f1 / (f1 + fx) : 0.5);
            }
        }

        x1 = x;
        f1 = fx;

        if (Math.abs(f1) <= ftol || Math.abs(x1 - x0) < Math.max(rtol * Math.abs(x1), atol)) {
            return (allowed == AllowedSide.LEFT_SIDE && !inverted) || (allowed == AllowedSide.RIGHT_SIDE && inverted) ? x0 : x1;
        }
    }
}
