public double solve(double min, double max) throws MaxIterationsExceededException, FunctionEvaluationException {

    clearResult();
    verifyInterval(min, max);
    
    double ret = Double.NaN;
    
    double yMin = f.value(min);
    double yMax = f.value(max);
    
    // Verify bracketing
    double sign = yMin * yMax;
    if (sign > 0) {
        if (yMin == 0) {
            ret = min;
        } else if (yMax == 0) {
            ret = max;
        } else {
            throw new IllegalArgumentException
            ("Function values at endpoints do not have different signs." +
                    "  Endpoints: [" + min + "," + max + "]" + 
                    "  Values: [" + yMin + "," + yMax + "]");
        }
    } else {
        // solve using only the first endpoint as initial guess
        ret = solve(min, yMin, max, yMax, min, yMin);
    }

    return ret;
}