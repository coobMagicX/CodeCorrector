public double solve(double min, double max) throws MaxIterationsExceededException, 
FunctionEvaluationException {

    clearResult();
    verifyInterval(min, max);

    double ret = Double.NaN;

    double yMin = f.value(min);
    double yMax = f.value(max);

    // Verify bracketing
    double sign = yMin * yMax;
    if (sign > 0) {
        // Neither value is zero and min and max do not bracket root.
        throw new IllegalArgumentException
        ("Function values at endpoints do not have different signs." +
                "  Endpoints: [" + min + "," + max + "]" + 
                "  Values: [" + yMin + "," + yMax + "]");
    } else if (sign == 0) {
        if (yMin == 0) {
            return min; // min is a root
        } else if (yMax == 0) {
            return max; // max is a root
        }
    }

    // If the function passes the checks but no endpoint is the root, proceed to solve
    ret = solve(min, yMin, max, yMax, min, yMin);

    return ret;
}