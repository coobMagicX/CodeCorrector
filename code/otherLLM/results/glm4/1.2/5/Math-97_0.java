public double solve(double min, double max) throws MaxIterationsExceededException, FunctionEvaluationException {
    
    clearResult();
    verifyInterval(min, max);
    
    double ret = Double.NaN;
    
    double yMin = f.value(min);
    double yMax = f.value(max);
    
    // Verify bracketing
    double sign = yMin * yMax;
    if (sign >= 0) {
        if (Math.abs(yMin) < functionValueAccuracy || Math.abs(yMax) < functionValueAccuracy) {
            // One of the endpoints is a root, which can be handled by passing the midpoint as initial guess
            double midPoint = 0.5 * (min + max);
            ret = solve(midPoint, f.value(midPoint), min, yMin, max, yMax);
        } else {
            throw new IllegalArgumentException("Function values at endpoints do not have different signs." +
                    "  Endpoints: [" + min + "," + max + "]" + 
                    "  Values: [" + yMin + "," + yMax + "]");
        }
    } else {
        // Solve using the standard method with both endpoints
        ret = solve(min, yMin, max, yMax, min, yMin);
    }

    return ret;
}

// The private method solve can remain unchanged as it does not need to be modified for handling roots at endpoints.