public double solve(double min, double max) throws MaxIterationsExceededException, 
    FunctionEvaluationException {
    
    clearResult();
    verifyInterval(min, max);
    
    double ret = Double.NaN;
    
    double yMin = f.value(min);
    double yMax = f.value(max);
    
    // Verify bracketing
    double sign = yMin * yMax;
    if (sign >= 0) {
        // check if either value is close to a zero
        if (Math.abs(yMin) <= functionValueAccuracy || Math.abs(yMax) <= functionValueAccuracy) {
            // check which endpoint is closer to zero and use it as a guess
            if (Math.abs(yMin) < Math.abs(yMax)) {
                ret = min;
            } else {
                ret = max;
            }
            setResult(ret, 0);
            return ret;
        } else {
            // neither value is close to zero and min and max do not bracket root.
            throw new IllegalArgumentException
            ("Function values at endpoints do not have different signs." +
                    "  Endpoints: [" + min + "," + max + "]" + 
                    "  Values: [" + yMin + "," + yMax + "]");
        }
    } else {
        // solve using both endpoints as bracket
        ret = solve(min, yMin, max, yMax, min, yMin);
        // either min or max is a root
    }

    return ret;
}