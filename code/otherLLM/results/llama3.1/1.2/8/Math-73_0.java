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

    // return the second endpoint if it is good enough
    double yMax = f.value(max);
    if (Math.abs(yMax) <= functionValueAccuracy) {
        setResult(yMax, 0);
        return result;
    }

    // bracketing check
    if (!bracketRoot(f, min, max)) {
        throw new IllegalArgumentException("Invalid intervals or endpoints");
    }

    // reduce interval if initial and max bracket the root
    double yInitialMin = f.value(min);
    if (yInitial * yInitialMin < 0) {
        return solve(f, min, yInitialMin, max, yMax, initial, yInitial);
    }

    // full Brent algorithm starting with provided initial guess
    return solve(f, min, yMin, max, yMax, initial, yInitial);

}

private boolean bracketRoot(final UnivariateRealFunction f,
                            final double x0, final double y0,
                            final double x1, final double y1)
{
    // Check if y0 * y1 < 0
    return (y0 > 0) != (y1 > 0);
}

private boolean bracketRoot(final UnivariateRealFunction f,
                            final double min, final double max)
{
    // Check if yMin * yMax < 0
    double yMin = f.value(min);
    double yMax = f.value(max);
    return (yMin > 0) != (yMax > 0);
}