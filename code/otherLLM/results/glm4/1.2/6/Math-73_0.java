public double solve(final UnivariateRealFunction f,
                    final double min, final double max, final double initial)
    throws MaxIterationsExceededException, FunctionEvaluationException {

    clearResult();
    verifySequence(min, initial, max);

    // Validate the input parameters for bracketing
    if (min >= max) {
        throw new IllegalArgumentException("Invalid interval: lower bound must be less than upper bound.");
    }
    double yInitial = f.value(initial);
    if (yInitial * f.value(min) < 0) { // Check if initial and min bracket the root
        return solve(f, min, Math.min(Math.max(yInitial, min), max), initial, yInitial, min, yInitial);
    }

    double yMax = f.value(max);
    if (initial * yMax < 0) { // Check if initial and max bracket the root
        return solve(f, initial, Math.min(Math.max(initial, max), max), initial, yMax, initial, yMax);
    }

    double yMin = f.value(min);
    if (min * yMin < 0) { // Check if min and max bracket the root
        return solve(f, Math.min(Math.max(min, initial), max), min, Math.max(min, initial), yMin, min, yMin);
    }

    // If no bracketing interval is found, throw an IllegalArgumentException
    throw new IllegalArgumentException("No bracketing interval found. The function values at the interval bounds do not have opposite signs.");

    // Note: No changes were made to the call to solve with five parameters, as it's not provided in the context.
}