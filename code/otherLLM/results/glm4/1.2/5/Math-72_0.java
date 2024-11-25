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
        setResult(min, 0); // Correctly set the endpoint as the result
        return result;
    }

    // return the second endpoint if it is good enough
    double yMax = f.value(max);
    if (Math.abs(yMax) <= functionValueAccuracy) {
        setResult(max, 0); // Correctly set the endpoint as the result
        return result;
    }

    // reduce interval if min and initial bracket the root
    if (yInitial * yMin < 0) {
        return solve(f, min, Math.max(yMin, initial), initial, yMax, initial, yInitial);
    }

    // reduce interval if initial and max bracket the root
    if (yInitial * yMax < 0) {
        return solve(f, initial, yInitial, max, Math.min(yMax, initial), initial, yInitial);
    }

    if (yMin * yMax > 0) {
        throw MathRuntimeException.createIllegalArgumentException(
              NON_BRACKETING_MESSAGE, min, max, yMin, yMax);
    }

    // full Brent algorithm starting with provided initial guess
    return solve(f, min, yMin, max, yMax, initial, yInitial);
}

// Note: The method 'setResult' is assumed to be a part of the existing codebase,
// which sets the result and accuracy for the solver. It's used here in the correct way
// after identifying an endpoint as the root.