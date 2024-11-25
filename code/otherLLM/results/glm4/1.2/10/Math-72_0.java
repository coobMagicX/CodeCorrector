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

    // return the first endpoint if it is good enough
    double yMin = f.value(min);
    if (Math.abs(yMin) <= functionValueAccuracy) {
        setResult(min, 0);
        return result;
    }

    // reduce interval if min and initial bracket the root
    if (yInitial * yMin < 0) {
        double midpoint = (min + yMin) / 2.0;
        return solve(f, min, midpoint, midpoint, max, initial, yInitial);
    }

    // return the second endpoint if it is good enough
    double yMax = f.value(max);
    if (Math.abs(yMax) <= functionValueAccuracy) {
        setResult(max, 0);
        return result;
    }

    // reduce interval if initial and max bracket the root
    if (yInitial * yMax < 0) {
        double midpoint = (initial + yMax) / 2.0;
        return solve(f, initial, midpoint, midpoint, max, initial, yInitial);
    }

    if (yMin * yMax > 0) {
        throw MathRuntimeException.createIllegalArgumentException(
              NON_BRACKETING_MESSAGE, min, max, yMin, yMax);
    }

    // full Brent algorithm starting with provided initial guess
    return solve(f, min, initial, max, initial, initial, yInitial);
}

// Helper method for the recursive call to create a new midpoint and adjust bounds
private double solve(final UnivariateRealFunction f,
                    final double min, final double yMin, final double initial,
                    final double max, final double yMax, final double initialGuess)
    throws MaxIterationsExceededException, FunctionEvaluationException {

    clearResult();
    verifySequence(min, initial, max);

    // ... (rest of the implementation remains unchanged)

    // The recursive call will be made here with the new midpoint and bounds
}