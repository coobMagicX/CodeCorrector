public double solve(double min, double max) throws MaxIterationsExceededException, 
FunctionEvaluationException {

    clearResult();
    verifyInterval(min, max);
    
    double ret = Double.NaN;
    
    double yMin = f.value(min);
    double yMax = f.value(max);
    
    // Verify bracketing or check if an endpoint is exactly zero
    if (yMin == 0) {
        return min;  // min is a root
    } else if (yMax == 0) {
        return max;  // max is a root
    } else if (yMin * yMax > 0) {
        throw new IllegalArgumentException(
            "Function values at endpoints do not have different signs and neither endpoint is zero." +
            "  Endpoints: [" + min + "," + max + "]" + 
            "  Values: [" + yMin + "," + yMax + "]");
    } else {
        // solve using the bisection method, for example
        ret = solve(min, yMin, max, yMax, min, yMin);
    }

    return ret;
}