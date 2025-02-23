public double solve(double min, double max) throws MaxIterationsExceededException, 
    FunctionEvaluationException {

    clearResult();
    verifyInterval(min, max);
    
    double ret = Double.NaN;

    
    double yMin = f.value(min);
    double yMax = f.value(max);

    
    double tolerance = 1e-15; 

    
    if (Math.abs(yMin) < tolerance) {
        return min; 
    }
    if (Math.abs(yMax) < tolerance) {
        return max; 
    }

    
    if (yMin * yMax > 0) {
        throw new IllegalArgumentException(
            "Function values at endpoints do not have different signs." +
            " Endpoints: [" + min + ", " + max + "]" +
            " Values: [" + yMin + ", " + yMax