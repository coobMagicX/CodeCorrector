public double solve(final UnivariateRealFunction f,
                    final double min, final double max, final double initial)
throws MaxIterationsExceededException, FunctionEvaluationException {

    clearResult();
    verifySequence(min, initial, max);

    // return the initial guess if it is good enough
    double yInitial = f.value(initial);
    if (Math.abs(yInitial) <= functionValueAccuracy) {
        setResult(initial, 0);
        return result;
    }

    // bracketing check: ensure that the provided endpoints bracket a root by checking the signs of function values at these points.
    boolean minBracketed = (yInitial * f.value(min)) < 0;
    boolean maxBracketed = (yInitial * f.value(max)) < 0;

    if (!minBracketed && !maxBracketed) {
        // If neither endpoint brackets a root, throw an IllegalArgumentException
        throw new IllegalArgumentException("Given intervals do not bracket a root");
    }

    // return the first or second endpoint if it is good enough
    double yMin = f.value(min);
    double yMax = f.value(max);

    if (Math.abs(yMin) <= functionValueAccuracy && minBracketed) {
        setResult(yMin, 0);
        return result;
    }
    if (Math.abs(yMax) <= functionValueAccuracy && maxBracketed) {
        setResult(yMax, 0);
        return result;
    }

    // reduce interval if min and initial bracket the root
    if (minBracketed) {
        return solve(f, min, yMin, max, yMax, initial, yInitial);
    }
    
    // reduce interval if initial and max bracket the root
    if (maxBracketed) {
        return solve(f, initial, yInitial, max, yMax, initial, yInitial);
    }

}