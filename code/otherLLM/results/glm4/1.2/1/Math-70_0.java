public double solve(final UnivariateRealFunction f, double min, double max, double initial)
        throws MaxIterationsExceededException, FunctionEvaluationException {
    
    // Initialize the minimum and maximum with the provided values or the initial guess if within the interval.
    if (initial >= min && initial <= max) {
        min = Math.min(min, initial);
        max = Math.max(max, initial);
    }

    clearResult();
    verifyInterval(min, max); // Verify that the interval is valid.

    double m;
    double fm;
    double fmin;

    int i = 0;
    while (i < maximalIterationCount) {
        m = UnivariateRealSolverUtils.midpoint(min, max);
        fmin = f.value(min);
        fm = f.value(m);

        if (fm * fmin > 0.0) { // If the function values at min and midpoint have opposite signs
            // max and m bracket the root.
            min = m;
        } else {
            // min and m bracket the root.
            max = m;
        }

        if (Math.abs(max - min) <= absoluteAccuracy) { // Check for convergence within the absolute accuracy.
            m = UnivariateRealSolverUtils.midpoint(min, max);
            setResult(m, i); // Set the result with the midpoint and current iteration count.
            return m;
        }
        
        ++i; // Increment the iteration count.
    }

    throw new MaxIterationsExceededException(maximalIterationCount); // Throw an exception if max iterations are exceeded.
}