public double solve(double min, double max) throws MaxIterationsExceededException, 
    FunctionEvaluationException {
    
    clearResult();
    verifyInterval(min, max);
    
    double ret = Double.NaN;
    
    double yMin = f.value(min);
    double yMax = f.value(max);
    
    
    double tol = 1e-15; 

    
    if (Math.abs(yMin * yMax) < tol) {
        
        if(Math.abs(yMin) < tol) return min;
        if(Math.abs(yMax) < tol) return max;
        

        throw new IllegalArgumentException("Function values at endpoints are nearly zero, floating-point precision might be insufficient. " +
                "Endpoints: [" + min + ", " + max + "] Values: [" + yMin + ", " + yMax + "]");
    } else if (yMin * yMax > 0) {
        
        throw new IllegalArgumentException("