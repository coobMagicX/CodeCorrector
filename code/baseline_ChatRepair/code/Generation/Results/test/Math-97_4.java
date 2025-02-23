public double solve(double min, double max) throws MaxIterationsExceededException, 
    FunctionEvaluationException {
    
    clearResult();
    verifyInterval(min, max);
    
    double ret = Double.NaN;
    
    double yMin = f.value(min);
    double yMax = f.value(max);
    
    
    double zeroTolerance = 1e-15; 

    
    if (Math.abs(yMin) < zeroTolerance) {
        return min;
    } else if (Math.abs(yMax) < zeroTolerance) {
        return max;
    }

    
    if (yMin * yMax > 0) {
        
        throw new IllegalArgumentException("Function values at endpoints do not have different signs. Endpoints: [" + min + "," + max + 
            "] Values: [" + yMin + "," + yMax + "]");
    } else {
        
        
        ret = solve(min,