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
    if (yInitial * yMin < 0) {
        double mid = computeMidpoint(min, yMin, initial);
        double yMid = f.value(mid);
        if (Math.abs(yMid) <= functionValueAccuracy) {
            setResult(mid, yMid);
            return result;
        }
        // Call the recursive method with updated values
        return solve(f, min, mid, mid, max, initial, yInitial);
    }

    // return the second endpoint if it is good enough
    double yMax = f.value(max);
    if (Math.abs(yMax) <= functionValueAccuracy) {
        setResult(yMax, 0);
        return result;
    }

    // reduce interval if initial and max bracket the root
    if (yInitial * yMax < 0) {
        double mid = computeMidpoint(initial, yInitial, max);
        double yMid = f.value(mid);
        if (Math.abs(yMid) <= functionValueAccuracy) {
            setResult(mid, yMid);
            return result;
        }
        // Call the recursive method with updated values
        return solve(f, initial, mid, mid, max, initial, yInitial);
    }

    if (yMin * yMax > 0) {
        throw MathRuntimeException.createIllegalArgumentException(
              NON_BRACKETING_MESSAGE, min, max, yMin, yMax);
    }

    // full Brent algorithm starting with provided initial guess
    return brentMethod(f, min, initial, max);
}

private double computeMidpoint(double a, double b, double c) {
    double mid = (a + b) / 2;
    if (Math.abs(mid - a) < Math.abs(mid - c)) {
        return mid;
    } else if (Math.abs(mid - a) > Math.abs(mid - c)) {
        return c;
    } else {
        // To avoid numerical issues, select the midpoint that is not equal to 'a' or 'c'
        double alternative = (b + c) / 2;
        return Math.abs(alternative - b) < Math.abs(alternative - c) ? alternative : c;
    }
}

private double brentMethod(final UnivariateRealFunction f, final double min, final double initial, final double max)
    throws FunctionEvaluationException {
    // Brent's method implementation goes here
}