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

    // Ensure that we have a valid bracketing interval for the root
    if (yInitial * yMin >= 0) {
        throw new IllegalArgumentException("The initial guess and minimum do not bracket a root.");
    }

    // Ensure that the provided interval brackets a root
    double yMax = f.value(max);
    if (yInitial * yMax < 0) {
        // Initial and max bracket the root, proceed with solving the reduced interval
        return solve(f, initial, yMin, max, yMax, initial, yInitial);
    } else if (Math.abs(yMax) <= functionValueAccuracy) {
        // Max is good enough, set result to max and return
        setResult(max, 0);
        return result;
    } else {
        throw new IllegalArgumentException("The interval does not bracket a root.");
    }
}

// A helper method to verify the sequence (min, initial, max)
private void verifySequence(double min, double initial, double max) throws IllegalArgumentException {
    if (initial <= min || initial >= max) {
        throw new IllegalArgumentException("Initial value must be between min and max.");
    }
}