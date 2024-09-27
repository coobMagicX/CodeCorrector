public double solve(final UnivariateRealFunction f,
                    final double min, final double max, final double initial)
    throws MaxIterationsExceededException, FunctionEvaluationException {

    clearResult();
    verifySequence(min, initial, max);

    // Check for valid interval
    if (min >= max) {
        throw new IllegalArgumentException("Invalid interval: min must be less than max.");
    }

    double yMin = f.value(min);
    double yMax = f.value(max);

    // Check for bracketing of the root
    if (yMin * yMax >= 0) {
        throw new IllegalArgumentException("Non-bracketing interval: Function values at the endpoints must have opposite signs.");
    }

    // Check if initial is a good enough approximation before proceeding
    double yInitial = f.value(initial);
    if (Math.abs(yInitial) <= functionValueAccuracy) {
        setResult(initial, 0);
        return result;
    }

    // Evaluate the function at the initial guess to decide on the direction of the interval reduction
    if (yInitial * yMin < 0) {
        // If initial and min bracket the root
        return solve(f, min, yMin, initial, yInitial, initial, yInitial);
    } else {
        // If initial and max bracket the root
        return solve(f, initial, yInitial, max, yMax, initial, yInitial);
    }
}