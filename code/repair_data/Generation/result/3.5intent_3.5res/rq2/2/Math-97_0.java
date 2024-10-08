public double solve(double min, double max) throws MaxIterationsExceededException, FunctionEvaluationException {

    clearResult();
    verifyInterval(min, max);

    double ret = Double.NaN;

    double yMin = f.value(min);
    double yMax = f.value(max);

    // Verify bracketing
    double sign = yMin * yMax;
    if (sign >= 0) {
        if (Math.abs(yMin) <= functionValueAccuracy) {
            // min is a root
            ret = min;
        } else if (Math.abs(yMax) <= functionValueAccuracy) {
            // max is a root
            ret = max;
        } else {
            // neither value is close to zero and min and max do not bracket root.
            throw new IllegalArgumentException
            ("Function values at endpoints do not have different signs." +
                    "  Endpoints: [" + min + "," + max + "]" +
                    "  Values: [" + yMin + "," + yMax + "]");
        }
    } else {
        // solve using only the first endpoint as initial guess
        ret = solve(min, yMin, max, yMax, min, yMin);
        // either min or max is a root
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
                double r1 = y0 / y2;
                double r2 = y1 / y2;
                p = r3 * (dx * r1 * (r1 - r2) - (x1 - x0) * (r2 - 1.0));
                p1 = (r1 - 1.0) * (r2 - 1.0) * (r3 - 1.0);
            }
            if (p > 0.0) {
                p1 = -p1;
            } else {
                p = -p;
            }
            if (2.0 * p >= 1.5 * dx * p1 - Math.abs(tolerance * p1) ||
                    p >= Math.abs(0.5 * oldDelta * p1)) {
                // Inverse quadratic interpolation gives a value
                // in the wrong direction, or progress is slow.
                // Fall back to bisection.
                delta = 0.5 * dx;
                oldDelta = delta;
            } else {
                oldDelta = delta;
                delta = p / p1;
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