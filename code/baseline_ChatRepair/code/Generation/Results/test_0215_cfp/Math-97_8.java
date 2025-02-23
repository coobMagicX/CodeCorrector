public double solve(double min, double max) throws MaxIterationsExceededException, FunctionEvaluationException {
    
    clearResult();
    verifyInterval(min, max);
    
    double ret = Double.NaN;
    
    double yMin = f.value(min);
    double yMax = f.value(max);
    
    
    double tol = 1e-15; 

    
    if (Math.abs(yMin) < tol) {
        return min; 
    } else if (Math.abs(yMax) < tol) {
        return max; 
    }

    
    double sign = yMin * yMax;

    
    if (sign > 0) {
        throw new IllegalArgumentException(
            "Function values at endpoints do not have different signs.  Endpoints: [" + min + ", " + max + 
            "]  Values: [" + yMin + ", " + yMax + "]");  
    } else {
        