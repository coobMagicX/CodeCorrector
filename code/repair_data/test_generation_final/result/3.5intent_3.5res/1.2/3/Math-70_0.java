public double solve(final UnivariateRealFunction f, double min, double max, double initial)
    throws MaxIterationsExceededException, FunctionEvaluationException {
    clearResult();
    verifyInterval(min, max);
    double m;
    double fm;
    double fmin;

    // Start at the initial point if provided
    m = initial;
    fm = f.value(m);
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
            m = UnivariateRealSolverUtils.midpoint(min, max);
            setResult(m, i);
            return m;
        }
        ++i;

        m = UnivariateRealSolverUtils.midpoint(min, max);
        fm = f.value(m);
    }

    throw new MaxIterationsExceededException(maximalIterationCount);
}