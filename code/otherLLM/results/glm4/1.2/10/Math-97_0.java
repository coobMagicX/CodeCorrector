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
        boolean isRootAtMin = Math.abs(yMin) < Double.MIN_NORMAL;
        boolean isRootAtMax = Math.abs(yMax) < Double.MIN_NORMAL;

        if (!isRootAtMin && !isRootAtMax) {
            throw new IllegalArgumentException(
                "Function values at endpoints do not have different signs." +
                "  Endpoints: [" + min + "," + max + "]" +
                "  Values: [" + yMin + "," + yMax + "]");
        } else if (isRootAtMin && isRootAtMax) {
            // Both endpoints are roots, use any of them as the root
            ret = min; // or max, since both are roots
        } else if (isRootAtMin) {
            // Root is at the minimum endpoint
            ret = solve(min, yMin, max, yMax, min, yMin);
        } else if (isRootAtMax) {
            // Root is at the maximum endpoint
            ret = solve(min, yMin, max, yMax, max, yMax);
        }
    } else {
        // Solve using only the first endpoint as initial guess
        ret = solve(min, yMin, max, yMax, min, yMin);
    }

    return ret;
}