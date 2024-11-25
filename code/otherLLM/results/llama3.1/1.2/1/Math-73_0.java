public double solve(final UnivariateRealFunction f,
                    final double min, final double max, final double initial)
    throws MaxIterationsExceededException, FunctionEvaluationException {

    clearResult();
    verifySequence(min, initial, max);

    // check for bad endpoints in the interval
    if (min >= max) {
        throw new IllegalArgumentException("Invalid interval: min = " + min + ", max = " + max);
    }

    double yInitial = f.value(initial);
    if (Math.abs(yInitial) <= functionValueAccuracy) {
        setResult(initial, 0);
        return result;
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

    // verify that the initial guess and both endpoints bracket the root of the function
    if ((yInitial * yMin > 0 && yInitial * yMax > 0) || (yInitial * yMin < 0 && yInitial * yMax < 0)) {
        throw new IllegalArgumentException("Interval does not bracket a root: min = " + min + ", max = " + max);
    }

    // reduce interval if min and initial bracket the root
    if (yInitial * yMin < 0) {
        return solve(f, min, yMin, initial, yInitial, min, yMin);
    }

    // reduce interval if initial and max bracket the root
    if (yInitial * yMax < 0) {
        return solve(f, initial, yInitial, max, yMax, initial, yInitial);
    }


    // full Brent algorithm starting with provided initial guess
    return solve(f, min, yMin, max, yMax, initial, yInitial);

}