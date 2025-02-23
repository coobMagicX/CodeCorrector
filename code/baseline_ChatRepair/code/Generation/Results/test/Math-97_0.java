public double solve(double min, double max) throws MaxIterationsExceededException, 
    FunctionEvaluationException {
    
    clearResult();
    verifyInterval(min, max);
    
    double ret = Double.NaN;
    
    double yMin = f.value(min);
    double yMax = f.value(max);
    
    
    final double closeZeroThreshold = 1e-10;

    
    if (Math.abs(yMin) < closeZeroThreshold) {
        return min;
    } 
    if (Math.abs(yMax) < closeZeroThreshold) {
        return max;
    }

    
    double sign = yMin * yMax;
    if (sign > 0) {
        
        throw new IllegalArgumentException("Function values at endpoints do not have different signs. Endpoints: [" 
            + min + "," + max + "], Values: [" + yMin + "," + yMax + "]");
    } else {
        
        ret = solve(min, yMin, max, yMax, min, yMin);
    }

    return ret;
}
