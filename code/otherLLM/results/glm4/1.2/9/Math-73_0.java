public double solve(final UnivariateRealFunction f,
                    final double min, final double max, final double initial)
    throws MaxIterationsExceededException, FunctionEvaluationException {

    // Check for invalid interval
    if (min > max) {
        throw new IllegalArgumentException("Invalid interval: lower bound must not exceed upper bound.");
    }
    
    // Check for bracketing condition before attempting to solve the function
    double yMin = f.value(min);
    double yInitial = f.value(initial);
    double yMax = f.value(max);

    if (Math.abs(yInitial) <= functionValueAccuracy && Math.abs(yMin) <= functionValueAccuracy && Math.abs(yMax) <= functionValueAccuracy) {
        // All endpoints are good enough
        return solve(f, min, max, initial); // Call solve with full interval
    }

    if ((yInitial * yMin < 0 || (initial == min && yMin != 0)) &&
        (yInitial * yMax < 0 || (initial == max && yMax != 0))) {
        // There is bracketing, so we can apply the Brent's method
        return solve(f, min, yMin, initial, yMax, initial, yInitial);
    }

    if ((min > initial && initial > max) || (max < initial && initial < min)) {
        throw new IllegalArgumentException("Non-bracketing: function values at endpoints do not bracket the root.");
    }

    // If none of the above, we might have an edge case where one endpoint is not bracketed but close enough
    if ((min == initial || yMin * yInitial >= 0) && Math.abs(yMin) <= functionValueAccuracy) {
        return solve(f, min, max, min);
    }

    // If none of the conditions are met, the interval does not satisfy the requirements to solve
    throw new IllegalArgumentException("Invalid or non-bracketing interval provided.");
}

private double solve(final UnivariateRealFunction f,
                    final double min, final double yMin, final double initial,
                    final double max, final double initialGuess, final double initialValue)
    throws MaxIterationsExceededException, FunctionEvaluationException {
    
    // The rest of the code (the full Brent's method) would go here
    // Since we don't have the full context and methods available, this is a placeholder for the actual algorithm.
}