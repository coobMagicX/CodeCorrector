public double solve(final UnivariateRealFunction f, double min, double max, double initial)
    throws MaxIterationsExceededException, FunctionEvaluationException {
    clearResult();
    verifyInterval(min, max);
    double m;
    double fm;
    double fmin;

    m = initial;
    fm = f.value(initial);
    fmin = f.value(min);

    int i = 0;
    while (i < maximalIterationCount) {
        if (fm * fmin > 0.0) {
            // max and m bracket the root.
            min = m;
        } else {
            // min and m bracket the root.
            max = m;
        }

        if (Math.abs(max - min) <= absoluteAccuracy) {
            setResult(m, i);
            return m;
        }

        m = UnivariateRealSolverUtils.midpoint(min, max);
        fmin = f.value(min);
        fm = f.value(m);
        ++i;
    }

    throw new MaxIterationsExceededException(maximalIterationCount);
}