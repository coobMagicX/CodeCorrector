public double solve(final UnivariateRealFunction f, double min, double max, double initial)
        throws MaxIterationsExceededException, FunctionEvaluationException {

    clearResult();
    verifyInterval(min, max);

    // Adjust the initial guess if it is outside the interval or does not bracket the root
    if (initial <= min || initial >= max) {
        throw new IllegalArgumentException("Initial guess must be within the bracketing interval [min, max].");
    }

    double m;
    double fm;
    double fmin;

    int i = 0;
    while (i < maximalIterationCount) {
        m = UnivariateRealSolverUtils.midpoint(min, max);
        fmin = f.value(min);
        fm = f.value(m);

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
    }

    throw new MaxIterationsExceededException(maximalIterationCount);
}