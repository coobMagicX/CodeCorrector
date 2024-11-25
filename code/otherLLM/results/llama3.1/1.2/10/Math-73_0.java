public double solve(final UnivariateRealFunction f,
                    final double min, final double max, final double initial)
    throws MaxIterationsExceededException, FunctionEvaluationException {

    clearResult();
    verifySequence(min, initial, max);

    // Check for invalid intervals before proceeding with the calculation
    if (min >= max) {
        throw new IllegalArgumentException("Input interval does not bracket a root");
    }

    // return the initial guess if it is good enough
    double yInitial = f.value(initial);
    if (Math.abs(yInitial) <= functionValueAccuracy) {
        setResult(initial, 0);
        return result;
    }

    // Check for non-bracketing conditions and throw IllegalArgumentException when necessary
    double yMin = f.value(min);
    double yMax = f.value(max);
    if ((yInitial * yMin >= 0 && yInitial * yMax >= 0) || (yMin * yMax >= 0)) {
        throw new IllegalArgumentException("Input interval does not bracket a root");
    }

    // return the first endpoint if it is good enough
    if (Math.abs(yMin) <= functionValueAccuracy) {
        setResult(min, 0);
        return result;
    }

    // return the second endpoint if it is good enough
    if (Math.abs(yMax) <= functionValueAccuracy) {
        setResult(max, 0);
        return result;
    }

    // full Brent algorithm starting with provided initial guess
    return solve(f, min, max, yMin, yMax, initial, yInitial);

}