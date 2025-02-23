public double solve(final UnivariateRealFunction f,
                    final double min, final double max, final double initial)
    throws MaxIterationsExceededException, FunctionEvaluationException, IllegalArgumentException {

    clearResult();
    verifySequence(min, initial, max);

    // Evaluate function at initial guess and check if it is a good enough solution
    double yInitial = f.value(initial);
    if (Math.abs(yInitial) <= functionValueAccuracy) {
        setResult(initial, 0);
        return result;
    }

    // Evaluate function at min and check if it is a good enough solution
    double yMin = f.value(min);
    if (Math.abs(yMin) <= functionValueAccuracy) {
        setResult(min, 0);
        return result;
    }

    // Only check for bracketing and solve if necessary
    if (yInitial * yMin < 0) {
        return solve(f, min, yMin, initial, yInitial, min, yMin);
    }

    // Evaluate function at max and check if it is a good enough solution
    double yMax = f.value(max);
    if (Math.abs(yMax) <= functionValueAccuracy) {
        setResult(max, 0);
        return result;
    }

    // Check if the 'initial' and 'max' bracket the root
    if (yInitial * yMax < 0) {
        return solve(f, initial, yInitial, max, yMax, initial, yInitial);
    }

    // If no bracketing conditions are met, throw an exception 
    // to indicate no bracketing interval was found.
    throw new IllegalArgumentException("Function values at the endpoints do not have opposite signs: " +
                                       "f(min) = " + yMin + ", f(max) = " + yMax);
}
