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
        // Check if either endpoint is the root
        if (yMin == 0 || yMax == 0) {
            return min; // or max, depending on which one is closer to zero
        }
        
        ret = solve(min, yMin, max, yMax, min, yMin);
    }

    return ret;
}