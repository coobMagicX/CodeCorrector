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

    // check if min and max are endpoints
    if (yMin * yMax > 0) {
        throw MathRuntimeException.createIllegalArgumentException(
              NON_BRACKETING_MESSAGE, min, max, yMin, yMax);
    }

    // reduce interval if min and initial bracket the root
    if (yInitial * yMin < 0) {
        return solve(f, min, yMin, initial, yInitial, min, yMin);
    }

    // reduce interval if initial and max bracket the root
    if (yInitial * yMax < 0) {
        return solve(f, initial, yInitial, max, yMax, initial, yInitial);
    }

    // return the second endpoint if it is good enough
    double yMax = f.value(max);
    if (Math.abs(yMax) <= functionValueAccuracy) {
        setResult(yMax, 0);
        return result;
    }

    // full Brent algorithm starting with provided initial guess
    return solve(f, min, yMin, max, yMax, initial, yInitial);

}

private double solve(final UnivariateRealFunction f,
                     double x0, double y0,
                     double x1, double y1,
                     double x2, double y2)
throws MaxIterationsExceededException, FunctionEvaluationException {

    double delta = x1 - x0;
    double oldDelta = delta;

    int i = 0;
    while (i < maximalIterationCount) {
        if (Math.abs(y2) < Math.abs(y1)) {
            // use the bracket point if is better than last approximation
            x0 = x1;
            x1 = x2;
            x2 = x0;
            y0 = y1;
            y1 = y2;
            y2 = y0;
        }
        if (Math.abs(y1) <= functionValueAccuracy) {
            // Avoid division by very small values. Assume
            // the root has been found
            return x1;
        }
        if ((y1 > 0) == (y2 > 0)) {
            x2 = x0;
            y2 = y0;
            delta = x1 - x0;
            oldDelta = delta;
        }
        // Save old X1, Y1
        double oldX1 = x1;
        double oldY1 = y1;

        // Compute new X1, Y1
        if (Math.abs(delta) > tolerance) {
            x1 = x1 + delta;
        } else if (delta > 0.0) {
            x1 = x1 + 0.5 * tolerance;
        } else if (delta <= 0.0) {
            x1 = x1 - 0.5 * tolerance;
        }
        y1 = f.value(x1);

        // Save old X2, Y2
        double oldX2 = x2;
        double oldY2 = y2;

        // Compute new X2, Y2
        if (Math.abs(delta) > tolerance) {
            x2 = x2 + delta;
        } else if (delta > 0.0) {
            x2 = x2 + 0.5 * tolerance;
        } else if (delta <= 0.0) {
            x2 = x2 - 0.5 * tolerance;
        }
        y2 = f.value(x2);

        i++;
    }
    throw new MaxIterationsExceededException(maximalIterationCount);
}