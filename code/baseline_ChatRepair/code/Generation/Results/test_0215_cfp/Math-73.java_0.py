public double solve(final UnivariateRealFunction f,
                    final double min, final double max, final double initial)
    throws MaxIterationsExceededException, FunctionEvaluationException, IllegalArgumentException {

    clearResult();
    verifySequence(min, initial, max);

    // Check function evaluation at points
    double yInitial = f.value(initial);
    double yMin = f.value(min);
    double yMax = f.value(max);

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

    // return the second endpoint if it is good enough
    if (Math.abs(yMax) <= functionValueAccuracy) {
        setResult(max, 0);
        return result;
    }

    // Check if the 'min' and 'initial' bracket the root
    if (yInitial * yMin < 0) {
        return solve(f, min, yMin, initial, yInitial, min, yInitial);
    }

    // Check if the 'initial' and 'max' bracket the root
    if (yInitial * yMax < 0) {
        return solve(f, initial, yInitial, max, yMax, initial, yInitial);
    }

    // If no bracketing conditions are met, throw an exception 
    // to indicate no bracketing interval was found.
    throw new IllegalArgumentException("Function values at the endpoints do not have opposite signs: " +
                                        "f(min) = " + yMin + ", f(max) = " + yMax);

    // If above conditions were sorted out successfully 
    // then use the Brent Algorithm
    // return solve(f, min, yMin, max, yMax, initial, yInitial);
}
