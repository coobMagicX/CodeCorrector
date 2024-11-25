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
        setResult(min, 0);
        return result;
    }

    // reduce interval if min and initial bracket the root
    if (yInitial * yMin < 0 && verifyIntervalBrackets(f, min, initial)) {
        return solve(f, min, yMin, initial, yInitial, min, yMin);
    }

    // return the second endpoint if it is good enough
    double yMax = f.value(max);
    if (Math.abs(yMax) <= functionValueAccuracy) {
        setResult(max, 0);
        return result;
    }

    // reduce interval if initial and max bracket the root
    if (yInitial * yMax < 0 && verifyIntervalBrackets(f, initial, max)) {
        return solve(f, initial, yInitial, max, yMax, initial, yInitial);
    }

    // If no valid bracketing is found, throw an IllegalArgumentException.
    if (!verifyBracketing(f, min, initial, max)) {
        throw new IllegalArgumentException("No valid bracketing of the root was found.");
    }

    // full Brent algorithm starting with provided initial guess
    return solve(f, min, yMin, max, yMax, initial, yInitial);
}

private boolean verifyIntervalBrackets(final UnivariateRealFunction f,
                                       final double a, final double b) {
    double fa = f.value(a);
    double fb = f.value(b);
    return (fb * fa < 0) && !Double.isNaN(fa) && !Double.isNaN(fb);
}

private boolean verifyBracketing(final UnivariateRealFunction f,
                                 final double a, final double b, final double c) {
    double fa = f.value(a);
    double fb = f.value(b);
    double fc = f.value(c);
    return (fb * fa < 0 || fb * fc < 0) && !Double.isNaN(fa) && !Double.isNaN(fb) && !Double.isNaN(fc);
}