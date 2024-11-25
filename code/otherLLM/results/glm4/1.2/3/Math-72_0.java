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
        setResult(yMin, 0);
        return result;
    }

    // reduce interval if min and initial bracket the root
    boolean bracketInitialAndMin = (yInitial * yMin < 0);
    double reducedMin = bracketInitialAndMin ? yMin : min;
    double reducedMax = bracketInitialAndMin ? min : yMin;

    // return the second endpoint if it is good enough
    double yMax = f.value(max);
    if (Math.abs(yMax) <= functionValueAccuracy) {
        setResult(yMax, 0);
        return result;
    }

    boolean bracketInitialAndMax = (yInitial * yMax < 0);
    double reducedMin2 = bracketInitialAndMax ? yMax : max;
    double reducedMax2 = bracketInitialAndMax ? max : yMax;

    if (bracketInitialAndMin) {
        return solve(f, min, reducedMin, initial, yInitial, reducedMin, reducedMax);
    }

    if (bracketInitialAndMax) {
        return solve(f, initial, yInitial, max, yMax, initial, yInitial);
    }

    // If neither bracketing condition is met for both pairs of endpoints and initial,
    // we need to handle the case where yMin * yMax > 0
    double midpoint = (min + max) / 2;
    if (Math.abs(f.value(midpoint)) <= functionValueAccuracy) {
        setResult(midpoint, 0);
        return result;
    }

    if (yMin * yMax > 0) {
        throw MathRuntimeException.createIllegalArgumentException(
              NON_BRACKETING_MESSAGE, min, max, yMin, yMax);
    }

    // full Brent algorithm starting with provided initial guess
    return solve(f, min, reducedMin2, max, reducedMax2, initial, yInitial);
}

// Note: The recursive call to 'solve' should be fixed by adding the proper parameters for bracketing and convergence.