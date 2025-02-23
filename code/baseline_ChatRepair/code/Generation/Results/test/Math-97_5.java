public double solve(double min, double max) throws MaxIterationsExceededException, 
    FunctionEvaluationException {
    
    clearResult();
    verifyInterval(min, max);
    
    double ret = Double.NaN;
    
    double yMin = f.value(min);
    double yMax = f.value(max);

    
    double sign = yMin * yMax;
    if (sign > 0) {
        throw new IllegalArgumentException("Function values at endpoints do not have different signs. " +
                                           "Endpoints: [" + min + "," + max + "], " + 
                                           "Values: [" + yMin + "," + yMax + "]");
    } else if (sign == 0) {
        
        if (yMin == 0) {
            return min;
        } else if (yMax == 0) {
            return max;
        }
    } else {
        
        
        ret = solve(min, yMin, max, yMax, min, yMin);
    }

    return ret;
}
