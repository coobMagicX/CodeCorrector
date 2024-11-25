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

    // validate interval and endpoints
    if (!isValidInterval(min, max)) {
        throw new IllegalArgumentException("The input interval does not bracket a root.");
    }
    if (!isValidEndpoint(min, f.value(min))) {
        throw new IllegalArgumentException("Min endpoint is invalid.");
    }
    if (!isValidEndpoint(max, f.value(max))) {
        throw new IllegalArgumentException("Max endpoint is invalid.");
    }

    // reduce interval if min and initial bracket the root
    double yMin = f.value(min);
    if (yInitial * yMin < 0) {
        return solve(f, min, max, initial, yInitial, min, yMin);
    }

    // return the second endpoint if it is good enough
    double yMax = f.value(max);
    if (Math.abs(yMax) <= functionValueAccuracy) {
        setResult(yMax, 0);
        return result;
    }

    // reduce interval if initial and max bracket the root
    if (yInitial * yMax < 0) {
        return solve(f, initial, max, min, yMin, initial, yInitial);
    }


    // full Brent algorithm starting with provided initial guess
    return solve(f, min, max, initial, yInitial, min, yMin);

}

private boolean isValidInterval(double min, double max) {
    return !Double.isNaN(min) && !Double.isNaN(max) && (min <= max);
}

private boolean isValidEndpoint(double value, double evaluatedValue) {
    return !Double.isNaN(value) && !Double.isNaN(evaluatedValue) && Math.abs(evaluatedValue) > functionValueAccuracy;
}