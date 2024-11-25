public double solve(final UnivariateRealFunction f,
                    final double min, final double max, final double initial)
        throws MaxIterationsExceededException, FunctionEvaluationException {

    clearResult();
    verifySequence(min, initial, max);

    // return the initial guess if it is good enough
    double yInitial = f.value(initial);
    if (Math.abs(yInitial) <= getAbsoluteAccuracy()) {
        setResult(initial, 0);
        return result;
    }

    // return the first endpoint if it is good enough
    double yMin = f.value(min);
    if (Math.abs(yMin) <= getAbsoluteAccuracy()) {
        setResult(min, 0);
        return result;
    }

    // reduce interval if min and initial bracket the root
    if (yInitial * yMin < 0) {
        double mid = (min + yMin) / 2.0; // Use midpoint to avoid recursion
        double yMid = f.value(mid);
        if (Math.abs(yMid) <= getAbsoluteAccuracy()) {
            setResult(mid, 0);
            return result;
        }
        // Update interval based on the sign of the derivative at the midpoint
        if ((yInitial > 0 && yMid < 0) || (yInitial < 0 && yMid > 0)) {
            return solve(f, min, mid, initial);
        } else {
            return solve(f, mid, max, yMid);
        }
    }

    // return the second endpoint if it is good enough
    double yMax = f.value(max);
    if (Math.abs(yMax) <= getAbsoluteAccuracy()) {
        setResult(max, 0);
        return result;
    }

    // reduce interval if initial and max bracket the root
    if (yInitial * yMax < 0) {
        double mid = (initial + max) / 2.0; // Use midpoint to avoid recursion
        double yMid = f.value(mid);
        if (Math.abs(yMid) <= getAbsoluteAccuracy()) {
            setResult(mid, 0);
            return result;
        }
        // Update interval based on the sign of the derivative at the midpoint
        if ((yInitial > 0 && yMid < 0) || (yInitial < 0 && yMid > 0)) {
            return solve(f, initial, mid, yMax);
        } else {
            return solve(f, mid, max, yMax);
        }
    }

    if (yMin * yMax > 0) {
        throw MathRuntimeException.createIllegalArgumentException(
              NON_BRACKETING_MESSAGE, min, max, yMin, yMax);
    }

    // full Brent algorithm starting with provided initial guess
    return solve(f, min, yMin, max, yMax, initial, yInitial);
}