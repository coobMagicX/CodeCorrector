public double solve(double min, double max) throws MaxIterationsExceededException, 
    FunctionEvaluationException {

    clearResult();
    verifyInterval(min, max);

    double ret = Double.NaN;

    double yMin = f.value(min);
    double yMax = f.value(max);

    // Verify bracketing
    double sign = yMin * yMax;
    if (sign >= 0) {
        // check if either value is close to a zero
        // neither value is close to zero and min and max do not bracket root.
        throw new IllegalArgumentException
        ("Function values at endpoints do not have different signs." +
                "  Endpoints: [" + min + "," + max + "]" + 
                "  Values: [" + yMin + "," + yMax + "]");
    } else {
        // Solve the function if both are roots, or if only one is a root
        if (Math.abs(yMin) == 0 && Math.abs(yMax) == 0 || (Math.abs(yMin) == 0 || Math.abs(yMax) == 0)) {
            ret = min; // Assume the root is at the endpoint with zero value
        } else {
            // solve using only the first endpoint as initial guess
            double x1 = min;
            double y1 = yMin;

            if (Math.abs(yMax) != 0) { // If max is not a root, use it to find a bracketing point
                ret = solve(min, yMin, max, yMax, min, yMin);
            } else {
                ret = min; // If the only endpoint with a zero value is 'min', use it as the root
            }
        }
    }

    return ret;
}

private double solve(double x0, double y0,
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
            // the iteration has converged (the problem may
            // still be ill conditioned)
            setResult(x1, i);
            return result;
        }
        double dx = (x2 - x1);
        double tolerance =
            Math.max(relativeAccuracy * Math.abs(x1), absoluteAccuracy);
        if (Math.abs(dx) <= tolerance) {
            setResult(x1, i);
            return result;
        }
        if ((Math.abs(oldDelta) < tolerance) ||
                (Math.abs(y0) <= Math.abs(y1))) {
            // Force bisection.
            delta = 0.5 * dx;
            oldDelta = delta;
        } else {
            double r3 = y1 / y0;
            double p;
            double p1;
            // the equality test (x0 == x2) is intentional,
            // it is part of the original Brent's method,
            // it should NOT be replaced by proximity test
            if (x0 == x2) {
                // Linear interpolation.
                p = dx * r3;
                p1 = 1.0 - r3;
            } else {
                // Inverse quadratic interpolation.
                double q = y2 - y1;
                double r = y1 - y0;
                if (r != 0) {
                    q /= r;
                    p = r * (x1 - x0) / (q + Math.abs(q));
                    p1 = 2.0 * y1 - p * q / 3.0;
                } else {
                    p = 0;
                    p1 = y1;
                }
            }
            if ((p > 0 && y1 != 0) || (p < 0 && y2 != 0)) {
                delta = p / p1;
            } else {
                delta = q / 3.0;
            }
        }
        // Save old X1, Y1 
        x0 = x1;
        y0 = y1;
        // Compute new X1, Y1
        if (Math.abs(delta) > tolerance) {
            x1 = x1 + delta;
        } else if (dx > 0.0) {
            x1 = x1 + 0.5 * tolerance;
        } else if (dx <= 0.0) {
            x1 = x1 - 0.5 * tolerance;
        }
        y1 = f.value(x1);
        if ((y1 > 0) == (y2 > 0)) {
            x2 = x0;
            y2 = y0;
            delta = x1 - x0;
            oldDelta = delta;
        }
        i++;
    }
    throw new MaxIterationsExceededException(maximalIterationCount);
}