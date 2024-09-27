public double solve(double min, double max) throws MaxIterationsExceededException, 
    FunctionEvaluationException {

    clearResult();
    verifyInterval(min, max);
    
    double ret = Double.NaN;
    
    double yMin = f.value(min);
    double yMax = f.value(max);
    
    // Verify bracketing
    double sign = yMin * yMax;
    if (sign > 0 || yMin == 0 || yMax == 0) {
        // check if either value is close to a zero
        // either value is zero or min and max bracket the root
        ret = solve(min, yMin, max, yMax, min, yMin);
    } else if (sign < 0) {
        // solve using only the first endpoint as initial guess
        ret = solve(min, yMin, max, yMax, min, yMin);
    } else {
        // both values are zero
        ret = min;
    }

    return ret;
}