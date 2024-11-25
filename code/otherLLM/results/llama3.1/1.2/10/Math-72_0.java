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

    // Validate Interval Endpoints
    if (f.value(min) * f.value(max) > 0) {  // Check if endpoints bracket the root
        throw MathRuntimeException.createIllegalArgumentException(
              NON_BRACKETING_MESSAGE, min, max, f.value(min), f.value(max));
    }

    // return the first endpoint if it is good enough
    double yMin = f.value(min);
    if (Math.abs(yMin) <= functionValueAccuracy) {
        setResult(yMin, 0);
        return result;
    }

    // return the second endpoint if it is good enough
    double yMax = f.value(max);
    if (Math.abs(yMax) <= functionValueAccuracy) {
        setResult(yMax, 0);
        return result;
    }

    // Handle Root Detection Correctly
    if (yInitial * yMin < 0) {  // If initial and min bracket the root
        return solve(f, min, yMin, max, yMax, initial, yInitial);
    } else if (yInitial * yMax < 0) {  // If initial and max bracket the root
        return solve(f, initial, yInitial, max, yMax, min, yMin);
    }

    // Correct Absolute Accuracy Comparison
    double brentValue = f.value((min + max) / 2.0);  // Evaluate function at midpoint of interval
    if (Math.abs(brentValue) <= functionValueAccuracy) {  // Check if midpoint is good enough
        setResult((min + max) / 2.0, 0);
        return result;
    }

    // full Brent algorithm starting with provided initial guess
    return solve(f, min, yMin, max, yMax, initial, yInitial);

}