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
            // neither value is close to zero and min and max do not bracket root.
            throw new IllegalArgumentException
            ("Function values at endpoints do not have different signs." +
                    "  Endpoints: [" + min + "," + max + "]" + 
                    "  Values: [" + yMin + "," + yMax + "]");
    } else {
        // solve using only the first endpoint as initial guess and check if it's a root
        double x0 = min;
        double y0 = yMin;
        double ret1 = solve(x0, y0, max, yMax, x0, y0);
        
        // check if the other endpoint is a root
        double x2 = max;
        double y2 = yMax;
        double ret2 = solve(min, yMin, x2, y2, min, yMin);

        // return the root that was found first
        return Double.isNaN(ret1) ? ret2 : ret1;
    }

    return ret;
}