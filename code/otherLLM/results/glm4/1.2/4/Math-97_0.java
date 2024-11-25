public double solve(double min, double max) throws MaxIterationsExceededException, FunctionEvaluationException {
    
    clearResult();
    verifyInterval(min, max);
    
    double ret = Double.NaN;
    
    double yMin = f.value(min);
    double yMax = f.value(max);
    
    // Verify bracketing
    double sign = yMin * yMax;
    if (sign >= 0) {
        // check if either value is close to a zero or one of the endpoints is zero
        if ((Math.abs(yMin) < EPSILON || Math.abs(yMax) < EPSILON) || (min == 0 || max == 0)) {
            if (Math.abs(yMin) < EPSILON) { // min is a root
                ret = min;
            } else if (Math.abs(yMax) < EPSILON) { // max is a root
                ret = max;
            } else {
                throw new IllegalArgumentException
                ("Function values at endpoints do not have different signs or one of the endpoints is zero." +
                        "  Endpoints: [" + min + "," + max + "]" + 
                        "  Values: [" + yMin + "," + yMax + "]");
            }
        } else {
            // neither value is close to zero and min and max do not bracket root.
            throw new IllegalArgumentException
            ("Function values at endpoints do not have different signs." +
                    "  Endpoints: [" + min + "," + max + "]" + 
                    "  Values: [" + yMin + "," + yMax + "]");
        }
    } else {
        // solve using only the first endpoint as initial guess if it's not a root
        if (min != 0 && Math.abs(yMin) > EPSILON) {
            ret = solve(min, yMin, max, yMax, min, yMin);
        } else {
            // either min or max is a root
            if (Math.abs(yMin) < EPSILON) { // min is a root
                ret = min;
            } else { // max is a root
                ret = max;
            }
        }
    }

    return ret;
}