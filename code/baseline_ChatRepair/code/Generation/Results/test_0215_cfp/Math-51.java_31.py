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
    final int maxEvaluations = getMaxEvaluations();  // Assuming a method to get a maximum allowed number of evaluations
    int evaluations = 2;  // Already evaluated f0 and f1 at minimum and maximum

    while (evaluations < maxEvaluations) {
        final double x = x1 - ((f1 * (x1 - x0)) / (f1 - f0));
        final double fx = computeObjectiveValue(x);
        evaluations++;

        if (fx == 0.0 || FastMath.abs(fx) <= ftol || FastMath.abs(x1 - x0) < FastMath.max(rtol * FastMath.abs(x1), atol)) {
            return x;
        }

        if (f1 * fx < 0) {
            x0 = x1;
            f0 = f1;
        } else if (method == Method.ILLINOIS) {
            f0 *= 0.5;
        } else if (method == Method.PEGASUS){
            f0 *= f1 / (f1 + fx);
        }

        x1 = x;
        f1 = fx;
    }

    throw new TooManyEvaluationsException(maxEvaluations);
}
