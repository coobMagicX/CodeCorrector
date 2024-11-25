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
        // Check for a root at one of the endpoints by evaluating at a midpoint.
        double midValue = f.value((min + max) / 2);
        if ((midValue == 0) || (Math.abs(midValue) <= functionValueAccuracy)) {
            // If one endpoint is a root, find it and return that value.
            ret = (Math.abs(yMin) < Math.abs(yMax)) ? min : max;
        } else {
            throw new IllegalArgumentException(
                "Function values at endpoints do not have different signs." +
                "  Endpoints: [" + min + "," + max + "]" + 
                "  Values: [" + yMin + "," + midValue + "]");
        }
    } else {
        // Solve using the entire interval, which includes both endpoints.
        ret = solve(min, yMin, (min + max) / 2, f.value((min + max) / 2), max, yMax);
    }

    return ret;
}

// The method solve(double x0, double y0, double x1, double y1, double x2, double y2)
// is already correctly implemented and does not need any changes.