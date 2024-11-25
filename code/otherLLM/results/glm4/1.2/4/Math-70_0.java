public double solve(final UnivariateRealFunction f, double min, double max, double initial)
throws MaxIterationsExceededException, FunctionEvaluationException {

    clearResult();
    verifyInterval(min, max);

    double m;
    double fm;
    double fmin;

    int i = 0;
    while (i < maximalIterationCount) {
        // Use the initial guess within the specified range
        if (initial > max || initial < min) {
            throw new IllegalArgumentException("Initial guess is out of bounds.");
        }

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