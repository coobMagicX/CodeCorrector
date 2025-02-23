public double solve(double min, double max) throws MaxIterationsExceededException, 
    FunctionEvaluationException {
    
    clearResult();
    verifyInterval(min, max);
    
    double ret = Double.NaN;
    
    double yMin = f.value(min);
    double yMax = f.value(max);
    
    
    double tol = 1e-10; 

    
    if (isCloseToZero(yMin, tol)) {
        return min;
    } else if (isCloseToZero(yMax, tol)) {
        return max;
    }

    
    int signMin = signum(yMin);
    int signMax = signum(yMax);
    if (signMin == signMax) {
        
        throw new IllegalArgumentException("Function values at endpoints do not have different signs. Endpoints: [" +
            min + ", " + max + "] Values: [" + yMin + ", " + yMax + "]");
    } else {
        