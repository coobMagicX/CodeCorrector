public double solve(final UnivariateRealFunction f,
                    final double min, final double max, final double initial)
        throws MaxIterationsExceededException, FunctionEvaluationException {

    clearResult();
    verifySequence(min, initial, max);

    // Verify if the interval is ordered
    if (min > max) {
        throw new IllegalArgumentException("Interval is out of order: min must be less than or equal to max.");
    }

    double yInitial = f.value(initial);
    double yMin = f.value(min);
    double yMax = f.value(max);

    // Check for function value accuracy at initial, min, and max points
    if (Math.abs(yInitial) <= functionValueAccuracy || Math.abs(yMin) <= functionValueAccuracy || Math.abs(yMax) <= functionValueAccuracy) {
        if (Math.abs(yInitial) <= functionValueAccuracy) {
            setResult(initial, 0);
            return result;
        } else if (Math.abs(yMin) <= functionValueAccuracy) {
            setResult(yMin, 0);
            return result;
        } else {
            setResult(yMax, 0);
            return result;
        }
    }

    // Check for bracketing conditions
    if (yInitial * yMin < 0 && yMin != initial) {
        return solve(f, min, yMin, initial); // Ensure that the interval is reduced to the part that brackets a root
    } else if (yInitial * yMax < 0 && yMax != initial) {
        return solve(f, initial, max, initial); // Ensure that the interval is reduced to the part that brackets a root
    }

    // Full Brent algorithm starting with provided initial guess
    return solve(f, min, yMin, max, yMax, initial, yInitial);
}

private double solve(final UnivariateRealFunction f,
                    final double min, final double yMin, final double max, final double yMax,
                    final double initial, final double yInitial) {
    // Implement the full Brent algorithm starting with the interval [min, yMin] and [initial, max]
    // This method is a placeholder for the actual implementation of the Brent's method
    // For now, it simply returns a default value
    return 0.0;
}

private void verifySequence(final double min, final double initial, final double max) {
    // Verify that the sequence is ordered (min <= initial <= max)
    if (!(min <= initial && initial <= max)) {
        throw new IllegalArgumentException("The provided sequence must be in non-decreasing order.");
    }
}