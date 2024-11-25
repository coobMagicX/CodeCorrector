public double solve(final UnivariateRealFunction f,
                    final double min, final double max, final double initial)
    throws MaxIterationsExceededException, FunctionEvaluationException {

    clearResult();
    verifySequence(min, initial, max);

    // Check for non-bracketing intervals by ensuring that function values at the endpoints do not have the same sign
    if ((f.value(min) * f.value(max) >= 0) || (min >= max)) {
        throw new IllegalArgumentException("Initial interval does not bracket the root.");
    }

    double yInitial = f.value(initial);
    if (Math.abs(yInitial) <= functionValueAccuracy) {
        setResult(initial, 0);
        return result;
    }

    // Check for bracketing conditions before solving
    if ((yInitial * f.value(min) >= 0) || (yInitial * f.value(max) >= 0)) {
        throw new IllegalArgumentException("The initial guess is not in a bracketing interval.");
    }

    double yMin = f.value(min);
    if (Math.abs(yMin) <= functionValueAccuracy) {
        setResult(yMin, 0);
        return result;
    }

    double yMax = f.value(max);
    if (Math.abs(yMax) <= functionValueAccuracy) {
        setResult(yMax, 0);
        return result;
    }

    // Reduce interval if min and initial bracket the root
    if (yInitial * yMin < 0) {
        return solve(f, min, yMin, initial, yInitial, min, yMin);
    }

    // Reduce interval if initial and max bracket the root
    if (yInitial * yMax < 0) {
        return solve(f, initial, yInitial, max, yMax, initial, yInitial);
    }

    // Full Brent algorithm starting with provided initial guess
    return solve(f, min, yMin, max, yMax, initial, yInitial);

}