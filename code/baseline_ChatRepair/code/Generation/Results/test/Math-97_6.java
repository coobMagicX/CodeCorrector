public double solve(double min, double max) throws MaxIterationsExceededException, 
    FunctionEvaluationException {
    
    clearResult();
    verifyInterval(min, max);
    
    double ret = Double.NaN;
    
    double yMin = f.value(min);
    double yMax = f.value(max);
    
    
    double zeroThreshold = 1e-14; 

    
    if (Math.abs(yMin) < zeroThreshold) {
        ret = min;
        return ret;
    }
    if (Math.abs(yMax) < zeroThreshold) {
        ret = max;
        return ret;
    }

    
    if (yMin * yMax > 0) {
        throw new IllegalArgumentException("Function values at endpoints do not have different signs. " + 
            "Endpoints: [" + min + ", " + max + "], " + "Values: [" + yMin + ", " + yMax + "]");
    }

    
    
    ret = solveUsingBracketingMethod(min, yMin, max, yMax);
