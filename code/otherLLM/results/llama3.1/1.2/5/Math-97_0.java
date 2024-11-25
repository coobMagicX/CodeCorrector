public double solve(double min, double max) throws MaxIterationsExceededException, FunctionEvaluationException {
    
    clearResult();
    verifyInterval(min, max);
    
    double ret = Double.NaN;
    
    double yMin = f.value(min);
    double yMax = f.value(max);
    
    // Verify bracketing
    double sign = yMin * yMax;
    if (sign >= 0) {
        throw new IllegalArgumentException
            ("Function values at endpoints do not have different signs." +
                    "  Endpoints: [" + min + "," + max + "]" + 
                    "  Values: [" + yMin + "," + yMax + "]");
    } else {
        // solve using only the first endpoint as initial guess
        ret = solve(min, f.value(min), max, f.value(max), min, yMin);
        if (Math.abs(ret - min) < 1e-9 || Math.abs(f.value(ret) - yMin) < 1e-9) {
            return ret; // either min or max is a root
        }
        
        double temp = solve(min, f.value(min), max, f.value(max), max, yMax);
        if (Math.abs(temp - max) < 1e-9 || Math.abs(f.value(temp) - yMax) < 1e-9) {
            return temp; // either min or max is a root
        }
        
        throw new MaxIterationsExceededException("Failed to find the root within maximum iterations");
    }

    return ret;
}