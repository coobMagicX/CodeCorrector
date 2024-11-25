public double solve(final UnivariateRealFunction f,
                   final double min, final double max, final double initial)
        throws MaxIterationsExceededException, FunctionEvaluationException {

    clearResult();
    verifySequence(min, initial, max);

    // Check if interval brackets a root and throw IllegalArgumentException if it does not
    double yMin = f.value(min);
    double yInitial = f.value(initial);
    double yMax = f.value(max);
    
    if ((yMin * yInitial > 0) || (yInitial * yMax > 0)) {
        throw new IllegalArgumentException("Interval does not bracket a root.");
    }

    // return the initial guess if it is good enough
    if (Math.abs(yInitial) <= functionValueAccuracy) {
        setResult(initial, 0);
        return result;
    }

    // return the first endpoint if it is good enough
    if (Math.abs(yMin) <= functionValueAccuracy) {
        setResult(min, 0);
        return result;
    }

    // reduce interval if min and initial bracket the root
    if ((yInitial * yMin < 0)) {
        double midpoint = initial - (initial - min) / 3.0 * Math.cbrt(yInitial / yMin);
        return solve(f, min, midpoint, initial, midpoint, yInitial, midPoint);
    }

    // return the second endpoint if it is good enough
    if (Math.abs(yMax) <= functionValueAccuracy) {
        setResult(max, 0);
        return result;
    }

    // reduce interval if initial and max bracket the root
    if ((yInitial * yMax < 0)) {
        double midpoint = initial + (max - initial) / 3.0 * Math.cbrt(yMax / yInitial);
        return solve(f, initial, midpoint, midpoint, max, yInitial, yMax);
    }

    // full Brent algorithm starting with provided initial guess
    return solve(f, min, yMin, max, yMax, initial, yInitial);
}