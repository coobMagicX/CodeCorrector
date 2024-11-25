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

    // check if min and initial bracket the root
    if ((yInitial * yMin < 0)) {
        // return the point that brackets a root when it's good enough
        double midpoint = (min + yMin) / 2;
        double midpointValue = f.value(midpoint);
        if (Math.abs(midpointValue) <= functionValueAccuracy) {
            setResult(midpoint, 0);
            return result;
        }
    } else {
        throw new IllegalArgumentException("The interval does not bracket a root.");
    }

    // check if initial and max bracket the root
    double yMax = f.value(max);
    if (yInitial * yMax < 0) {
        // return the point that brackets a root when it's good enough
        double midpoint = (initial + max) / 2;
        double midpointValue = f.value(midpoint);
        if (Math.abs(midpointValue) <= functionValueAccuracy) {
            setResult(midpoint, 0);
            return result;
        }
    } else {
        throw new IllegalArgumentException("The interval does not bracket a root.");
    }

    // full Brent algorithm starting with provided initial guess
    return solve(f, min, yMin, max, yMax, initial, yInitial);

}