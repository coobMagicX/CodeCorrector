protected final double doSolve() throws ConvergenceException {
    // Get initial solution
    final double min = getMin();
    final double max = getMax();
    double x0 = min;
    double x1 = max;
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

    int maxEvaluations = getMaxEvaluations();
    int evaluations = 0;

    while (evaluations < maxEvaluations) {
        evaluations++;

        final double x = x1 - (f1 * (x1 - x0) / (f1 - f0));
        final double fx = computeObjectiveValue(x);

        if (fx == 0.0) {
            return x;
        }
        
        f0 = f1;
        x0 = x1;
        x1 = x;
        f1 = fx;
        
        if (Math.abs(f1) <= ftol) {
            return x1;
        }
        
        if (Math.abs(x1 - x0) < Math.max(rtol * Math.abs(x1), atol)) {
            return x1;
        }
    }
    throw new ConvergenceException();
}
