public double solve(final UnivariateRealFunction f,
                    final double min, final double max, final double initial)
    throws MaxIterationsExceededException, FunctionEvaluationException {

    clearResult();
    verifySequence(min, initial, max);

    // Check if the interval brackets the root
    double yMin = f.value(min);
    double yMax = f.value(max);
    if (yMin * yMax >= 0) {
        throw new IllegalArgumentException("Function values at the interval endpoints must have opposite signs");
    }

    // return the initial guess if it is good enough
    double yInitial = f.value(initial);
    if (Math.abs(yInitial) <= functionValueAccuracy) {
        setResult(initial, 0);
        return result;
    }

    // return the first endpoint if it is good enough
    if (Math.abs(yMin) <= functionValueAccuracy) {
        setResult(min, 0);  // Fixed incorrect setResult argument from yMin to min
        return result;
    }

    // reduce interval if min and initial bracket the root
    if (yInitial * yMin < 0) {
        return solve(f, min, yMin, initial, yInitial, min, yMin);
    }

    // return the second endpoint if it is good enough
    if (Math.abs(yMax) <= functionValueAccuracy) {
        setResult(max, 0);  // Fixed incorrect setResult argument from yMax to max
        return result;
    }

    // reduce interval if initial and max bracket the root
    if (yInitial * yMax < 0) {
        return solve(f, initial, yInitial, max, yMax, initial, yInitial);
    }

    // full Brent algorithm starting with provided initial guess
    return solve(f, min, yMin, max, yMax, initial, yInitial);
}