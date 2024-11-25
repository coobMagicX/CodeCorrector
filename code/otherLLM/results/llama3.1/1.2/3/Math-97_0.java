public double solve(double min, double max) throws MaxIterationsExceededException, FunctionEvaluationException {
    
    clearResult();
    verifyInterval(min, max);
    
    double ret = Double.NaN;
    
    double yMin = f.value(min);
    double yMax = f.value(max);
    
    // Verify bracketing
    double sign = yMin * yMax;
    if (sign >= 0) {
        // check if either value is close to a zero
        if(Math.abs(yMin) < functionValueAccuracy || Math.abs(yMax) < functionValueAccuracy){
            throw new IllegalArgumentException("Function values at endpoints do not have different signs." +
                    "  Endpoints: [" + min + "," + max + "]" + 
                    "  Values: [" + yMin + "," + yMax + "]");
        } else {
            // neither value is close to zero and min and max do not bracket root.
            if(Math.signum(yMin) == Math.signum(yMax)){
                throw new IllegalArgumentException("Function values at endpoints have the same sign." +
                    "  Endpoints: [" + min + "," + max + "]" + 
                    "  Values: [" + yMin + "," + yMax + "]");
            } else {
                // solve using only the first endpoint as initial guess
                ret = solve(min, yMin, max, yMax, min, yMin);
            }
        }
    } else {
        throw new IllegalArgumentException("Function values at endpoints have different signs." +
                "  Endpoints: [" + min + "," + max + "]" + 
                "  Values: [" + yMin + "," + yMax + "]");
    }

    return ret;
}