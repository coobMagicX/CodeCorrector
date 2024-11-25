public double solve(double min, double max) throws MaxIterationsExceededException, FunctionEvaluationException {
    
    clearResult();
    verifyInterval(min, max);
    
    double ret = Double.NaN;
    
    double yMin = f.value(min);
    double yMax = f.value(max);
    
    // Verify bracketing
    double sign = yMin * yMax;
    if (sign >= 0) {
        throw new IllegalArgumentException("Function values at endpoints do not have different signs." +
                "  Endpoints: [" + min + "," + max + "]" +
                "  Values: [" + yMin + "," + yMax + "]");
    } else {
        // Check if either endpoint is a root
        if (Math.abs(yMin) <= functionValueAccuracy || Math.abs(yMax) <= functionValueAccuracy) {
            throw new IllegalArgumentException("Endpoint itself is the root being sought");
        }
        
        // Solve using only the first endpoint as initial guess
        ret = solve(min, yMin, max, yMax, min, yMin);
    }

    return ret;
}