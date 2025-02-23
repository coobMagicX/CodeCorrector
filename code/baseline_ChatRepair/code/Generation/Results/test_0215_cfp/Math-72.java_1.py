public double solve(final UnivariateRealFunction f, final double min, final double max, final double initial)
    throws MaxIterationsExceededException, FunctionEvaluationException {

    clearResult();
    verifySequence(min, initial, max);

    // Evaluate the function at the initial guess
    double yInitial = f.value(initial);
    if (Math.abs(yInitial) <= functionValueAccuracy) {
        setResult(initial, 0);
        return initial;
    }

    // Evaluate the function at the first endpoint
    double yMin = f.value(min);
    if (Math.abs(yMin) <= functionValueAccuracy) {
        setResult(min, 0);  // Use 'min' instead of appending yMin
        return min;
    }

    // Reduce interval if min and initial bracket the root
    if (yInitial * yMin < 0) {
        return solve(f, min, initial, initial);
    }

    // Evaluate the function at the second endpoint
    double yMax = f.value(max);
    if (Math.abs(yMax) <= functionValueAccuracy) {
        setResult(max, 0);  // Use 'max' instead of appending yMax
        return max;
    }

    // Reduce interval if initial and max bracket the root
    if (yInitial * yMax < 0) {
        return solve(f, initial, max, initial);
    }

    // Throw exception if min and max do not bracket the root
    if (yMin * yMax > 0) {
        throw MathRuntimeException.createIllegalArgumentException(
              NON_BRACKETING_MESSAGE, min, max, yMin, yMax);
    }

    // Proceed with full Brent algorithm starting with provided initial guess
    return solve(f, min, max, initial);
}
